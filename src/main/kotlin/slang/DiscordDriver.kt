package slang

import app.CommunistBot
import app.Keys
import org.javacord.api.DiscordApiBuilder
import java.io.File

fun main() {
    var file = File("dev")
    val api = DiscordApiBuilder().setToken(if (file.exists()) Keys.discordDev else Keys.discordProd).login().join()

    val cBot = CommunistBot(api)

}