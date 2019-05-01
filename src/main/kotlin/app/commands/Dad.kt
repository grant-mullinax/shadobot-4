package app.commands

import app.commands.abstract.MessageProcess
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Dad : MessageProcess {
    private val clanTag = Regex("\\[(.*?)\\]")

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith("im ", ignoreCase = true) ||
               event.messageContent.startsWith("i'm ", ignoreCase = true) ||
               event.messageContent.startsWith("i’m ", ignoreCase = true)
    }

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        try {
            var name = parser.extractMultiSpaceString("dad name hehe secret!!!")
            val tagResult = clanTag.find(event.messageAuthor.displayName)
            if (tagResult != null) name = "${tagResult.value} $name"

            event.messageAuthor.asUser().get().updateNickname(
                event.server.get(),
                name.substring(0, minOf(name.length, 32))
            )

            event.message.addReaction("\uD83D\uDC68")
        } catch (ex: ParserFailureException) {}
    }
}
