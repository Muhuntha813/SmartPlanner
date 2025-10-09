package com.smartplanner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;

public final class GoogleTasksHttp {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private GoogleTasksHttp() {}

    public static String getDefaultTaskListId() throws Exception {
        String token = GoogleOAuth.getAccessToken();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://tasks.googleapis.com/tasks/v1/users/@me/lists"))
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2) {
            throw new RuntimeException("GET lists failed: " + resp.statusCode() + "\n" + resp.body());
        }

        String json = resp.body();
        int items = json.indexOf("\"items\"");
        if (items < 0) throw new RuntimeException("No task lists found in response: " + json);

        int idKey = json.indexOf("\"id\"", items);
        if (idKey < 0) throw new RuntimeException("No list id found in response: " + json);

        int a = json.indexOf('"', idKey + 4);
        int b = json.indexOf('"', a + 1);
        return json.substring(a + 1, b);
    }

    /** Create a task (due is optional). Returns selfLink or throws with full HTTP error. */
    public static String createTask(String taskListId, String title, String notes, LocalDate dueDate) throws Exception {
        String token = GoogleOAuth.getAccessToken();

        // Always URL-encode path segment
        String encodedListId = java.net.URLEncoder.encode(taskListId, java.nio.charset.StandardCharsets.UTF_8);

        // Build a clean RFC-3339 due timestamp only if provided
        String duePart = "";
        if (dueDate != null) {
            // Convert the local date (no time) to a specific time (e.g. 18:00) in user's zone, then to UTC
            ZonedDateTime z = dueDate.atTime(18, 0)
                    .atZone(java.time.ZoneId.systemDefault())
                    .withZoneSameInstant(java.time.ZoneOffset.UTC);

            // Use ISO_INSTANT -> always RFC-3339 with Z (e.g. 2025-11-02T12:00:00Z)
            String due = java.time.format.DateTimeFormatter.ISO_INSTANT.format(z.toInstant());
            duePart = "\"due\":\"" + due + "\",";
        }

        // Minimal JSON; escape quotes in title/notes; no trailing commas
        String safeTitle = title == null ? "" : title.trim().replace("\"", "\\\"");
        String safeNotes = notes == null ? "" : notes.trim().replace("\"", "\\\"");
        String body = "{"
                + "\"title\":\"" + safeTitle + "\","
                + duePart
                + "\"notes\":\"" + safeNotes + "\""
                + "}";

        // DEBUG: log what we’re sending (helps if it still fails)
        System.out.println("POST /tasks body => " + body);

        var req = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("https://tasks.googleapis.com/tasks/v1/lists/" + encodedListId + "/tasks"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                .build();

        var resp = CLIENT.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2) {
            throw new RuntimeException("POST task failed: " + resp.statusCode() + "\n" + resp.body() + "\n\nBody sent:\n" + body);
        }

        String json = resp.body();
        int i = json.indexOf("\"selfLink\":");
        if (i >= 0) {
            int a = json.indexOf('"', i + 11);
            int b = json.indexOf('"', a + 1);
            return json.substring(a + 1, b);
        }
        return "Task created.";
    }


    private static String q(String s) { return "\"" + s.replace("\"", "\\\"") + "\""; }
}
