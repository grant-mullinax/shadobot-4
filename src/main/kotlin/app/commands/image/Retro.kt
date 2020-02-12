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

class Retro : StandardCommand() {
    override val commandName = "retro"

    override fun action(event: MessageCreateEvent) {
        GlobalScope.launch {
            val parser = MessageParameterParser(event.message)
            val pxScale = parser.extractInt("pixel size", 4)
            val palatization = parser.extractInt("palatization", 60)

            val image = parser.extractImageAndLookUpward()

            val outputImage = BufferedImage(image.width, image.height, image.type)

            IntStream.range(0, image.width).parallel().forEach { x ->
                IntStream.range(0, image.height).forEach { y ->
                    val color = SimpleColor(image.getRGB(
                        (round(x.toFloat()/pxScale) * pxScale).toInt().coerceIn(0, image.width-1),
                        (round(y.toFloat()/pxScale) * pxScale).toInt().coerceIn(0, image.height-1)
                    )).applyToEach { x -> (round(x.toFloat()/palatization) * palatization).toInt() }

                    outputImage.setRGB(
                        x,
                        y,
                        color.toInt()
                    )
                }
            }

            val message = MessageBuilder()
            message.addAttachment(outputImage, "result.png")

            message.send(event.channel)
        }
    }
}