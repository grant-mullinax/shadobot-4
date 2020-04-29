package app.commands.abstract

import org.javacord.api.event.message.MessageCreateEvent

abstract class OwnerCommand : StandardCommand() {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        if (!super.qualifier(event)) {
            return false
        }

        if (event.messageAuthor.id == 155061315977740288) {
            return true
        }

        event.channel.sendMessage("only my master can do that")
        return false
    }
}