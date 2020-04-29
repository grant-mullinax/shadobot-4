package app.commands

import app.commands.abstract.AdminCommand
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.util.*

class Spell : AdminCommand() {
    override val commandName = "spell"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val word = parser.extractString("Word")
        word.forEach { event.channel.sendMessage(it.toString()) }
    }
}