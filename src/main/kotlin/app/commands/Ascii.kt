package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.core.event.message.MessageCreateEventImpl
import java.awt.image.BufferedImage
import java.lang.Float.min
import java.lang.StringBuilder
import java.util.*
import kotlin.math.sqrt

class Ascii: StandardCommand() {
    override val commandName = "txtify"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val splitMsg = event.message.content.split(" ")
        val gradient = if (splitMsg.size > 2) splitMsg[2] else "─▒▓▓█"

        val scale = if (splitMsg.size > 1) min(splitMsg[1].toFloat(), 1f) else 1f
        val image = event.message.attachments.first().downloadAsImage().join()

        event.channel.sendMessage(imgToText(image, scale, gradient))

        val message = MessageCreateEventImpl(event.message)
    }

    companion object {
        fun imgToText(image: BufferedImage, scale: Float, gradient: String): String {
            val imageRatio = image.height.toFloat() / image.width
            val outputRatio = (imageRatio + sqrt(imageRatio * (1800 + imageRatio))) / imageRatio * scale
            val outputHeight = (image.height.toFloat() / image.width * outputRatio * 9f / 14).toInt()
            val outputWidth = (outputRatio * 14f / 9).toInt()

            val ascii = StringBuilder(".\n`")

            val sampledColors = mutableListOf<MutableList<Float>>()

            var edgeSamples = outputHeight * outputWidth / 200
            var max = LinkedList<Float>(List(edgeSamples) {0f})
            var min = LinkedList<Float>(List(edgeSamples) {(0xff * 3).toFloat()})

            for (y in 0 until outputHeight) {
                sampledColors.add(mutableListOf())
                for (x in 0 until outputWidth) {
                    val relativeX = ((x.toFloat() + .5f) * image.width / outputWidth).toInt()
                    val relativeY = ((y.toFloat() + .5f) * image.height / outputHeight).toInt()

                    val rgb = image.getRGB(relativeX, relativeY)

                    val red = rgb shr 16 and 0xFF
                    val green = rgb shr 8 and 0xFF
                    val blue = rgb and 0xFF

                    val darkness = (red + blue + green).toFloat()
                    sampledColors[y].add(darkness)

                    if (darkness < min.peekLast()) {
                        min.push(darkness)
                        min.sort()
                        min.removeLast()
                    } else if (max.peekLast() < darkness) {
                        max.push(darkness)
                        max.sortDescending()
                        max.removeLast()
                    }
                }
            }

            val minAvg = min.average().toFloat()
            val colorRange = max.average().toFloat() - minAvg

            for (y in 0 until outputHeight) {
                for (x in 0 until outputWidth) {
                    val charDistribution = ((sampledColors[y][x] - minAvg) / colorRange).coerceIn(0f, 1f) * (gradient.length - 1)
                    val char = gradient[charDistribution.toInt()]

                    ascii.append(char)
                }
                ascii.appendln()
            }
            ascii.append("`")

            return ascii.toString()
        }
    }
}