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

class Edges : StandardCommand() {
    override val commandName = "edges"

    override fun action(event: MessageCreateEvent) {
        GlobalScope.launch {
            val parser = MessageParameterParser(event.message)
            val resolution = parser.extractInt("edge resolution", 1)
            val brighten = parser.extractInt("brighten scale", 1)

            val image = parser.extractImageAndLookUpward()

            val outputImage = BufferedImage(image.width, image.height, image.type)

            val range = (-resolution..resolution)
            val directions = range.flatMap { x -> range.map { y -> Pair(x, y) } }

            IntStream.range(0, image.width).parallel().forEach { x ->
                IntStream.range(0, image.height).forEach { y ->
                    var outputColor = SimpleColor(image.getRGB(x, y))
                    for (direction in directions) {
                        if (direction.first == 0 && direction.second == 0) continue
                        if ((x + direction.first > image.width - 1 || x + direction.first < 0) ||
                            (y + direction.second > image.height - 1 || y + direction.second < 0)
                        ) continue
                        outputColor -= SimpleColor(
                            image.getRGB(
                                x + direction.first,
                                y + direction.second
                            )
                        ) / (directions.size - 1)
                    }

                    outputImage.setRGB(x, y, (outputColor * brighten).abs().toInt())
                }
            }

            val message = MessageBuilder()
            message.addAttachment(outputImage, "result.png")

            message.send(event.channel)
        }
    }
}