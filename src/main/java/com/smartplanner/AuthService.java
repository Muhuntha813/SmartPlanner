package com.smartplanner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

    public class AuthService {

    //class to carry info to the dashboard later !!
    public static class userProfile{
        public final String userName;
        public final String currentOccupation;
        public final String currentExperience;

        public userProfile(String userName, String currentOccupation, String currentExperience) {
            this.userName = userName;
            this.currentOccupation = currentOccupation;
            this.currentExperience = currentExperience;
        }
    }

        // --- 2) Fetch the current occupation & experience for a username ---
    public userProfile getUserProfile(String username) {
            String sql = """
            SELECT username, currentoccupation, currentexperience
            FROM public.users
            WHERE username = ?
            LIMIT 1
            """;
            try (Connection conn = Db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new userProfile(
                                rs.getString("username"),
                                rs.getString("currentoccupation"),
                                rs.getString("currentexperience")
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // If not found, still return a profile with safe fallbacks
            return new userProfile(username, null, null);
        }


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
            int updated = ps.executeUpdate();   // 1 if inserted 0 if conflict
            return updated == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
