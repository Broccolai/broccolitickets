package broccolai.tickets.core.inject.platform;

import broccolai.tickets.api.model.event.Subscriber;
import broccolai.tickets.core.commands.command.BaseCommand;
import broccolai.tickets.core.commands.command.TicketCommand;
import broccolai.tickets.core.subscribers.NotificationSubscriber;
import broccolai.tickets.core.subscribers.SoulSubscriber;
import broccolai.tickets.core.utilities.ArrayHelper;

public interface PluginPlatform {
    Class<? extends BaseCommand>[] COMMANDS = ArrayHelper.create(
            TicketCommand.class
    );

    Class<? extends Subscriber>[] SUBSCRIBERS = ArrayHelper.create(
            NotificationSubscriber.class,
            SoulSubscriber.class
    );

    ClassLoader loader();
}