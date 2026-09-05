package dev.prathamesh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.prathamesh.ai.tools.BookingTools;
import dev.prathamesh.ai.tools.RoomTools;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(GoogleGenAiChatModel chatModel, BookingTools bookingTools, RoomTools roomTools) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                        You are a hotel booking customer support assistant.
                        Use the available tools to answer questions about bookings.
                        Be concise and friendly.
                        """)
                .defaultTools(bookingTools,roomTools)
                .build();
    }
}