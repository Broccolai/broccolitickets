package broccolai.tickets.core.subscribers;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.api.model.event.TicketEvent;
import broccolai.tickets.api.model.event.impl.TicketCloseEvent;
import broccolai.tickets.api.model.event.impl.TicketCompleteEvent;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.service.event.EventService;
import broccolai.tickets.api.service.storage.StorageService;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

@Singleton
public final class RewardSubscriber implements Subscriber {

    private final StorageService storageService;

    public RewardSubscriber(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void register(@NonNull final EventService eventService) {
        eventService.register(TicketCloseEvent.class, this::onTicketClose);
        eventService.register(TicketCompleteEvent.class, this::onTicketComplete);
    }

    private void onTicketClose(final @NonNull TicketCloseEvent event) {
        this.process(event);
    }

    private void onTicketComplete(final @NonNull TicketCompleteEvent event) {
        this.process(event);
    }

    private void process(final @NonNull TicketEvent event) {
        Optional<UUID> potentialUUID = event.ticket().claimer();

        if (potentialUUID.isEmpty()) {
            return;
        }

        UUID uuid = potentialUUID.get();
        this.storageService.highscore(uuid, EnumSet.of(TicketStatus.CLOSED));
    }

}
