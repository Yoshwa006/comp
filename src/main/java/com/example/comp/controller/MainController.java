package com.example.comp.controller;

import com.example.comp.dto.EnterToken;
import com.example.comp.dto.GenerateToken;
import com.example.comp.dto.SubmitRequest;
import com.example.comp.service.MainService;
import com.example.comp.service.SubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    private final MainService mainService;
    private final SubmitService submitService;


    @Autowired
    public MainController(MainService mainService, SubmitService submitService) {
        this.mainService = mainService;
        this.submitService = submitService;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody GenerateToken generateToken) {
        System.out.println("JWt: " + generateToken.getJwt());
        return ResponseEntity.ok().body(mainService.generateToken(generateToken.getJwt(),generateToken.getQuestionId()));
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enter(@RequestBody EnterToken em) {
        mainService.enterToken(em.getJwt(), em.getToken());
        return ResponseEntity.ok().body("Done");
    }

    @GetMapping
    public ResponseEntity<?> getAllQuestions() {
        return ResponseEntity.ok(mainService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(mainService.getQuestionById(id));
    }

    @PostMapping("/submit")
    public ResponseEntity<Boolean> createQuestion(@RequestBody SubmitRequest request) {
        return ResponseEntity.ok().body(submitService.submitCode(request));
    }

}
