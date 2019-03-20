package app

import app.commands.*
import org.javacord.api.DiscordApiBuilder
import java.io.File

fun main() {
    // Insert your bot's token here
    val token = File("token.txt").readText(Charsets.UTF_8)

    val api = DiscordApiBuilder().setToken(token).login().join()

    val cBot = CommunistBot(api)
    cBot.addProccess(Filter())
    cBot.addProccess(Dad())

    cBot.addProccess(Amiugly())
    cBot.addProccess(Aretheyugly())
    cBot.addProccess(BiggestLove())
    cBot.addProccess(Broadcast(api))
    cBot.addProccess(Love())
    cBot.addProccess(Pickle())
    cBot.addProccess(Slay())
    cBot.addProccess(Wapoosh())
    cBot.addProccess(Isanyonethere())

    val voteProcess = Vote()
    api.addReactionAddListener(voteProcess::receiveVoteReaction)
    cBot.addProccess(voteProcess)

    api.addMessageCreateListener(cBot::receiveMessage)
    api.addUserRoleAddListener(cBot::mikeyRoleHack)

    println("---------- BOT ONLINE ----------")
}