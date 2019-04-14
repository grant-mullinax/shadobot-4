package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Listbans: StandardCommand() {
    override val commandName = "aretheyugly"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        var s = "*Users Banned:*\n"
        event.server.get().bans.thenAcceptAsync { bs ->
            bs.forEach { b ->
                s += "${b.user.name} - ${b.user.id}\n"
            }
            event.channel.sendMessage(s)
        }
    }
}