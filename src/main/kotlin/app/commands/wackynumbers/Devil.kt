package app.commands.wackynumbers

import app.commands.abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Devil : StandardCommand() {
    override val commandName = "devil"

    override fun action(event: MessageCreateEvent) {
        val mentionedSomeone = event.message.mentionedUsers.isEmpty()
        val id = if (mentionedSomeone) event.messageAuthor.id else event.message.mentionedUsers[0].id
        val targetName = if (mentionedSomeone) "You are" else "${event.message.mentionedUsers[0].name} is"

        event.channel.sendMessage(
                ":fire: :japanese_goblin: $targetName ${((((id + 9966) % 10000) / 100f)).format(2)}% devil :japanese_goblin: :fire:")
    }
}