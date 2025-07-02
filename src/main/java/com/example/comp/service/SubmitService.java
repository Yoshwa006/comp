package com.example.comp.service;

import com.example.comp.dto.JudgeResponse;
import com.example.comp.dto.SubmitAPI;
import com.example.comp.dto.SubmitRequest;
import com.example.comp.model.Session;
import com.example.comp.repo.SessionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class SubmitService {

    private final WebClient client;
    private final SessionRepo sessionRepo;

    public SubmitService(SessionRepo sessionRepo) {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:3001")
                .build();
        this.sessionRepo = sessionRepo;
    }

    public boolean submitCode(SubmitRequest request) {
        SubmitAPI api = new SubmitAPI();
        api.setLanguage_id(request.getLanguage_id());
        api.setStdin(request.getStdin());
        api.setSource_code(request.getSource_code());

        try {
            JudgeResponse result = client.post()
                    .uri("/run-code")
                    .bodyValue(api)
                    .retrieve()
                    .bodyToMono(JudgeResponse.class)
                    .block();

            if (result == null) {
                log.error("Judge0 response was null");
                return false;
            }

            log.info("Judge0 Result: {}", result);

            if ("Accepted".equalsIgnoreCase(result.getStatus())) {
                Session session = sessionRepo.findByUserId((long) request.getUserId());
                if (session == null) {
                    log.error("No session found for user ID: {}", request.getUserId());
                    return false;
                }

                session.setWho_won(request.getUserId());
                sessionRepo.save(session);

                log.info("Winner marked: user ID {}", request.getUserId());
                return true;
            } else {
                log.info("Code not accepted. Status: {}", result.getStatus());
                return false;
            }

        } catch (Exception e) {
            log.error("Error during code submission", e);
            return false;
        }
    }
}
