package app.commands

import app.commands.abstract.AdminCommand
import app.commands.abstract.OwnerCommand
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.util.*

class Whereami(private val api: DiscordApi) : OwnerCommand() {
    override val commandName = "whereami"

    override fun action(event: MessageCreateEvent) {
        event.channel.sendMessage(api.servers.map { "${it.name} - ${it.id}" }.reduce { a, b -> "$a\n$b"})
    }
}