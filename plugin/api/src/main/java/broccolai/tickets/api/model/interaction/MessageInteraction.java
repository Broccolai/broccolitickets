package broccolai.tickets.api.model.interaction;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class MessageInteraction extends Interaction {

    private final String message;

    public MessageInteraction(
            final @NonNull Action action,
            final @NonNull LocalDateTime time,
            final @NonNull UUID sender,
            final @NonNull String message
    ) {
        super(action, time, sender);
        this.message = message;
    }

    public final @NonNull String message() {
        return this.message;
    }

}
