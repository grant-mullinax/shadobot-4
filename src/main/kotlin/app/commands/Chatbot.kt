package app.commands

import app.commands.Abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Chatbot: MessageProcess {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith("cb")
    }

    override fun action(event: MessageCreateEvent, api: DiscordApi) {

    }
}