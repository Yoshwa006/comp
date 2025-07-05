package com.example.comp.service;


import com.example.comp.dto.auth.AuthRequest;
import com.example.comp.dto.auth.AuthResponce;
import com.example.comp.dto.Mapper;
import com.example.comp.model.Users;
import com.example.comp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    UserRepo repo;
    JwtService jwtService;
    @Autowired
    public AuthService(UserRepo repo, JwtService jwtService) {
        this.jwtService = jwtService;
        this.repo = repo;
    }

    public boolean isEmailExists(String email) {
        return repo.findByEmail(email) != null;
    }


    public void register(AuthRequest authDTO) {
        if (isEmailExists(authDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Users newUser = new Users();
        newUser = Mapper.DTOtoUser(authDTO);
        repo.save(newUser);
    }


    public AuthResponce login(AuthRequest authDTO) {
        Users users = repo.findByEmail(authDTO.getEmail());
        if(users == null){
            throw new RuntimeException("Email don't exist");
        }


        String token = jwtService.generateToken(users.getEmail());
        System.out.println(token);
        AuthResponce responce = new AuthResponce();
        responce.setToken(token);
        return responce;
    }
}
