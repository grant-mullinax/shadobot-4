package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Isanyonethere: StandardCommand() {
    override val commandName = "test"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        event.channel.sendMessage("what up brooooo")
    }
}