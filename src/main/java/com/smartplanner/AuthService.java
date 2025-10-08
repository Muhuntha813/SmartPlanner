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

    public boolean createUser(String username, String password,String currentOccupation,
                              String currentExperience) {
        String sql = """
        INSERT INTO public.users (username, password, currentoccupation, currentexperience)
        VALUES (?, ?, ?, ?)
        ON CONFLICT (username) DO UPDATE
        SET password = EXCLUDED.password,
            currentoccupation = EXCLUDED.currentoccupation,
            currentexperience = EXCLUDED.currentexperience
        """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3,currentOccupation);
            ps.setString(4,currentExperience);
            int updated = ps.executeUpdate();   // 1 if inserted, 0 if conflict
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
