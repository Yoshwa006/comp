package com.example.comp.repo;

import com.example.comp.model.Question;
import com.example.comp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
}
