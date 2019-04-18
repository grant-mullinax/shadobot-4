package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.util.SimpleColor
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import java.util.stream.IntStream
import java.util.stream.StreamSupport.intStream
import javax.imageio.ImageIO
import kotlin.collections.HashSet
import kotlin.math.sqrt

class Edges: StandardCommand() {
    override val commandName = "edges"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)

        val image = parser.extractImageAndLookUpward()

        val outputImage = BufferedImage(image.width, image.height, image.type)

        IntStream.range(1, image.width-1).forEach {x ->
            IntStream.range(1, image.height-1).parallel().forEach {y ->
                var outputColor = SimpleColor(image.getRGB(x, y))
                for (direction in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
                    outputColor -= SimpleColor(image.getRGB(x + direction.first, y + direction.second))/4
                }
                outputImage.setRGB(x, y, (outputColor * 10).abs().darknessToAlpha().toInt())
            }
        }

        val message = MessageBuilder()
        message.addAttachment(outputImage, "result.png")

        message.send(event.channel)
    }
}