package com.smartplanner;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Db {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Db.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties not found on classpath");
            }
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }

    public static Connection getConnection() throws Exception {
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String pass = props.getProperty("password");
        return DriverManager.getConnection(url, user, pass);
    }
}
