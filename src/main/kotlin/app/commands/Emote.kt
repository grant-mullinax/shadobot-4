package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Emote : StandardCommand() {
    override val commandName = "e"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val emoji = event.message.customEmojis[0]
        event.channel.sendMessage(emoji.image.url.toString())
    }
}