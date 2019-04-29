package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.event.message.MessageCreateEvent

class GiveAll : StandardCommand() {
    override val commandName = "giveall"

    override fun action(event: MessageCreateEvent) {
        // todo add permissioned commands

        val splitMsg = event.message.content.split(" ")
        val rankName = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }
        val rank = event.server.get().roles.find { r -> r.name == rankName }!!

        event.server.get().members.forEach { m -> m.addRole(rank) }
    }
}