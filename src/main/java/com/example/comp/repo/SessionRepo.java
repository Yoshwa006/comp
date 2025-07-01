package com.example.comp.repo;

import com.example.comp.model.Session;
import com.example.comp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<Session, Long> {
}
