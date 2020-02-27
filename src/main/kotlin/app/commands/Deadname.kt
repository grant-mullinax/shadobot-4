package app.commands

import app.commands.abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Deadname : MessageProcess {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.toLowerCase().contains("brandon")
    }

    override fun action(event: MessageCreateEvent) {
        event.messageAuthor.asUser().get().sendMessage("bruh")
    }
}