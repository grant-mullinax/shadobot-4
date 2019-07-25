package app.commands.trash

import app.commands.abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class WhenisayResponse(private val trigger: String, private val reply: String) : MessageProcess {

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageAuthor.isUser &&
                !event.messageAuthor.isYourself &&
                event.messageContent.toLowerCase() == trigger.toLowerCase()
    }

    override fun action(event: MessageCreateEvent) {
        event.channel.sendMessage(reply)
    }
}
