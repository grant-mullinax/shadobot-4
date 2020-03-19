package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.stream.IntStream
import kotlin.math.round

class Dream : StandardCommand() {
    override val commandName = "dream"

    override fun action(event: MessageCreateEvent) {
        GlobalScope.launch {
            val parser = MessageParameterParser(event.message)

            val image = parser.extractImageAndLookUpward()


            IntStream.range(0, image.width).parallel().forEach { x ->
                IntStream.range(0, image.height).forEach { y ->
                    val color = SimpleColor(image.getRGB(x, y))
                    val scale = color.darkness()/255.0

                    image.setRGB(
                        x,
                        y,
                        SimpleColor.fromHsv(scale.toFloat().coerceIn(0.0f, 0.99f), 1f, 1f).toInt()
                    )
                }
            }

            val message = MessageBuilder()
            message.addAttachment(image, "result.png")

            message.send(event.channel)
        }
    }
}