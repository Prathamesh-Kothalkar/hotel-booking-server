package dev.prathamesh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.prathamesh.ai.prompt.PromptBuilder;
import dev.prathamesh.ai.tools.BookingTools;
import dev.prathamesh.ai.tools.RoomTools;

@Configuration
public class ChatClientConfig {

	@Bean
	public ChatClient chatClient(GoogleGenAiChatModel chatModel, BookingTools bookingTools,
	                              RoomTools roomTools, ChatMemory chatMemory) {
	    return ChatClient.builder(chatModel)
	            .defaultSystem(PromptBuilder.hotelBookingAgentPrompt())
	            .defaultTools(bookingTools, roomTools)
	            .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
	            .build();
	}
}