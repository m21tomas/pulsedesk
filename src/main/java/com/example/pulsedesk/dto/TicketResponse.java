package com.example.pulsedesk.dto;

public class TicketResponse {
	private Long id;
	
	private String title;
	
	private String category;
	
	private String priority;
	
	private String summary;
	
	private String createdAt;
	
	public TicketResponse() {}

	public TicketResponse(Long id, String title, String category, String priority, String summary, 
			              String createdAt) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.priority = priority;
		this.summary = summary;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
