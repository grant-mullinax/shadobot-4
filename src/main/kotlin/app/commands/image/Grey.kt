package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.image.BufferedImage
import java.util.stream.IntStream
import kotlin.math.round

class Grey : StandardCommand() {
    override val commandName = "grey"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        val image = parser.extractImageAndLookUpward()

        IntStream.range(0, image.width).parallel().forEach { x ->
            IntStream.range(0, image.height).forEach { y ->
                val darkness = SimpleColor(image.getRGB(x, y)).darkness()
                val color = SimpleColor(darkness, darkness, darkness)

                image.setRGB(x, y, color.toInt())
            }
        }

        val message = MessageBuilder()
        message.addAttachment(image, "result.png")

        message.send(event.channel)
    }
}