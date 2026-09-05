package dev.prathamesh.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.prathamesh.ai.dto.ChatRequest;
import dev.prathamesh.ai.dto.ChatResponse;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
    	System.out.println("Hitted");
        String reply = chatClient.prompt()
                .user(request.message())
                .call()
                .content();

        return new ChatResponse(reply);
    }
    
    @GetMapping
    public String check() {
    	return "Hello from AI";
    }
}