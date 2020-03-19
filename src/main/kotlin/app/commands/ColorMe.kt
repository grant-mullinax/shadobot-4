package app.commands

import app.Keys
import app.commands.abstract.StandardCommand
import app.commands.image.SimpleColor
import app.parsing.MessageParameterParser
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.imageio.ImageIO

class ColorMe : StandardCommand() {
    override val commandName = "colorme"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val query = parser.extractMultiSpaceString("query")

        val url = URL("https://www.googleapis.com/customsearch/v1" +
                "?q=" + URLEncoder.encode(query, "UTF-8") +
                "&num=3" +
                "&start=1" +
                "&imgSize=medium" +
                "&filetype=jpg" +
                "&searchType=image" +
                "&key=" + Keys.googleImages +
                "&cx=002530997264605549526:retczad1ovw")

        try {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"

                val jsonString = inputStream.bufferedReader().use {
                    it.lines().reduce { a, b -> a + "\n" + b }
                }
                val urlString = jsonString.toString().substringAfter("link\": \"").substringBefore("\"")
                val image = ImageIO.read(URL(urlString))
                var sum = SimpleColor(0,0,0)
                for (x in 0 until image.width) {
                    for (y in 0 until image.height) {
                        sum += SimpleColor(image.getRGB(x,y))
                    }
                }
                sum /= image.width * image.height

                val id = event.messageAuthor.id

                val role = event.server.get().roles.findLast { it.name == id.toString() } ?:
                event.server.get().createRoleBuilder().setName(id.toString()).create().get()

                role.updateColor(Color(sum.red, sum.green, sum.blue))

                event.messageAuthor.asUser().get().addRole(role)
            }
        } catch (e: Exception) {
            event.channel.sendMessage("sorry guys its broke wait until tomorrow lol")
        }

    }
}