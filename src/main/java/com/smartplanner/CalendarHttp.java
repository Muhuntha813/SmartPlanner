package com.smartplanner;

import com.google.api.client.auth.oauth2.Credential;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;

public final class CalendarHttp {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private CalendarHttp() {}

    /** Creates an event on the user's primary calendar; returns the Google "htmlLink". */
    public static String createEvent(String title, LocalDateTime start, LocalDateTime end) throws Exception {
        Credential cred = GoogleOAuth.getCredential();           // ensures OAuth
        String token = cred.getAccessToken();

        ZoneId zone = ZoneId.systemDefault();
        String s = start.atZone(zone).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String e = end.atZone(zone).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String body = """
        {
          "summary": %s,
          "start": { "dateTime": %s },
          "end":   { "dateTime": %s },
          "reminders": { "useDefault": false, "overrides": [ { "method": "popup", "minutes": 10 } ] }
        }
        """.formatted(q(title), q(s), q(e));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/calendar/v3/calendars/primary/events"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() / 100 != 2) {
            throw new RuntimeException("Calendar API " + resp.statusCode() + ": " + resp.body());
        }
        // quick & simple parse for htmlLink
        String json = resp.body();
        int i = json.indexOf("\"htmlLink\":");
        if (i >= 0) {
            int a = json.indexOf('"', i + 11);
            int b = json.indexOf('"', a + 1);
            return json.substring(a + 1, b);
        }
        return "Event created.";
    }

    private static String q(String s) { return "\"" + s.replace("\"", "\\\"") + "\""; }
}
