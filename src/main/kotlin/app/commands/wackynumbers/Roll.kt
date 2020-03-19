package app.commands.wackynumbers

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.random.Random

class Roll : StandardCommand() {
    override val commandName = "Roll"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val max = parser.extractInt("max roll", 100)

        event.channel.sendMessage((Random.nextInt(max) + 1).toString())
    }
}