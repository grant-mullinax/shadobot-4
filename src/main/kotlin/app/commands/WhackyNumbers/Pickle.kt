package app.commands.WhackyNumbers

import app.commands.Abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Pickle: StandardCommand() {
    override val commandName = "pickle"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val mentionedSomeone = event.message.mentionedUsers.isEmpty()
        val id = if (mentionedSomeone) event.messageAuthor.id else event.message.mentionedUsers[0].id
        val targetName = if (mentionedSomeone) "Your" else "${event.message.mentionedUsers[0].name}'s"

        event.channel.sendMessage(
            "$targetName pickle size is ${(((id % 100) / 100f) * 12).format(2)} inches"
        )
    }
}