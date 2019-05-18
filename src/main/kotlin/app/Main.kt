package app

import app.commands.*
import app.commands.Pokemon.PokeScraper
import app.commands.Pokemon.PokecordCheater
import app.commands.doge.AddDoge
import app.commands.doge.ListDoge
import app.commands.doge.RandomDoge
import app.commands.image.*
import app.commands.trash.Wapoosh2
import app.commands.trash.Whenisay
import app.commands.vote.Vote
import app.commands.wackynumbers.*
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import org.javacord.api.AccountType
import org.javacord.api.DiscordApiBuilder
import org.javacord.core.util.FileContainer
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

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
            Fry()
            // PokecordCheater()
            // PokeScraper()
    ).forEach(cBot::addProccess)

    val dogeProcess = AddDoge(api)
    api.addReactionAddListener(dogeProcess::receiveVoteReaction)
    cBot.addProccess(dogeProcess)
    cBot.addProccess(ListDoge())

    val voteProcess = Vote(api)
    api.addReactionAddListener(voteProcess::receiveVoteReaction)
    cBot.addProccess(voteProcess)

    api.addMessageCreateListener(cBot::receiveMessage)
    api.addUserRoleAddListener(cBot::mikeyRoleHack)

    api.addServerMemberJoinListener(cBot::userJoining)

    println("---------- BOT ONLINE ----------")

    cBot.gatherRoles()
/*
    val msg = api.getMessageById(
        578041747221905419,
        api.getServerTextChannelById(577958960422715402).get()
    ).get()

    msg.channel.sendMessage("/info flabebe")

    print("hey")
    val mapConverter = object: Converter {
        override fun fromJson(jv: JsonValue): HashMap<String, Any?> = HashMap(jv.obj!!)
        override fun canConvert(cls: Class<*>): Boolean {return true }
        override fun toJson(value: Any): String {return ""}
    }

    val pokemonMap: HashMap<String, String> = Klaxon()
        .converter(mapConverter)
        .parse(File("pokemon.json").readText(Charset.defaultCharset()))!!

    val data = FileContainer(msg.embeds.first().image.get().url).asByteArray(api).join()
    val md = MessageDigest.getInstance("MD5")
    val hash = BigInteger(1, md.digest(data)).toString(16).padStart(32, '0')
    msg.channel.sendMessage("pokemon is ${pokemonMap[hash]}")
    */
}