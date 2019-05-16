package app.commands

import app.commands.abstract.MessageProcess
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Hi : MessageProcess {
    private val greetings = setOf("hi", "hey", "hello")

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return greetings.contains(event.messageContent.toLowerCase()) && !event.messageAuthor.isYourself
    }

    override fun action(event: MessageCreateEvent) {
        event.channel.sendMessage(event.messageContent)
    }
}
