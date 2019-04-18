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

class RotateColor: StandardCommand() {
    override val commandName = "rotatecolor"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)

        val image = parser.extractImageAndLookUpward()

        val outputImage = BufferedImage(image.width, image.height, image.type)

        IntStream.range(0, image.width).parallel().forEach {x ->
            IntStream.range(0, image.height).forEach {y ->
                val color = SimpleColor(image.getRGB(x, y))
                outputImage.setRGB(x, y, SimpleColor(color.green, color.blue, color.red, 255).toInt())
            }
        }

        val message = MessageBuilder()
        message.addAttachment(outputImage, "result.png")

        message.send(event.channel)
    }
}