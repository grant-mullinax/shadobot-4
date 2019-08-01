package app.commands.trash

import app.commands.abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class CreeperTroll : MessageProcess {
    private val nRegex: Regex = Regex("creeper", RegexOption.IGNORE_CASE)

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return nRegex.containsMatchIn(event.messageContent)
    }

    override fun action(event: MessageCreateEvent) {
        // event.messageAuthor.asUser().get().
    }
}