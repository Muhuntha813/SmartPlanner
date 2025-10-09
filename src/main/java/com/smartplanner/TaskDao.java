package com.smartplanner;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public List<Task> listByUser(String username) throws Exception {
        String sql = """
            SELECT id, title, due_date, done
            FROM public.tasks
            WHERE username = ?
            ORDER BY COALESCE(due_date, DATE '9999-12-31') ASC, id DESC
        """;
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                List<Task> out = new ArrayList<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String title = rs.getString("title");
                    Date d = rs.getDate("due_date");
                    LocalDate due = (d == null) ? null : d.toLocalDate();
                    boolean done = rs.getBoolean("done");
                    out.add(new Task(id, title, due, done));
                }
                return out;
            }
        }
    }

    /** Insert and return the created Task (with DB id) */
    public Task insert(String username, String title, LocalDate due) throws Exception {
        String sql = """
            INSERT INTO public.tasks(username, title, due_date)
            VALUES (?, ?, ?)
            RETURNING id, title, due_date, done
        """;
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, title);
            if (due == null) ps.setNull(3, Types.DATE);
            else ps.setDate(3, Date.valueOf(due));

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                long id = rs.getLong("id");
                String t = rs.getString("title");
                Date d = rs.getDate("due_date");
                LocalDate dueBack = (d == null) ? null : d.toLocalDate();
                boolean done = rs.getBoolean("done");
                return new Task(id, t, dueBack, done);
            }
        }
    }

    public void delete(long id) throws Exception {
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM public.tasks WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public void updateTitle(long id, String title) throws Exception {
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE public.tasks SET title = ? WHERE id = ?")) {
            ps.setString(1, title);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void updateDue(long id, LocalDate due) throws Exception {
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE public.tasks SET due_date = ? WHERE id = ?")) {
            if (due == null) ps.setNull(1, Types.DATE);
            else ps.setDate(1, Date.valueOf(due));
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }
}
