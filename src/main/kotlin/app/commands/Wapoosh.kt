package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Wapoosh: StandardCommand() {
    override val commandName = "wapoosh"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val str = "*${event.messageAuthor.asUser().get().name} and ${event.message.mentionedUsers[0].name} wapoosh*"

        var combo = 1
        var r = (1..4).random()
        while (r != 4) {
            event.channel.sendMessage("$str x$combo")
            r = (1..4).random()
            combo++
        }
    }
}