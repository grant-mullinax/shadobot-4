package app

import app.commands.*
import app.commands.trash.Avalon
import app.commands.Pokemon.PokecordCheater
import app.commands.doge.AddDoge
import app.commands.doge.ListDoge
import app.commands.doge.RandomDoge
import app.commands.image.*
import app.commands.trash.Avalon2
import app.commands.trash.Wapoosh2
import app.commands.trash.Whenisay
import app.commands.vote.Vote
import app.commands.wackynumbers.*
import org.javacord.api.DiscordApiBuilder

fun main() {
    val api = DiscordApiBuilder().setToken(Keys.discord).login().join()

    val cBot = CommunistBot(api)
    cBot.processes.add(Filter())
    cBot.processes.add(Dad())

    listOf(
        Ugly(),
        BiggestLove(),
        Broadcast(api),
        Love(),
        RussianRoulette(),
        Pickle(),
        Slay(),
        Wapoosh2(),
        Wapoosh(),
        Isanyonethere(),
        Whenisay(cBot),
        RoleInfo(),
        RandomDoge(),
        Speak(),
        Devil(),
        Angel(),
        Alignment(),
        Bars(),
        Resize(),
        Avatar(),
        Ascii(),
        Img(),
        AniDrip(),
        Drip(),
        Meme(),
        Hi(),
        Blur(),
        Version(),
        Oblivion(),
        RotateColor(),
        Rotate(),
        Edges(),
        Emote(),
        Fry(),
        PokecordCheater(),
        Friends(),
        Avalon2(),
        StatusReport()
    ).forEach(cBot::addProccess)

    val dogeProcess = AddDoge(api)
    api.addReactionAddListener(dogeProcess::receiveVoteReaction)
    cBot.processes.add(dogeProcess)
    cBot.processes.add(ListDoge())

    val voteProcess = Vote(api)
    api.addReactionAddListener(voteProcess::receiveVoteReaction)
    cBot.processes.add(voteProcess)

    api.addMessageCreateListener(cBot::receiveMessage)
    api.addUserRoleAddListener(cBot::mikeyRoleHack)

    api.addServerMemberJoinListener(cBot::userJoining)

    println("---------- BOT ONLINE ----------")

    cBot.gatherRoles()
}