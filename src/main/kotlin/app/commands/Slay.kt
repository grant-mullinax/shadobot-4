package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Slay: StandardCommand() {
    override val commandName = "slay"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        var str = ""
        for (user in event.message.mentionedUsers) {
            str += "*${user.name} has been slain*\n"
        }

        event.channel.sendMessage(str)
    }
}