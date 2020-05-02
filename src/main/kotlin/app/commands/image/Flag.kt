package app.commands.image

import app.Keys
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color
import java.awt.Graphics2D
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.stream.IntStream
import javax.imageio.ImageIO


class Flag : StandardCommand() {
    override val commandName = "thin"

    override fun action(event: MessageCreateEvent) {
        try {
            val parser = MessageParameterParser(event.message)
            val query = parser.extractMultiSpaceString("query").substringBefore("line")

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
                val urlString = jsonString.toString().substringAfter("link\": \"").substringBefore("\"")

                val flag = ImageIO.read(File("flag.png"))
                val image = ImageIO.read(URL(urlString))

                val lineStart = 132
                val lineEnd = 180

                IntStream.range(0, flag.width).parallel().forEach { x ->
                    IntStream.range(lineStart, lineEnd).forEach { y ->
                        val color = Color(flag.getRGB(x, y), true)
                        if (color.alpha <= .5) {
                            flag.setRGB(x, y, image.getRGB(
                                (x.toFloat() * image.width/flag.width).toInt(),
                                ((y.toFloat() - lineStart) * image.height/(lineEnd - lineStart)).toInt()
                            )
                            )
                        }
                    }
                }

                val message = MessageBuilder()
                message.addAttachment(flag, "result.png")

                message.send(event.channel)

            }
        } catch (e: Exception) {
            event.channel.sendMessage("sorry guys its broke wait until tomorrow lol")
        }
    }
}