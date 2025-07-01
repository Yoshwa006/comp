package com.example.comp.repo;

import com.example.comp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Long> {
     Users findByEmail(String username);
}
