package broccolai.tickets.locale;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing locale messages.
 */
@SuppressWarnings("unused")
public enum Messages implements MessageKeyProvider {
    // GENERAL
    GENERAL__LIST_FORMAT, GENERAL__LIST_HEADER_FORMAT, GENERAL__LOG_FORMAT, GENERAL__HS_FORMAT,
    // SENDER
    SENDER__NEW_TICKET, SENDER__UPDATE_TICKET, SENDER__CLOSE_TICKET, SENDER__PICK_TICKET, SENDER__YIELD_TICKET,
    SENDER__ASSIGN_TICKET, SENDER__DONE_TICKET, SENDER__REOPEN_TICKET, SENDER__NOTE_TICKET, SENDER__TELEPORT_TICKET,
    // NOTIFICATION
    NOTIFICATION__PICK_TICKET, NOTIFICATION__YIELD_TICKET, NOTIFICATION__ASSIGN_TICKET, NOTIFICATION__DONE_TICKET,
    NOTIFICATION__REOPEN_TICKET, NOTIFICATION__NOTE_TICKET,
    // ANNOUNCEMENT
    ANNOUNCEMENT__NEW_TICKET, ANNOUNCEMENT__UPDATE_TICKET, ANNOUNCEMENT__CLOSE_TICKET, ANNOUNCEMENT__PICK_TICKET,
    ANNOUNCEMENT__YIELD_TICKET, ANNOUNCEMENT__ASSIGN_TICKET, ANNOUNCEMENT__DONE_TICKET, ANNOUNCEMENT__REOPEN_TICKET,
    ANNOUNCEMENT__NOTE_TICKET,
    // DISCORD
    DISCORD__NEW_TICKET, DISCORD__UPDATE_TICKET, DISCORD__CLOSE_TICKET, DISCORD__PICK_TICKET, DISCORD__YIELD_TICKET,
    DISCORD__ASSIGN_TICKET, DISCORD__DONE_TICKET, DISCORD__REOPEN_TICKET, DISCORD__NOTE_TICKET,
    // TITLES
    TITLES__SPECIFIC_TICKETS, TITLES__YOUR_TICKETS, TITLES__ALL_TICKETS, TITLES__SPECIFIC_STATUS, TITLES__TICKET_STATUS,
    TITLES__SHOW_TICKET, TITLES__TICKET_LOG, TITLES__HIGHSCORES,
    // SHOW NAMES
    SHOW__SENDER, SHOW__PICKER, SHOW__UNPICKED, SHOW__MESSAGE, SHOW__LOCATION,
    // EXCEPTIONS
    EXCEPTIONS__TICKET_NOT_FOUND, EXCEPTIONS__INVALID_SETTING_TYPE, EXCEPTIONS__TOO_MANY_OPEN_TICKETS,
    EXCEPTIONS__TICKET_CLOSED, EXCEPTIONS__TICKET_OPEN,
    // OTHER
    OTHER__REMINDER, OTHER__SETTING_UPDATE;

    @Override
    public MessageKey getMessageKey() {
        return MessageKey.of(name().toLowerCase().replace("__", "."));
    }

    /**
     * Retrieve a Message using a target type and a message name.
     *
     * @param targetType   the target type
     * @param messageNames the message name
     * @return a message
     */
    @NotNull
    public static Messages retrieve(TargetType targetType, MessageNames messageNames) {
        return valueOf(targetType.name() + "__" + messageNames.name());
    }
}