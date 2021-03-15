package broccolai.tickets.core.service.message;

import broccolai.tickets.api.model.ticket.Ticket;
import broccolai.tickets.api.service.message.MessageService;
import broccolai.tickets.core.configuration.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

@Singleton
public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;

    @Inject
    public MiniMessageService(final @NonNull LocaleConfiguration locale) {
        this.locale = locale;
    }

    @Override
    public Component senderTicketCreation(final @NonNull Ticket ticket) {
        return this.locale.sender.create.use(ticket);
    }

    @Override
    public Component senderTicketUpdate(@NonNull final Ticket ticket) {
        return this.locale.sender.update.use(ticket);
    }

    @Override
    public Component senderTicketClose(@NonNull final Ticket ticket) {
        return this.locale.sender.close.use(ticket);
    }

    @Override
    public Component senderTicketClaim(final @NonNull Ticket ticket) {
        return this.locale.sender.claim.use(ticket);
    }

    @Override
    public Component senderTicketComplete(@NonNull final Ticket ticket) {
        return this.locale.sender.complete.use(ticket);
    }

    @Override
    public Component targetTicketClaim(final @NonNull Ticket ticket) {
        return this.locale.notify.claim.use(ticket);
    }

    @Override
    public Component targetTicketComplete(@NonNull final Ticket ticket) {
        return this.locale.notify.complete.use(ticket);
    }

    @Override
    public Component staffTicketClaim(final @NonNull Ticket ticket) {
        return this.locale.announcement.claim.use(ticket);
    }

    @Override
    public Component staffTicketComplete(@NonNull final Ticket ticket) {
        return this.locale.announcement.complete.use(ticket);
    }

    @Override
    public Component staffTicketCreate(@NonNull final Ticket ticket) {
        return this.locale.announcement.create.use(ticket);
    }

    @Override
    public Component staffTicketUpdate(@NonNull final Ticket ticket) {
        return this.locale.announcement.update.use(ticket);
    }

    @Override
    public Component staffTicketClose(@NonNull final Ticket ticket) {
        return this.locale.announcement.close.use(ticket);
    }

    @Override
    public Component commandsTicketList(final @NonNull Collection<@NonNull Ticket> tickets) {
        TextComponent.Builder builder = Component.text()
                .append(this.locale.title.yourTickets.use());

        Template wrapper = Template.of("wrapper", this.locale.title.wrapper.use());

        tickets.forEach(ticket -> {
            Component list = this.locale.format.list.use(wrapper, ticket);
            builder.append(Component.newline(), list);
        });

        return builder.build();
    }

    @Override
    public Component taskReminder(final int count) {
        return this.locale.format.reminder.use(Template.of("amount", String.valueOf(count)));
    }

}
