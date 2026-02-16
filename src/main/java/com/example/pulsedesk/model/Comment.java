package com.example.pulsedesk.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createdAt;
	
	private boolean convertedToTicket;
	
	@OneToOne(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
	private Ticket ticket;
	
	public Comment() {}
	
	public Comment(String content, LocalDateTime createdAt) {
        this.content = content;
        this.createdAt = createdAt;
        this.convertedToTicket = false;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isConvertedToTicket() {
		return convertedToTicket;
	}

	public void setConvertedToTicket(boolean convertedToTicket) {
		this.convertedToTicket = convertedToTicket;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
}
