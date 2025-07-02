package com.example.comp.repo;

import com.example.comp.model.Session;
import com.example.comp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepo extends JpaRepository<Session, Integer> {
    Session findByToken(String token);
    @Query("SELECT s FROM Session s WHERE s.createdBy = :userId OR s.joinedBy = :userId")
    Session findByUserId(@Param("userId") Long userId);
}
