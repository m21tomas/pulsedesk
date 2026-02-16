package com.example.pulsedesk.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {
	@NotBlank(message = "Comment text cannot be empty")
	private String text;

    public CommentRequest() {}

    public CommentRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
