package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class RoleInfo : StandardCommand() {
    override val commandName = "roleinfo"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val splitMsg = event.message.content.split(" ")
        val rankName = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }
        val rank = event.server.get().roles.find { r -> r.name == rankName }!!

        event.channel.sendMessage("$rankName -\n" +
                "id: ${rank.id}\n" +
                "perms: ${rank.permissions.allowedBitmask}\n" +
                "color: ${rank.color.get()}")
    }
}