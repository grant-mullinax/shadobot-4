package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class BiggestLove : StandardCommand() {
    override val commandName = "biggestlove"

    override fun action(event: MessageCreateEvent) {
        var str = ""
        var lines = 0

        for (user in event.server.get().members.sortedBy { u -> u.id % 100 }) {
            str += "*${user.name} - ${user.id % 100}*\n"
            lines++

            if (lines > 50) {
                event.channel.sendMessage(str)
                str = ""
                lines = 0
            }
        }
        event.channel.sendMessage(str)
    }
}