package dev.prathamesh.ai.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.*;

import dev.prathamesh.ai.dto.ChatRequest;
import dev.prathamesh.ai.dto.ChatResponse;
import dev.prathamesh.ai.dto.EnhancedChatResponse;
import dev.prathamesh.ai.service.ImprovedResponseEnricher;



@RestController
@RequestMapping("/api/v1/chat")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {

    private final ChatClient chatClient;
    private final ImprovedResponseEnricher responseEnricher;

    public ChatController(ChatClient chatClient, ImprovedResponseEnricher responseEnricher) {
        this.chatClient = chatClient;
        this.responseEnricher = responseEnricher;
    }

   
    @PostMapping
    public EnhancedChatResponse chat(
            @RequestBody ChatRequest request, 
            Authentication auth) {
        
      
        Long userId = (Long) auth.getPrincipal();
        String conversationId = "user-" + userId + "-" + request.sessionId();

        try {
           
            String textReply = chatClient.prompt()
                    .user(request.message())
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .call()
                    .content();

           
            EnhancedChatResponse enrichedResponse = responseEnricher.enrich(textReply);

            return enrichedResponse;
            
        } catch (Exception e) {
            EnhancedChatResponse errorResponse = new EnhancedChatResponse(
                "Sorry, I encountered an error processing your request. Please try again."
            );
            errorResponse.setResponseType("ERROR");
            errorResponse.setError(e.getMessage());
            return errorResponse;
        }
    }

   
    @GetMapping
    public String healthCheck() {
        return "Hotel Booking AI Assistant is running";
    }

    
    @PostMapping("/text-only")
    public ChatResponse chatTextOnly(
            @RequestBody ChatRequest request, 
            Authentication auth) {
        
        Long userId = (Long) auth.getPrincipal();
        String conversationId = "user-" + userId + "-" + request.sessionId();

        String reply = chatClient.prompt()
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();

        return new ChatResponse(reply);
    }
}