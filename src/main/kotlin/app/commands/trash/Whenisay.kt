package app.commands.trash

import app.CommunistBot
import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Whenisay(private val cBot: CommunistBot) : StandardCommand() {
    override val commandName = "whenisay"

    override fun action(event: MessageCreateEvent) {
        val delimiter = " usay "
        val trigger = event.messageContent.substringAfter("$prefix$commandName ").substringBefore(delimiter)
        val response = event.messageContent.substringAfter(delimiter)
        cBot.processes.add(WhenisayResponse(trigger, response))
    }
}