package app.commands.wackynumbers

import app.commands.abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Angel : StandardCommand() {
    override val commandName = "angel"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val mentionedSomeone = event.message.mentionedUsers.isEmpty()
        val id = if (mentionedSomeone) event.messageAuthor.id else event.message.mentionedUsers[0].id
        val targetName = if (mentionedSomeone) "You are" else "${event.message.mentionedUsers[0].name} is"

        event.channel.sendMessage(
                ":sparkler: :angel: $targetName ${((((id + 95189) % 100000) / 1000f)).format(2)}% angel :angel: :sparkler:")
    }
}