package com.example.pulsedesk.dto;

public class CommentResponse {
	
	private Long id;
	
	private String content;
	
	private String createdAt;
	
	private String convertedToTicket;
	
	private String ticketId;

	public CommentResponse() {}

	public CommentResponse(Long id, String content, String createdAt, String convertedToTicket, String ticketId) {
		this.id = id;
		this.content = content;
		this.createdAt = createdAt;
		this.convertedToTicket = convertedToTicket;
		this.ticketId = ticketId;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getConvertedToTicket() {
		return convertedToTicket;
	}

	public void setConvertedToTicket(String convertedToTicket) {
		this.convertedToTicket = convertedToTicket;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
}
