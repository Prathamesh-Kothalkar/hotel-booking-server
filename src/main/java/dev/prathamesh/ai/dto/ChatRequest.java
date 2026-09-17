package dev.prathamesh.ai.dto;


public record ChatRequest(
    String message,
    String sessionId
) {
    
    public ChatRequest {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Session ID cannot be null or empty");
        }
    }
}