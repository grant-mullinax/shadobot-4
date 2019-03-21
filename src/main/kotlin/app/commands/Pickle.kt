package app.commands

import app.commands.Abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Pickle: StandardCommand() {
    override val commandName = "pickle"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        event.channel.sendMessage(
            "${event.messageAuthor.asUser().get().name}'s pickle size is " +
                "${(((event.message.author.id % 100) / 100f) * 12).format(2)} inches"
        )
    }
}