package app.commands.trash

import app.CommunistBot
import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Whenisay(private val cBot: CommunistBot) : StandardCommand() {
    override val commandName = "whenisay"

    override fun action(event: MessageCreateEvent) {
        val splitMsg = event.message.content.split(" ")
        val response = splitMsg.subList(3, splitMsg.size).reduce { a, b -> "$a $b" }
        cBot.addProccess(WhenisayResponse(splitMsg[1], response))
    }
}