package dev.prathamesh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.prathamesh.ai.tools.BookingTools;
import dev.prathamesh.ai.tools.RoomTools;

@Configuration
public class ChatClientConfig {

	@Bean
	public ChatClient chatClient(GoogleGenAiChatModel chatModel, BookingTools bookingTools,
	                              RoomTools roomTools, ChatMemory chatMemory) {
	    return ChatClient.builder(chatModel)
	            .defaultSystem("""
	                    You are a hotel booking customer support assistant.
	                    Use the available tools to answer questions about bookings and room availability.
	                    Before cancelling a booking, always call requestCancelBooking first to show the
	                    user the details, then only call confirmCancelBooking once the user has clearly said yes.
	                    Be concise and friendly.
	                    """)
	            .defaultTools(bookingTools, roomTools)
	            .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
	            .build();
	}
}