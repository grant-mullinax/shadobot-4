package app.commands.trash

import app.commands.abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class WhenisayResponse(private val trigger: String, private val reply: String) : MessageProcess {

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith(trigger, ignoreCase = true)
    }

    override fun action(event: MessageCreateEvent) {
        event.channel.sendMessage(reply)
    }
}