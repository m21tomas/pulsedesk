package com.example.pulsedesk.model;

public enum TicketCategory {
	BUG, FEATURE, BILLING, ACCOUNT, OTHER;
	
	public static TicketCategory fromString(String value) {
	    if (value == null) return OTHER;
	    try {
	        return TicketCategory.valueOf(value.trim().toUpperCase());
	    } catch (IllegalArgumentException e) {
	        return OTHER; 
	    }
	}
}
