package app.commands.trash

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Wapoosh2 : StandardCommand() {
    override val commandName = "wapoosh2"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        var combo = -1
        var r = 0
        while (r != 1) {
            r = (1..100).random()
            combo++
        }
        event.channel.sendMessage("*${event.messageAuthor.asUser().get().name} and ${event.message.mentionedUsers[0].name} wapoosh x$combo*")
    }
}