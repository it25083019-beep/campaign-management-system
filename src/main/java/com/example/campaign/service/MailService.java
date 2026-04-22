package com.example.campaign.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

    @Value("${RESEND_API_KEY}")
    private String resendApiKey;

    @Value("${RESEND_FROM_EMAIL}")
    private String fromEmail;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendWinnerMail(String to, String name) throws Exception {
        Map<String, Object> body = Map.of(
                "from", fromEmail,
                "to", List.of(to),
                "subject", "TEST MAIL SPRING BOOT - CAMPAIGN",
                "html", "<strong>" + name + " 様</strong><br><br>これはテストメールです。<br>キャンペーン当選のお知らせです。",
                "text", name + " 様\n\nこれはテストメールです。\nキャンペーン当選のお知らせです。"
        );

        String json = objectMapper.writeValueAsString(body);

        String safeKey = to.replace("@", "_at_").replace(".", "_");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.resend.com/emails"))
                .header("Authorization", "Bearer " + resendApiKey)
                .header("Content-Type", "application/json")
                .header("Idempotency-Key", "winner-" + safeKey)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Resend API failed: " + response.statusCode() + " / " + response.body());
        }

        System.out.println("Resend success: " + response.body());
    }
}