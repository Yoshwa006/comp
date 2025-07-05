package com.example.comp.service;

import com.example.comp.model.Question;
import com.example.comp.model.Session;
import com.example.comp.repo.QuestionRepo;
import com.example.comp.repo.SessionRepo;
import com.example.comp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MainService {

    private final QuestionRepo questionRepo;
    final SessionRepo sessionRepo;
    final UserRepo userRepo;
    private final JwtService jwtService;

    @Autowired
    MainService(QuestionRepo questionRepo, SessionRepo sessionRepo, UserRepo userRepo, JwtService jwtService) {
        this.sessionRepo = sessionRepo;
        this.questionRepo = questionRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    }

    public Question createQuestion(Question question) {
        return questionRepo.save(question);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = getQuestionById(id);
        question.setTitle(questionDetails.getTitle());
        question.setDifficulty(questionDetails.getDifficulty());
        question.setDescription(questionDetails.getDescription());
        return questionRepo.save(question);
    }

    public String generateToken(String JWT, int quesId) {
        System.out.println(JWT);
        Random random = new Random();
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            if (random.nextBoolean()) {
                token.append(random.nextInt(10));
            } else {
                char ch = (char)(random.nextInt(26) + 'A');
                token.append(ch);
            }
        }
        String email = jwtService.extractUsername(JWT);
        Optional<Long> optionalUserId = userRepo.findIdByEmail(email);
        if (optionalUserId.isEmpty()) {
            return token.toString(); // or throw exception
        }
        int userId = Math.toIntExact(optionalUserId.get());

        Session session = new Session();
        session.setToken(token.toString());
        session.setCreatedBy(userId);
        session.setQuestion_id(quesId);
        sessionRepo.save(session);
        System.out.println(session);
        return token.toString();
    }

    public void enterToken(String JWT, String token) {

        String email = jwtService.extractUsername(JWT);
        Optional<Long> optionalUserId = userRepo.findIdByEmail(email);
        if (optionalUserId.isEmpty()) {
            throw new RuntimeException("Email not found");
        }
        int userId = Math.toIntExact(optionalUserId.get());
        Session session = sessionRepo.findByToken(token);
        if(session.getCreatedBy() !=-1 && session.getJoinedBy() != -1) {
            throw new RuntimeException("Session already full");
        }
        if (session == null) {
            throw new RuntimeException("Invalid token");
        }
        session.setJoinedBy(userId);
        sessionRepo.save(session);
    }

    public static void main(String[] args) {

    }
}
