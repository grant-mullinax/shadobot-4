package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Version : StandardCommand() {
    override val commandName = "version"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        event.channel.sendMessage("Built on: 04/24 19:36:34")
    }
}