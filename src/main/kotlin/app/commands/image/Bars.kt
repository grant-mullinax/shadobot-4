package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.image.BufferedImage
import java.util.stream.IntStream

class Bars : StandardCommand() {
    override val commandName = "bars"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val horizontal = parser.extractBool("horizontal toggle", false)
        val barWidth = parser.extractInt("bar width", 2)
        val flipWidth = parser.extractInt("flip width", 1)

        val image = parser.extractImageAndLookUpward()

        val outputImage = BufferedImage(image.width, image.height, image.type)

        IntStream.range(0, image.width).parallel().forEach { x ->
            IntStream.range(0, image.height).forEach { y ->
                if (horizontal) {
                    outputImage.setRGB(x, y, image.getRGB(if ((y/flipWidth) % barWidth == 0) image.width-x-1 else x, y))
                } else {
                    outputImage.setRGB(x, y, image.getRGB(x, if ((x/flipWidth) % barWidth == 0) image.height - y - 1 else y))
                }
            }
        }

        val message = MessageBuilder()
        message.addAttachment(outputImage, "result.png")

        message.send(event.channel)
    }
}