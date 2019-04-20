package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.util.stream.IntStream

class Drip: StandardCommand() {
    override val commandName = "drip"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val dripWidth = parser.extractInt("drip width", 50)
        val reset = parser.extractInt("reset chance", 10)

        val image = parser.extractImageAndLookUpward()

        val visted = mutableMapOf<Coordinate, SimpleColor>()
        /*val directions = listOf(
            Coordinate(0, -1), Coordinate(0, 1),
            Coordinate(1, 0), Coordinate(-1, 0),
            Coordinate(1, 1), Coordinate(1, -1),
            Coordinate(-1, 1), Coordinate(-1, -1)
        )*/

        val range = (-2..2)
        val directions = range.flatMap { x -> range.map { y -> Coordinate(x+1, y) } }.shuffled()

        IntStream.range(0, (image.width-1)/dripWidth).forEach {x ->
            var point = Coordinate(x * dripWidth, 0)
            var lastDirection = Coordinate(0, 1)
            var grad = 255f

            while (true) {
                var maxDirection = lastDirection
                var maxColor = -1f
                for (direction in directions) {
                    val movingTo = Coordinate(point.x + direction.x, point.y + direction.y)

                    if ((-direction.x == lastDirection.x && -direction.y == lastDirection.y) ||
                        (visted.contains(movingTo)) ||
                        (movingTo.x > image.width-1 || movingTo.x < 0) ||
                        (movingTo.y > image.height-1 || movingTo.y < 0)
                    ) continue

                    val darkness = SimpleColor(image.getRGB(movingTo.x, movingTo.y)).darkness().toFloat()
                    if (darkness > maxColor) {
                        maxDirection = direction
                        maxColor = darkness
                    }
                }
                val movingTo = Coordinate(point.x + maxDirection.x, point.y + maxDirection.y)
                if (visted.contains(movingTo) || maxColor == -1f) {
                    if ((0..reset).random() == 0)
                        break
                    grad = 255f
                    point = Coordinate((0 until image.width).random(), (0 until image.height).random())
                    continue
                }

                visted[movingTo] = SimpleColor(grad.toInt(), 0, 0)
                lastDirection = maxDirection
                point = movingTo
                grad -= 0.1f
            }
        }

        for (point in visted) {
            image.setRGB(point.key.x, point.key.y, point.value.toInt())
        }

        val message = MessageBuilder()
        message.addAttachment(image, "result.png")

        message.send(event.channel)
    }
}