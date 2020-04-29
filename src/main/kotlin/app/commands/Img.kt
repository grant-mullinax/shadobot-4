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

            println(jsonString.toString())
            val urlString = jsonString.toString().substringAfter("link\": \"").substringBefore("\"")
            println(urlString)
            try {
                val image = ImageIO.read(URL(urlString))

                val message = MessageBuilder()
                message.addAttachment(image, "result.png")

                message.send(event.channel)
            } catch (e: Exception) {
                println(e.message)
                event.channel.sendMessage("you probably had no results bro")
            }
        } 
    }
}