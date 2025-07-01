package com.example.comp.controller;


import com.example.comp.dto.EmailAndId;
import com.example.comp.dto.EmailAndToken;
import com.example.comp.dto.EmailAnsQues;
import com.example.comp.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }


    @PostMapping("/generate")
    public String generate(@RequestBody EmailAnsQues em) {
        return mainService.generateToken(em.getEmail(), em.getQuesId());
    }

    @PostMapping("/enter")
    public String enter(@RequestBody EmailAndToken em) {
        mainService.enterToken(em.getEmail(), em.getToken());
        return "Success";
    }
}
