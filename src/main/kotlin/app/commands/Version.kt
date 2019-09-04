package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Version : StandardCommand() {
    override val commandName = "version"

    override fun action(event: MessageCreateEvent) {
        event.channel.sendMessage("Built on: 09/04 16:38:37")
    }
}