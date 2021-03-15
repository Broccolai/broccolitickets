package broccolai.tickets.core.service.ticket;

import broccolai.tickets.api.model.interaction.MessageInteraction;
import broccolai.tickets.api.model.position.Position;
import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.model.ticket.TicketStatus;
import broccolai.tickets.api.model.user.Soul;
import broccolai.tickets.api.service.storage.StorageService;
import broccolai.tickets.api.service.ticket.TicketService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

@Singleton
public final class CachedTicketService implements TicketService {

    private final StorageService storageService;

    private final Cache<Integer, Ticket> cache = Caffeine.newBuilder().build();
    private final Multimap<UUID, TicketStatus> lookups = MultimapBuilder.hashKeys().enumSetValues(TicketStatus.class).build();

    @Inject
    public CachedTicketService(final @NonNull StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public @NonNull Ticket create(
            final @NonNull Soul soul,
            final @NonNull Position position,
            final @NonNull MessageInteraction interaction
    ) {
        int id = this.storageService.create(soul, position, interaction);
        return new Ticket(id, soul.uuid(), position, TicketStatus.OPEN, interaction, null);
    }

    @Override
    public @NonNull Optional<@NonNull Ticket> get(final int id) {
        return Optional.ofNullable(Iterables.getFirst(this.get(Collections.singletonList(id)), null));
    }

    @Override
    public @NonNull Collection<@NonNull Ticket> get(final @NonNull Collection<Integer> queries) {
        Collection<Integer> ids = new HashSet<>(queries);
        Map<Integer, Ticket> tickets = new HashMap<>(this.cache.getAllPresent(ids));
        ids.removeAll(tickets.keySet());

        if (ids.size() != 0) {
            Map<Integer, Ticket> uncached = this.storageService.tickets(ids);
            this.cache.putAll(uncached);
            tickets.putAll(uncached);
        }

        return new ArrayList<>(tickets.values());
    }

    @Override
    public @NonNull Collection<@NonNull Ticket> get(
            final @NonNull Soul soul,
            final @NonNull Set<TicketStatus> queries
    ) {
        Set<TicketStatus> modifiableQueries = new HashSet<>(queries);

        if (this.lookups.containsKey(soul.uuid())) {
            Collection<TicketStatus> loaded = this.lookups.get(soul.uuid());
            modifiableQueries.removeAll(loaded);
        }

        if (!modifiableQueries.isEmpty()) {
            this.cache.putAll(this.storageService.tickets(soul, modifiableQueries));
            this.lookups.get(soul.uuid()).addAll(modifiableQueries);
        }

        return this.filter(ticket -> ticket.player().equals(soul.uuid()) && queries.contains(ticket.status()));
    }

    @Override
    public int count(final @NonNull Set<TicketStatus> statuses) {
        return this.storageService.countTickets(statuses);
    }


    private @NonNull Collection<@NonNull Ticket> filter(final @NonNull Predicate<@NonNull Ticket> predicate) {
        Collection<Ticket> tickets = new ArrayList<>();

        for (final Ticket ticket : this.cache.asMap().values()) {
            if (predicate.test(ticket)) {
                tickets.add(ticket);
            }
        }

        return tickets;
    }

}
