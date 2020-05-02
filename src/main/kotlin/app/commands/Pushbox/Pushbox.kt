package app.commands.Pushbox

import app.commands.abstract.AdminCommand
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.event.message.MessageCreateEvent

class Pushbox : StandardCommand() {
    override val commandName = "blindfold"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val name = parser.extractString("role name")

        val server = event.server.get()
        val role = server.createRoleBuilder().setName(name).setMentionable(true).create().get()

        server.members.forEach { it.addRole(role) }
    }
}