package broccolai.tickets.core.ticket;

import cloud.commandframework.arguments.flags.FlagContext;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * enum representing the potential states of a ticket
 */
public enum TicketStatus {
    OPEN(TextColor.color(85, 255, 85)),
    PICKED(TextColor.color(255, 255, 85)),
    CLOSED(TextColor.color(255, 85, 85));

    private final TextColor color;

    TicketStatus(final TextColor color) {
        this.color = color;
    }

    /**
     * Retrieve the color associated with this status
     *
     * @return the color
     */
    public TextColor getColor() {
        return color;
    }

    /**
     * Retrieve a ticket status with a name
     *
     * @param input the potential statuses name
     * @return the constructed status, or if not found null
     */
    public static @Nullable TicketStatus from(final @NonNull String input) {
        for (TicketStatus value : values()) {
            if (value.name().equalsIgnoreCase(input)) {
                return value;
            }
        }

        return null;
    }

    /**
     * Retrieve an array of statuses from a flag context
     *
     * @param flags the flag context to retrieve from
     * @return array of ticket statuses
     */
    public static @NonNull TicketStatus[] fromFlags(final @NonNull FlagContext flags) {
        TicketStatus status = flags.getValue("status", null);

        return status != null ? of(status) : of(TicketStatus.OPEN, TicketStatus.PICKED);
    }

    /**
     * Retrieve an array of statuses from a vararg input
     *
     * @param values vararg ticket statuses to generate from
     * @return array of ticket statuses
     */
    public static @NonNull TicketStatus[] of(final @NonNull TicketStatus... values) {
        return values;
    }
}
