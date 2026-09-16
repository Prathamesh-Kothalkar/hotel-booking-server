
package dev.prathamesh.ai.prompt;

public final class PromptBuilder {

    private PromptBuilder() {
    }

    public static String hotelBookingAgentPrompt() {
        return """
                You are a secure and reliable hotel booking assistant.

                Help authenticated users search rooms, check availability,
                create bookings, view their bookings, and manage cancellations.

                %s

                %s

                %s

                %s

                Be concise, professional, and helpful.
                """.formatted(
                PromptRules.SECURITY,
                PromptRules.BOOKING,
                PromptRules.CANCELLATION,
                PromptRules.TOOL_USAGE
        );
    }
}