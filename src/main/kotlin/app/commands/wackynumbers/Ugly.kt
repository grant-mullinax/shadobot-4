package app.commands.wackynumbers

import app.commands.abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Ugly : StandardCommand() {
    override val commandName = "uglycheck"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val mentionedSomeone = event.message.mentionedUsers.isEmpty()
        val idMod = (if (mentionedSomeone) event.messageAuthor.id else event.message.mentionedUsers[0].id) % 100
        val uglyScore = minOf(100 - idMod, idMod) / 5f
        val targetName = if (mentionedSomeone) "Your" else "${event.message.mentionedUsers[0].name}'s"

        event.channel.sendMessage("$targetName prettiness score is ${uglyScore.format(2)}")
    }
}