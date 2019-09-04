package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.user.UserStatus
import org.javacord.api.event.message.MessageCreateEvent

class StatusReport : StandardCommand() {
    override val commandName = "statusreport"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        val role = parser.extractRoleFromString()
        val usersWithRole = role.users.filter { it.getRoles(role.server).contains(role) }
        val onlineCount = usersWithRole.count { it.status == UserStatus.ONLINE || it.status == UserStatus.DO_NOT_DISTURB }
        val busyCount = usersWithRole.count { it.status == UserStatus.IDLE }

        event.channel.sendMessage("${role.name} users are:\n" +
                "$onlineCount/${usersWithRole.size} online or do not disturb\n" +
                "$busyCount/${usersWithRole.size} idle"
        )
    }
}