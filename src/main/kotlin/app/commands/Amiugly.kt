package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Amiugly: StandardCommand() {
    override val commandName = "amiugly"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val idMod = event.messageAuthor.id % 100
        val uglyScore = minOf(100 - idMod, idMod)/5f
        event.channel.sendMessage("Your prettiness score is $uglyScore")
    }
}