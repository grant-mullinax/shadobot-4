package app.commands.abstract

import org.javacord.api.event.message.MessageCreateEvent

abstract class StandardCommand : MessageProcess {
    val prefix = "!"
    abstract val commandName: String

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageAuthor.isUser && event.messageContent.startsWith(prefix + commandName, ignoreCase = true)
    }
}