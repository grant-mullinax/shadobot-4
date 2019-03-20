package app

import app.commands.Abstract.MessageProcess
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.channel.VoiceChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.permission.PermissionType
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import org.javacord.api.event.server.role.UserRoleAddEvent
import kotlin.math.abs
import kotlin.random.Random

class CommunistBot(private val api: DiscordApi) {
    private val processes = mutableListOf<MessageProcess>()

    fun receiveMessage(event: MessageCreateEvent) {
        for (process in processes) {
            if (process.qualifier(event)) {
                process.action(event, api)
                return
            }
        }
    }

    fun addProccess(process: MessageProcess){
        processes.add(process)
    }

    /*fun receiveUserDeparture(event: ServerMemberJoinEvent) {
        if (event.server.id != 544737205315305481) {
            return
        }

        event.user.sendMessage("Hate to see you leave! Please consider filling out this survey o3o\n" +
        "https://docs.google.com/forms/d/e/1FAIpQLSfBmiwfGZURv5KSTlYy6FHYztB_68gdCsUXMTNagEFC8HY9_Q/viewform\n\n" +
        "https://discord.gg/XSt5pV4")
    }*/

    fun mikeyRoleHack(event: UserRoleAddEvent) {
        if (event.server.id != 544737205315305481) {
            return
        }

        if (!arrayOf(547918360516755459, 547918362794131467, 557369792236093440).contains(event.role.id)) {
            return
        }

        val rank = event.server.getRoleById(544742523319353364).get()
        event.user.addRole(rank)
    }
}