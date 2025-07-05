package com.example.comp.service;

import com.example.comp.dto.JudgeResponse;
import com.example.comp.dto.Mapper;
import com.example.comp.dto.SubmitAPI;
import com.example.comp.dto.SubmitRequest;
import com.example.comp.model.Session;
import com.example.comp.model.Users;
import com.example.comp.repo.SessionRepo;
import com.example.comp.repo.UserRepo;
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

    public SubmitService(SessionRepo sessionRepo, JwtService jwtService, UserRepo userRepo) {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:3001")
                .build();
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public boolean submitCode(SubmitRequest request) {
        String token = request.getToken();
        String jwt = request.getJwtToken();

        Session session = sessionRepo.findByToken(token);
        if (session == null) {
            log.error("Session not found for token: {}", token);
            return false;
        }

        if (session.getWho_won() != 0) {
            log.error("Code already submitted by user with id: {}", session.getWho_won());
            return false;
        }

        String email = jwtService.extractUsername(jwt);
        Users user = userRepo.findByEmail(email);
        if (user == null) {
            log.error("User not found for email: {}", email);
            return false;
        }

        int userId = user.getId();

        // Check if user is part of this session
        if (userId != session.getCreatedBy() && userId != session.getJoinedBy()) {
            log.error("User not part of session. Email: {}, userId: {}, sessionToken: {}", email, userId, token);
            return false;
        }

        SubmitAPI codeSubmission = Mapper.SubmitRequestToAPI(request);

        try {
            JudgeResponse judgeResult = client.post()
                    .uri("/run-code")
                    .bodyValue(codeSubmission)
                    .retrieve()
                    .bodyToMono(JudgeResponse.class)
                    .block();

            if (judgeResult == null) {
                log.error("Judge0 response was null");
                return false;
            }

            log.info("Judge0 Result: {}", judgeResult);

            if ("Accepted".equalsIgnoreCase(judgeResult.getStatus())) {
                session.setWho_won(userId);
                sessionRepo.save(session);
                log.info("Code accepted. Output: {}", judgeResult.getStdout());
                return true;
            } else {
                log.info("Code not accepted. Status: {}", judgeResult.getStatus());
                return false;
            }

        } catch (Exception e) {
            log.error("Error during code submission", e);
            return false;
        }
    }
}
