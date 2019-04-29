package app.commands

import app.commands.abstract.MessageProcess
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Dad : MessageProcess {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith("im ", ignoreCase = true) ||
               event.messageContent.startsWith("i'm ", ignoreCase = true) ||
               event.messageContent.startsWith("i’m ", ignoreCase = true)// ||
    }

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val longName = parser.extractMultiSpaceString("dad name hehe secret!!!")
        val newName = longName.substring(0, minOf(longName.length, 32))

        event.messageAuthor.asUser().get().updateNickname(event.server.get(), newName)

        event.message.addReaction("\uD83D\uDC68")
    }
}
