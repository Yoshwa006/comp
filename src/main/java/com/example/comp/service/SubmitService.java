package com.example.comp.service;

import com.example.comp.dto.SubmitRequest;
import com.example.comp.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SubmitService {

    private final WebClient client;

    public SubmitService() {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:3001")
                .build();
    }

    public boolean submitCode(SubmitRequest request) {
        try {
            // Step 1: POST /run-code
            TokenResponse tokenResponse = client.post()
                    .uri("/run-code")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block(); // wait for response

            if (tokenResponse == null || tokenResponse.getToken() == null) {
                System.err.println("Token response is null.");
                return false;
            }

            String token = tokenResponse.getToken();
            System.out.println("Token: " + token);

            // Step 2: GET /token/{token}
            String result = client.get()
                    .uri("/token/" + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // wait for response

            System.out.println("Judge0 Result: " + result);

            // ✅ Return true if result is "Accepted"
            return result.contains("Accepted");

        } catch (Exception e) {
            System.err.println("Error during submission: " + e.getMessage());
            return false;
        }
    }
}