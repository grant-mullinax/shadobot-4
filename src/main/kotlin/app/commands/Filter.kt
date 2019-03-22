package app.commands

import app.commands.Abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Filter: MessageProcess {
    private val nRegex: Regex = Regex("(^|[^a-z])ni+[^ ]{0,4}g+e*[^ ]{0,4}r( |\$)", RegexOption.IGNORE_CASE)

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return nRegex.containsMatchIn(event.messageContent)
    }

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        event.message.delete()
        val role = event.server.get().roles.find { r -> r.name == "n-word sayer" }
        event.messageAuthor.asUser().get().addRole(role)
    }
}