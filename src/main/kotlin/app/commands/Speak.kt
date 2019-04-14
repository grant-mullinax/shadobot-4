package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Speak: StandardCommand() {
    override val commandName = "speak"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
    }
}