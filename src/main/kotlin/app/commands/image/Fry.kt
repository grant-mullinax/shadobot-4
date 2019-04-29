package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.image.BufferedImage
import java.util.stream.IntStream

class Fry : StandardCommand() {
    override val commandName = "fry"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        val image = parser.extractImageAndLookUpward()

        val outputImage = BufferedImage(image.width, image.height, image.type)

        IntStream.range(2, image.width - 2).parallel().forEach { x ->
            IntStream.range(2, image.height - 2).forEach { y ->
                var outputColor = image.getRGB(x, y)
                for (direction in listOf(Pair(-2, 0), Pair(2, 0), Pair(0, -2), Pair(0, 2))) {
                    outputColor += image.getRGB(x + direction.first, y + direction.second) / 4
                }
                outputImage.setRGB(x, y, outputColor or 0xFF000000.toInt())
            }
        }

        val message = MessageBuilder()
        message.addAttachment(outputImage, "result.png")

        message.send(event.channel)
    }
}