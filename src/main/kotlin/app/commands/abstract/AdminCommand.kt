package app.commands.abstract

import org.javacord.api.event.message.MessageCreateEvent

abstract class AdminCommand : StandardCommand() {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        if (!super.qualifier(event)) {
            return false
        }

        if (event.messageAuthor.id == 155061315977740288 || event.messageAuthor.isServerAdmin) {
            return true
        }

        event.channel.sendMessage("you dont have permission to do that sneaky")
        return false
    }
}