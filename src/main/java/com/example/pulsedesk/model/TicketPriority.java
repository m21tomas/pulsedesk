package com.example.pulsedesk.model;

public enum TicketPriority {
	LOW, MEDIUM, HIGH;

    public static TicketPriority fromString(String value) {
        if (value == null) return LOW;
        try {
            return TicketPriority.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            String upperValue = value.toUpperCase();
            if (upperValue.contains("URGENT") || upperValue.contains("CRITICAL")) {
                return HIGH;
            }
            return MEDIUM;
        }
    }
}
