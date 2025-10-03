package com.smartplanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class AuthService {

    public boolean checkCredentials(String username, String password) {
        String sql = "SELECT 1 FROM public.users WHERE username = ? AND password = ? LIMIT 1";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if a row exists
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createUser(String username, String password) {
        String sql = """
            INSERT INTO public.users (username, password)
            VALUES (?, ?)
            ON CONFLICT (username) DO NOTHING
            """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            int updated = ps.executeUpdate();   // 1 if inserted, 0 if conflict
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
