package app.commands.abstract

import org.javacord.api.event.message.MessageCreateEvent

abstract class StandardCommand : MessageProcess {
    val prefix = "$"
    open val helpString: String? = null
    abstract val commandName: String

    override fun qualifier(event: MessageCreateEvent): Boolean {
        if (event.messageContent.split(" ").first() == "!$commandName") {
            event.channel.sendMessage("try \$help $commandName idiot")
        }

        return event.messageAuthor.isUser &&
                !event.messageAuthor.isYourself &&
                event.messageContent.split(" ").first() == prefix + commandName
    }
}