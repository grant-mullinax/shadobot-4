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

class MegaDream : StandardCommand() {
    override val commandName = "megadream"

    override fun action(event: MessageCreateEvent) {
        GlobalScope.launch {
            val parser = MessageParameterParser(event.message)
            val intensity = parser.extractFloat("intensity", 1f)
            val transparency = parser.extractFloat("transparency", 1f)
            val width = parser.extractFloat("rainbow width", 1f)

            val image = parser.extractImageAndLookUpward()


            IntStream.range(0, image.width).parallel().forEach { x ->
                IntStream.range(0, image.height).forEach { y ->
                    val color = SimpleColor(image.getRGB(x, y))
                    val scale = ((color.darkness() + (x + y) * width) % 256)/255.0 * intensity
                    val rainbow = SimpleColor.fromHsv(scale.toFloat().coerceIn(0.0f, 0.99f), 1f, 1f)

                    image.setRGB(
                        x,
                        y,
                        ((rainbow * transparency) + (color * (1 - transparency))).toInt()
                    )
                }
            }

            val message = MessageBuilder()
            message.addAttachment(image, "result.png")

            message.send(event.channel)
        }
    }
}