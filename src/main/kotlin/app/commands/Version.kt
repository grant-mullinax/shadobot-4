package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Version: StandardCommand() {
    override val commandName = "Version"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        event.channel.sendMessage("0412223211")
    }
}