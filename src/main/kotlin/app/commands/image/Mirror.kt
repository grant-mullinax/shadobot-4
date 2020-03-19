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

class Mirror : StandardCommand() {
    override val commandName = "mirror"

    override fun action(event: MessageCreateEvent) {
        GlobalScope.launch {
            val parser = MessageParameterParser(event.message)

            val image = parser.extractImageAndLookUpward()


            IntStream.range(image.width/2, image.width).parallel().forEach { x ->
                IntStream.range(0, image.height).forEach { y ->
                    image.setRGB(
                        x,
                        y,
                        image.getRGB(
                            (image.width/2 - (x - image.width/2)).coerceIn(0, image.width-1),
                            y
                        )
                    )
                }
            }

            val message = MessageBuilder()
            message.addAttachment(image, "result.png")

            message.send(event.channel)
        }
    }
}