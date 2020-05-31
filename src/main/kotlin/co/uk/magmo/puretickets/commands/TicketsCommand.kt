package co.uk.magmo.puretickets.commands

import co.aikar.commands.annotation.*
import co.uk.magmo.puretickets.interactions.NotificationManager
import co.uk.magmo.puretickets.locale.Messages
import co.uk.magmo.puretickets.storage.SQLFunctions
import co.uk.magmo.puretickets.tasks.TaskManager
import co.uk.magmo.puretickets.ticket.MessageReason
import co.uk.magmo.puretickets.ticket.TicketManager
import co.uk.magmo.puretickets.ticket.TicketStatus
import co.uk.magmo.puretickets.utils.*
import com.okkero.skedule.SynchronizationContext
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("tickets|tis")
@CommandPermission(Constants.STAFF_PERMISSION)
class TicketsCommand : PureBaseCommand() {
    @Subcommand("%show")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".show")
    @Description("Show a ticket")
    @Syntax("<Player> [Index]")
    fun onShow(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        processShowCommand(sender, information)
    }

    @Subcommand("%pick")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".pick")
    @Description("Pick a ticket")
    @Syntax("<Player> [Index]")
    fun onPick(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.pick(sender.asUUID(), information)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TICKET__PICKED, *replacements)
            notificationManager.send(information.player, Messages.NOTIFICATIONS__PICK, "%user%", sender.name, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__PICKED_TICKET, "%user%", sender.name, *replacements)
        }
    }

    @Subcommand("%assign")
    @CommandCompletion("@Players @AllTicketHolders @UserTicketIdsWithTarget")
    @CommandPermission(Constants.STAFF_PERMISSION + ".assign")
    @Description("Assign a ticket to a staff member")
    @Syntax("<TargetPlayer> <Player> [Index]")
    fun onAssign(sender: CommandSender, target: OfflinePlayer, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.pick(target.uniqueId, information)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TICKET__ASSIGN, "%target%", target.name!!, *replacements)
            notificationManager.send(target.uniqueId, Messages.NOTIFICATIONS__ASSIGN, "%user%", sender.name, *replacements)
            notificationManager.send(information.player, Messages.NOTIFICATIONS__PICK, "%user%", target.name!!, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__ASSIGN_TICKET, "%user%", sender.name, "%target%", target.name!!, *replacements)
        }
    }

    @Subcommand("%done")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".done")
    @Description("Done-mark a ticket")
    @Syntax("<Player> [Index]")
    fun onDone(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.done(sender.asUUID(), information)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TICKET__DONE, *replacements)
            notificationManager.send(information.player, Messages.NOTIFICATIONS__DONE, "%user%", sender.name, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__DONE_TICKET, "%user%", sender.name, *replacements)
        }
    }

    @Subcommand("%yield")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".yield")
    @Description("Yield a ticket")
    @Syntax("<Player> [Index]")
    fun onYield(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.yield(sender.asUUID(), information)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TICKET__YIELDED, *replacements)
            notificationManager.send(information.player, Messages.NOTIFICATIONS__YIELD, "%user%", sender.name, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__YIELDED_TICKET, "%user%", sender.name, *replacements)
        }
    }

    @Subcommand("%note")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".note")
    @Description("Make a note on a ticket")
    @Syntax("<Player> [Index]")
    fun onNote(sender: CommandSender, offlinePlayer: OfflinePlayer, index: Int, message: String) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager.note(sender.asUUID(), information, message)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TICKET__NOTE, *replacements)
            notificationManager.send(information.player, Messages.NOTIFICATIONS__NOTE, "%user%", sender.name, "%note%", message, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__NOTE_TICKET, "%user%", sender.name, "%note%", message, *replacements)
        }
    }

    @Subcommand("%reopen")
    @CommandCompletion("@UserOfflineNames @UserOfflineTicketIDs")
    @CommandPermission(Constants.STAFF_PERMISSION + ".reopen")
    @Description("Reopen a ticket")
    @Syntax("<Player> [Index]")
    fun onReopen(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, true)

        taskManager {
            val ticket = ticketManager.reopen(sender.asUUID(), information)
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TICKET__REOPENED, *replacements)
            notificationManager.send(information.player, Messages.NOTIFICATIONS__REOPEN, "%user%", sender.name, *replacements)
            notificationManager.announce(Messages.ANNOUNCEMENTS__REOPEN_TICKET, "%user%", sender.name, *replacements)
        }
    }

    @Subcommand("%teleport")
    @CommandCompletion("@AllTicketHolders @UserTicketIdsWithPlayer")
    @CommandPermission(Constants.STAFF_PERMISSION + ".teleport")
    @Description("Teleport to a ticket creation location")
    @Syntax("<Player> [Index]")
    fun onTeleport(player: Player, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, false)

        taskManager {
            val ticket = ticketManager[offlinePlayer.uniqueId, information.index]
            val replacements = Utils.ticketReplacements(ticket)
            val location = ticket?.location

            switchContext(SynchronizationContext.SYNC)

            if (location == null) {
                notificationManager.reply(player, Messages.TICKET__TELEPORT_ERROR, *replacements)
            } else {
                notificationManager.reply(player, Messages.TICKET__TELEPORT, *replacements)
                player.teleport(ticket.location)
            }
        }
    }

    @Subcommand("%log")
    @CommandCompletion("@AllTicketHolders @UserTicketIds")
    @CommandPermission(Constants.STAFF_PERMISSION + ".log")
    @Description("Log tickets messages")
    @Syntax("<Player> [Index]")
    fun onLog(sender: CommandSender, offlinePlayer: OfflinePlayer, @Optional index: Int?) {
        val information = generateInformation(offlinePlayer, index, true)

        taskManager {
            val ticket = ticketManager[information.player, information.index] ?: return@taskManager
            val replacements = Utils.ticketReplacements(ticket)

            switchContext(SynchronizationContext.SYNC)

            notificationManager.reply(sender, Messages.TITLES__TICKET_LOG, *replacements)

            ticket.messages.forEach {
                sender.sendMessage("§f§l" + it.reason.name + " §8@ §f" + it.date?.formatted() + "§8 - §f" + (it.data
                        ?: it.sender.asName()))
            }
        }
    }

    @Subcommand("%list")
    @CommandCompletion("@UserNames @TicketStatus")
    @CommandPermission(Constants.STAFF_PERMISSION + ".list")
    @Description("List all tickets")
    @Syntax("[Player]")
    fun onList(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?, @Optional status: TicketStatus?) {
        if (sender.hasPermission(Constants.STAFF_PERMISSION + ".list")) {
            if (offlinePlayer != null) {
                taskManager {
                    var tickets = SQLFunctions.retrieveClosedTickets(offlinePlayer.uniqueId)
                    if (status != null) tickets = tickets.filter { ticket -> ticket.status == status }

                    switchContext(SynchronizationContext.SYNC)

                    notificationManager.reply(sender, Messages.TITLES__SPECIFIC_TICKETS, "%player%", offlinePlayer.name!!)

                    tickets.forEach { ticket ->
                        val replacements = Utils.ticketReplacements(ticket)

                        notificationManager.reply(sender, Messages.FORMAT__LIST_ITEM, *replacements)
                    }
                }
            } else {
                notificationManager.reply(sender, Messages.TITLES__ALL_TICKETS)

                ticketManager.asMap().forEach { (uuid, tickets) ->
                    sender.sendMessage(ChatColor.GREEN.toString() + uuid.asName())

                    tickets.forEach { ticket ->
                        val replacements = Utils.ticketReplacements(ticket)

                        notificationManager.reply(sender, Messages.FORMAT__LIST_ITEM, *replacements)
                    }
                }
            }
        } else if (sender.hasPermission(Constants.USER_PERMISSION + ".list")) {
            ticketManager[sender.asUUID()].forEach {
                val replacements = Utils.ticketReplacements(it)

                notificationManager.reply(sender, Messages.FORMAT__LIST_ITEM, *replacements)
            }
        }
    }

    @Subcommand("%status")
    @CommandCompletion("@UserNames")
    @CommandPermission(Constants.STAFF_PERMISSION + ".status")
    @Description("View amount of tickets in")
    @Syntax("[Player]")
    fun onStatus(sender: CommandSender, @Optional offlinePlayer: OfflinePlayer?) {
        taskManager {
            val data = if (offlinePlayer != null) {
                notificationManager.reply(sender, Messages.TITLES__SPECIFIC_STATUS, "%player%", offlinePlayer.name!!)

                switchContext(SynchronizationContext.SYNC)

                SQLFunctions.selectCurrentTickets(offlinePlayer.uniqueId)
            } else {
                notificationManager.reply(sender, Messages.TITLES__TICKET_STATUS)

                switchContext(SynchronizationContext.SYNC)

                SQLFunctions.selectCurrentTickets(null)
            }

            data.forEach { (status, amount) ->
                if (amount != 0) sender.sendMessage(amount.toString() + " " + status.name.toLowerCase())
            }
        }
    }
}