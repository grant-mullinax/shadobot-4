package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Avatar : StandardCommand() {
    override val commandName = "avatar"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        event.channel.sendMessage("${parser.extractMentionedUser(true).avatar.url}?size=512")
    }
}