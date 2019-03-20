package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Aretheyugly: StandardCommand() {
    override val commandName = "aretheyugly"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val user = event.message.mentionedUsers[0]
        val idMod = event.message.mentionedUsers[0].id % 100
        val uglyScore = minOf(100 - idMod, idMod)/5f
        event.channel.sendMessage("${user.name}'s prettiness score is $uglyScore")
    }
}