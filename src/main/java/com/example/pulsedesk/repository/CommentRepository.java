package com.example.pulsedesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pulsedesk.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c LEFT JOIN FETCH c.ticket")
    List<Comment> findAllWithTickets();
}
