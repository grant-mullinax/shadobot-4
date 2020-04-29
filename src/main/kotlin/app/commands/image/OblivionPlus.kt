package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.util.stream.IntStream
import kotlin.math.round

class OblivionPlus : StandardCommand() {
    override val commandName = "oblivionplus"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val palatization = parser.extractInt("palatization", 120)
        val resolution = parser.extractInt("edge resolution", 1)
        val brighten = parser.extractInt("brighten scale", 10)

        val image = parser.extractImageAndLookUpward()
        val range = (-resolution..resolution)
        val directions = range.flatMap { x -> range.map { y -> Pair(x, y) } }

        IntStream.range(0, image.width).parallel().forEach { x ->
            IntStream.range(0, image.height).forEach { y ->
                var outputColor = SimpleColor(image.getRGB(x, y)).toGreyscale()
                    .applyToEach { x -> (round(x.toFloat()/palatization) * palatization).toInt() }
                for (direction in directions) {
                    if (direction.first == 0 && direction.second == 0) continue
                    if ((x + direction.first > image.width - 1 || x + direction.first < 0) ||
                            (y + direction.second > image.height - 1 || y + direction.second < 0)) continue
                    outputColor -= SimpleColor(
                            image.getRGB(
                                    x + direction.first,
                                    y + direction.second
                            )
                    ) / (directions.size - 1)
                }

                image.setRGB(x, y, (outputColor * brighten).abs().toInt())
            }
        }

        val message = MessageBuilder()
        message.addAttachment(image, "result.png")

        message.send(event.channel)
    }
}