package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.image.BufferedImage
import java.util.stream.IntStream

class RotateColor : StandardCommand() {
    override val commandName = "rotatecolor"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)

        val image = parser.extractImageAndLookUpward()

        val outputImage = BufferedImage(image.width, image.height, image.type)

        IntStream.range(0, image.width).parallel().forEach { x ->
            IntStream.range(0, image.height).forEach { y ->
                val color = SimpleColor(image.getRGB(x, y))
                outputImage.setRGB(x, y, SimpleColor(color.green, color.blue, color.red, 255).toInt())
            }
        }

        val message = MessageBuilder()
        message.addAttachment(outputImage, "result.png")

        message.send(event.channel)
    }
}