package dev.prathamesh.ai.guardrail;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class PendingConfirmationStore {

    public record PendingAction(Long userId, Long bookingId, Instant expiresAt) {}

    private final Map<String, PendingAction> pending = new ConcurrentHashMap<>();
    private static final long TTL_SECONDS = 120;

    public String create(Long userId, Long bookingId) {
        String token = UUID.randomUUID().toString();
        pending.put(token, new PendingAction(userId, bookingId, Instant.now().plusSeconds(TTL_SECONDS)));
        return token;
    }

    public Long consume(String token, Long requestingUserId) {
        PendingAction action = pending.remove(token); // one-time use — remove on read
        if (action == null || action.expiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("This confirmation has expired or was already used. Please try again.");
        }
        if (!action.userId().equals(requestingUserId)) {
            throw new org.springframework.security.access.AccessDeniedException("Confirmation does not belong to you");
        }
        return action.bookingId();
    }
}