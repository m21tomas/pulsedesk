package com.example.pulsedesk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AiTriageResponse {

	@JsonProperty("createTicket") 
	private Boolean createTicket = false;
    private String title;
    private String category;
    private String priority;
    private String summary;
    
    public AiTriageResponse() {}
    
	public AiTriageResponse(boolean createTicket, String title, String category, String priority,
			String summary) {
		this.createTicket = createTicket;
		this.title = title;
		this.category = category;
		this.priority = priority;
		this.summary = summary;
	}

	public Boolean getCreateTicket() {
        return createTicket != null ? createTicket : false;
    }

    public void setCreateTicket(Boolean createTicket) {
        this.createTicket = createTicket;
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
}
