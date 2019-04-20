package app

import app.commands.*
import app.commands.doge.AddDoge
import app.commands.doge.ListDoge
import app.commands.doge.RandomDoge
import app.commands.image.*
import app.commands.trash.HKCommand
import app.commands.trash.HKCommand2
import app.commands.trash.Wapoosh2
import app.commands.trash.Whenisay
import app.commands.vote.Vote
import app.commands.wackynumbers.*
import org.javacord.api.DiscordApiBuilder

fun main() {
    val api = DiscordApiBuilder().setToken(Keys.discord).login().join()

    val cBot = CommunistBot(api)
    cBot.addProccess(Filter())
    cBot.addProccess(Dad())

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
            HKCommand(),
            HKCommand2(),
            Resize(),
            Avatar(),
            Ascii(),
            Img(),
            AniDrip(),
            Drip(),
            Blur(),
            Version(),
            Oblivion(),
            RotateColor(),
            Rotate(),
            Edges(),
            Emote(),
            Fry()
    ).forEach(cBot::addProccess)

    val dogeProcess = AddDoge()
    api.addReactionAddListener(dogeProcess::receiveVoteReaction)
    cBot.addProccess(dogeProcess)
    cBot.addProccess(ListDoge())

    val voteProcess = Vote()
    api.addReactionAddListener(voteProcess::receiveVoteReaction)
    cBot.addProccess(voteProcess)

    api.addMessageCreateListener(cBot::receiveMessage)
    api.addUserRoleAddListener(cBot::mikeyRoleHack)

    api.addServerMemberJoinListener(cBot::userJoining)

    println("---------- BOT ONLINE ----------")

    cBot.gatherRoles()
}