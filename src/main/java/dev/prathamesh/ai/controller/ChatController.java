package dev.prathamesh.ai.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.ai.dto.ChatRequest;
import dev.prathamesh.ai.dto.ChatResponse;
import dev.prathamesh.expection.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/chat") 
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request,Authentication auth) {
    	System.out.println("Hitted");
    	Long userId = (Long) auth.getPrincipal();
    	if(request.message()==null) {
    		throw new ResourceNotFoundException("Message can't be null");
    	}
        String reply = chatClient.prompt()
                .user(request.message())
                .system(s -> s.param("userId", userId))
                .call()
                .content();

        return new ChatResponse(reply);
    }
    
    @GetMapping
    public String check() {
    	return "Hello from AI";
    }
}