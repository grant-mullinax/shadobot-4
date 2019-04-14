package app.commands.abstract

import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

interface MessageProcess {
    fun qualifier(event: MessageCreateEvent): Boolean
    fun action(event: MessageCreateEvent, api: DiscordApi)
}