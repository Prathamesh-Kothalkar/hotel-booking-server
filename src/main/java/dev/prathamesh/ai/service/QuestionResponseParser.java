package dev.prathamesh.ai.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import dev.prathamesh.ai.dto.EnhancedChatResponse;

/**
 * Parser for question/prompt responses
 * Detects when the assistant is asking a question and provides quick actions
 */
@Component
public class QuestionResponseParser implements ResponseParser {

    private static final int PRIORITY = 50;

    @Override
    public boolean canHandle(String response) {
        // Check if response contains question mark and is reasonably short
        return response.contains("?") && response.length() < 300;
    }

    @Override
    public EnhancedChatResponse parse(String response) {
        EnhancedChatResponse result = new EnhancedChatResponse(response);
        result.setResponseType("QUESTION");
        result.setSuggestedActions(generateContextualActions(response));
        return result;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    /**
     * Generate contextual action suggestions based on the question
     */
    private List<String> generateContextualActions(String response) {
        List<String> actions = new ArrayList<>();
        String lower = response.toLowerCase();

        // Check for guest-related questions
        if (lower.contains("guest") || lower.contains("how many people")) {
            // Provide number options for guests
            // Frontend will handle rendering guest count buttons
            actions.add("1 guest");
            actions.add("2 guests");
            actions.add("3 guests");
            actions.add("4+ guests");
        }
        // Check for date-related questions
        else if (lower.contains("date") || lower.contains("when") || lower.contains("check")) {
            actions.add("Tomorrow");
            actions.add("This weekend");
            actions.add("Next week");
            actions.add("Specific date");
        }
        // Check for location-related questions
        else if (lower.contains("location") || lower.contains("where") || lower.contains("city")) {
            actions.add("Pune");
            actions.add("Mumbai");
            actions.add("Bangalore");
            actions.add("Other location");
        }
        // Check for price-related questions
        else if (lower.contains("price") || lower.contains("budget") || lower.contains("cost")) {
            actions.add("Budget (₹0-₹2,000)");
            actions.add("Standard (₹2,000-₹5,000)");
            actions.add("Premium (₹5,000-₹10,000)");
            actions.add("Luxury (₹10,000+)");
        }
        // Check for room type questions
        else if (lower.contains("room") || lower.contains("type") || lower.contains("deluxe")) {
            actions.add("Standard Room");
            actions.add("Deluxe Room");
            actions.add("Super Deluxe");
            actions.add("Suite");
        }
        // Generic fallback
        else {
            actions.add("Yes");
            actions.add("No");
            actions.add("Tell me more");
        }

        return actions;
    }
}