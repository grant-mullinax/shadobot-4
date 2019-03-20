package app.commands

import app.commands.Abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.event.message.MessageCreateEvent

class Filter: MessageProcess {
    private val nRegex: Regex = Regex("n*.gg*.r", RegexOption.IGNORE_CASE)

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return nRegex.matches(event.messageContent)
    }

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        event.message.delete()
    }
}