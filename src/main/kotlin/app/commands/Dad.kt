package app.commands

import app.commands.Abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Dad: MessageProcess {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        /*return event.messageContent.startsWith("im ", ignoreCase = true) ||
                event.messageContent.startsWith("i'm ", ignoreCase = true) ||
                event.messageContent.startsWith("i’m ", ignoreCase = true)*/
        return event.messageContent.startsWith("i am", ignoreCase = true)
    }

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val splitMsg = event.message.content.split(" ")
        val longName = splitMsg.subList(2, splitMsg.size).reduce { a, b -> "$a $b" }
        val newName = longName.substring(0, minOf(longName.length, 32))

        event.messageAuthor.asUser().get().updateNickname(event.server.get(), newName)

        event.message.addReaction("\uD83D\uDC68")
    }
}