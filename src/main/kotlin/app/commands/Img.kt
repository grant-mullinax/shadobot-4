package app.commands

import app.Keys
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import com.beust.klaxon.json
import kotlinx.coroutines.Job
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.image.BufferedImage
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.imageio.IIOException
import javax.imageio.ImageIO


class Img : StandardCommand() {
    override val commandName = "img"

    override fun action(event: MessageCreateEvent) {
        try {
            val parser = MessageParameterParser(event.message)
            val query = parser.extractMultiSpaceString("query")

            val url = URL(
                "https://www.googleapis.com/customsearch/v1" +
                        "?q=" + URLEncoder.encode(query, "UTF-8") +
                        "&num=3" +
                        "&start=1" +
                        "&imgSize=medium" +
                        "&filetype=jpg" +
                        "&searchType=image" +
                        "&key=" + Keys.googleImages +
                        "&cx=002530997264605549526:retczad1ovw"
            )

            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                val jsonString = inputStream.bufferedReader().use {
                    it.lines().reduce { a, b -> a + "\n" + b }
                }
                val url = jsonString.toString().substringAfter("link\": \"").substringBefore("\"")
                event.channel.sendMessage(url)

            }
        } catch (e: Exception) {
            event.channel.sendMessage("sorry guys its broke wait until tomorrow lol")
        }
    }
}