package app

import app.commands.abstract.MessageProcess
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.server.member.ServerMemberEvent
import org.javacord.api.event.server.member.ServerMemberJoinEvent
import org.javacord.api.event.server.role.UserRoleAddEvent
import org.javacord.api.event.server.role.UserRoleRemoveEvent

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
                } catch (ex: Exception) {
                    val stackTrace = ex.stackTrace.map { e -> "in ${e.className}/${e.methodName} at line ${e.lineNumber}"}
                        .reduce{a, b -> "$a\n$b"}
                    event.channel.sendMessage("unusual error\n${ex.message}\n$stackTrace")
                }
                return
            }
        }
    }

    fun addProccess(process: MessageProcess){
        processes.add(process)
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