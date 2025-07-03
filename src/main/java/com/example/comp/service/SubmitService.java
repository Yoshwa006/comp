package com.example.comp.service;

import com.example.comp.dto.JudgeResponse;
import com.example.comp.dto.Mapper;
import com.example.comp.dto.SubmitAPI;
import com.example.comp.dto.SubmitRequest;
import com.example.comp.model.Session;
import com.example.comp.repo.SessionRepo;
import com.example.comp.repo.UserRepo;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class SubmitService {

    private final JwtService jwtService;
    private final WebClient client;
    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;

    public SubmitService(SessionRepo sessionRepo, JwtService jwtService , UserRepo userRepo) {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:3001")
                .build();
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public boolean submitCode(SubmitRequest request) {
        SubmitAPI api = Mapper.SubmitRequestToAPI(request);

        String email = jwtService.extractUsername(request.getJwtToken());
        int userId  = userRepo.findByEmail(email).getId();
        if(userId == 0){
            log.error("User not found for email: {}", email);
            return false;
        }
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
                Session session = sessionRepo.findByToken(request.getToken());
                if (session == null) {
                    log.error("Session not found for token: {}", request.getToken());
                    return false;
                }

                session.setWho_won(userId);
                sessionRepo.save(session);
                log.info("Code accepted. Output: {}", result.getStdout());
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
