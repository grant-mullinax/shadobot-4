package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Avatar: StandardCommand() {
    override val commandName = "avatar"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val target = event.message.mentionedUsers[0]
        event.channel.sendMessage("${target.avatar.url}?size=512")
    }
}