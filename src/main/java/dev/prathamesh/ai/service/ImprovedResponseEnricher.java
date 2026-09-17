package dev.prathamesh.ai.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import dev.prathamesh.ai.dto.EnhancedChatResponse;

/**
 * Improved ResponseEnricher using strategy pattern
 * Delegates parsing to specialized parsers based on response type
 */
@Service
public class ImprovedResponseEnricher {

    private final List<ResponseParser> parsers;

    public ImprovedResponseEnricher(SearchResultsResponseParser searchParser,
                                     BookingConfirmationResponseParser bookingParser,
                                     QuestionResponseParser questionParser) {
        this.parsers = new ArrayList<>();
        this.parsers.add(bookingParser);        // Priority 110
        this.parsers.add(searchParser);          // Priority 100
        this.parsers.add(questionParser);        // Priority 50
        
        // Sort by priority (descending)
        this.parsers.sort(Comparator.comparingInt(ResponseParser::getPriority).reversed());
    }

    /**
     * Enrich a plain text response with structured data
     * Delegates to the first parser that can handle the response
     */
    public EnhancedChatResponse enrich(String textResponse) {
        // Try each parser in priority order
        for (ResponseParser parser : parsers) {
            if (parser.canHandle(textResponse)) {
                return parser.parse(textResponse);
            }
        }

        // Fallback: return as plain text
        EnhancedChatResponse response = new EnhancedChatResponse(textResponse);
        response.setResponseType("TEXT");
        return response;
    }
}