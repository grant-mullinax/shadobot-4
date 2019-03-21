package app

import app.commands.*
import app.commands.Vote.Vote
import org.javacord.api.DiscordApiBuilder
import java.io.File

fun main() {
    val token = File("token.txt").readText(Charsets.UTF_8)

    val api = DiscordApiBuilder().setToken(token).login().join()

    val cBot = CommunistBot(api)
    cBot.addProccess(Filter())
    cBot.addProccess(Dad())

    cBot.addProccess(Ugly())
    cBot.addProccess(BiggestLove())
    cBot.addProccess(Broadcast(api))
    cBot.addProccess(Love())
    cBot.addProccess(RussianRoulette())
    cBot.addProccess(Pickle())
    cBot.addProccess(Slay())
    cBot.addProccess(Wapoosh())
    cBot.addProccess(Isanyonethere())
    cBot.addProccess(Whenisay(cBot))
    cBot.addProccess(RoleInfo())

    val voteProcess = Vote()
    api.addReactionAddListener(voteProcess::receiveVoteReaction)
    cBot.addProccess(voteProcess)

    api.addMessageCreateListener(cBot::receiveMessage)
    api.addUserRoleAddListener(cBot::mikeyRoleHack)

    println("---------- BOT ONLINE ----------")

    var file = File("path.txt")
    file.createNewFile()

    api.getChannelById(547303725706903552).get().asTextChannel().get().getMessages(999999999).join().forEach { s ->
        if (!s.content.isBlank())
            file.appendText(s.content + "\n")
    }
}