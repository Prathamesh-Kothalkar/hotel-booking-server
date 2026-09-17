package dev.prathamesh.ai.service;

import dev.prathamesh.ai.dto.EnhancedChatResponse;


public interface ResponseParser {
    
    boolean canHandle(String response);
    
    
    EnhancedChatResponse parse(String response);
    
   
    default int getPriority() {
        return 0;
    }
}