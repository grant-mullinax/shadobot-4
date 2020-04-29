package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class RoleInfo : StandardCommand() {
    override val commandName = "roleinfo"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        val role = parser.extractRoleFromString()

        event.channel.sendMessage("${role.name} -\n" +
                "id: ${role.id}\n" +
                "perms: ${role.permissions.allowedBitmask}\n" +
                "color: ${role.color.orElse(null)}")
    }
}