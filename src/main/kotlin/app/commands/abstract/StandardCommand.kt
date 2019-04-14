package app.commands.abstract

import org.javacord.api.event.message.MessageCreateEvent

abstract class StandardCommand: MessageProcess {
    private val prefix = "!"
    abstract val commandName: String

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith(prefix + commandName, ignoreCase = true)
    }
}