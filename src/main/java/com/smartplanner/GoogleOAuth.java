package com.smartplanner;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Google OAuth helper for desktop/JavaFX apps.
 * - Expects /credentials.json on the classpath (src/main/resources/credentials.json)
 * - Stores tokens under ./tokens/ so you only sign in once
 * - Uses the Calendar "events" scope (create/edit events)
 */
public final class GoogleOAuth {
    // GoogleOAuth.java
    private static final List<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/calendar.events",
            "https://www.googleapis.com/auth/tasks"
    );


    private static final JacksonFactory JSON = JacksonFactory.getDefaultInstance();
    private static final Path TOKEN_DIR = Path.of("tokens");   // persisted refresh tokens
    private static Credential CACHED;                          // in-memory cache per run

    private GoogleOAuth() {}

    /** Returns a valid Credential (triggers browser OAuth on first use). */
    public static synchronized Credential getCredential() throws Exception {
        if (CACHED != null) return CACHED;

        if (!Files.exists(TOKEN_DIR)) Files.createDirectories(TOKEN_DIR);

        // Load OAuth client secrets from resources
        InputStream in = GoogleOAuth.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new IllegalStateException(
                    "credentials.json not found. Put it in src/main/resources/credentials.json");
        }
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON, new InputStreamReader(in));

        var http = GoogleNetHttpTransport.newTrustedTransport();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                http, JSON, secrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(TOKEN_DIR.toFile()))
                .setAccessType("offline")        // get refresh token
                .build();

        // Local web server receiver for installed-app flow (http://localhost:8888)
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888)
                .build();

        CACHED = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return CACHED;
    }

    /** Convenience: get the current OAuth access token (auto-refreshes if needed). */
    public static String getAccessToken() throws Exception {
        return getCredential().getAccessToken();
    }

    /** Optional: simple flag to know if tokens are already present. */
    public static boolean isConnected() {
        return Files.exists(TOKEN_DIR.resolve("StoredCredential"));
    }

    /** Optional: drop in-memory reference (does not delete disk tokens). */
    public static void disconnectInMemory() {
        CACHED = null;
    }
}
