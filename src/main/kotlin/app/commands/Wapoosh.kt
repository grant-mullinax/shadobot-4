package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Wapoosh: StandardCommand() {
    override val commandName = "wapoosh"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)

        val str = "*${parser.getAuthorAsUser().name} and ${parser.extractMentionedUser().name} wapoosh*"

        var combo = 1
        var r = 0
        while (r != 4) {
            event.channel.sendMessage("$str x$combo")
            r = (1..4).random()
            combo++
        }
    }
}