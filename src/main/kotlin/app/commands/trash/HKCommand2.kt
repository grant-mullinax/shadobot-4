package app.commands.trash

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class HKCommand2 : StandardCommand() {
    override val commandName = "giveall"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        if (!(event.messageAuthor.asUser().get().getRoles(event.server.get()).contains(api.getRoleById(550546244788027412).get()) ||
                        event.messageAuthor.asUser().get().getRoles(event.server.get()).contains(api.getRoleById(549840418024587265).get()))) {
            return
        }
        val splitMsg = event.message.content.split(" ")
        val rankName = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }
        val rank = event.server.get().roles.find { r -> r.name == rankName }!!

        event.server.get().members.forEach { m -> m.addRole(rank) }
    }
}