package app

import app.commands.Abstract.MessageProcess
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.channel.VoiceChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.permission.PermissionType
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import org.javacord.api.event.server.member.ServerMemberEvent
import org.javacord.api.event.server.member.ServerMemberJoinEvent
import org.javacord.api.event.server.member.ServerMemberLeaveEvent
import org.javacord.api.event.server.role.UserRoleAddEvent
import org.javacord.api.event.server.role.UserRoleRemoveEvent
import kotlin.math.abs
import kotlin.random.Random

class CommunistBot(private val api: DiscordApi) {
    private val processes = mutableListOf<MessageProcess>()

    private val roles = mutableMapOf<Long, MutableList<Long>>()

    fun receiveMessage(event: MessageCreateEvent) {
        if (!event.messageAuthor.isUser)
            return

        for (process in processes) {
            if (process.qualifier(event)) {
                try {
                    process.action(event, api)
                } catch (ex: ParserFailureException) {
                    event.channel.sendMessage(ex.message)
                }
                return
            }
        }
    }

    fun addProccess(process: MessageProcess){
        processes.add(process)
    }

    fun receiveUserDeparture(event: ServerMemberJoinEvent) {
        if (event.server.id != 544737205315305481) {
            return
        }

        event.user.sendMessage("Hate to see you leave! Please consider filling out this survey o3o\n" +
        "https://docs.google.com/forms/d/e/1FAIpQLSfBmiwfGZURv5KSTlYy6FHYztB_68gdCsUXMTNagEFC8HY9_Q/viewform\n\n" +
        "https://discord.gg/XSt5pV4")
    }

    fun mikeyRoleHack(event: UserRoleAddEvent) {
        if (event.server.id != 544737205315305481) {
            return
        }

        gatherRolesForUser(event.user, event.server)

        // m, f, nb
        if (!arrayOf(547918362794131467, 547918360516755459, 547918349506707456).contains(event.role.id)) {
            return
        }

        val rank = event.server.getRoleById(544742523319353364).get()
        event.user.addRole(rank)
    }

    fun roleRemoved(event: UserRoleRemoveEvent) {
        gatherRolesForUser(event.user, event.server)
    }

    fun userLeaving(event: ServerMemberEvent) {
        println("bye")
    }

    fun userJoining(event: ServerMemberJoinEvent) {
        roles[event.user.id]?.forEach {
                id ->
            event.user.addRole(api.getRoleById(id).get())
        }
    }

    fun gatherRoles() {
        val server = api.getServerById(544737205315305481).get()
        server.members.forEach { m: User ->
            gatherRolesForUser(m, server)
        }
    }

    private fun gatherRolesForUser(user: User, server: Server) {
        roles[user.id] = mutableListOf()
        server.getRoles(user).forEach { r ->
            roles[user.id]!!.add(r.id)
        }
    }
}