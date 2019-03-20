package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class BiggestLove: StandardCommand() {
    override val commandName = "biggestlove"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        var str = ""

        for (user in event.server.get().members.sortedBy { u -> u.id % 100}) {
            str += "*${user.name} - ${user.id % 100}*\n"
        }
        event.channel.sendMessage(str)
    }
}