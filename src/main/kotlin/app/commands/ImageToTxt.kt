package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color
import java.awt.image.BufferedImage
import java.lang.StringBuilder
import kotlin.math.roundToInt

class ImageToTxt: StandardCommand() {
    override val commandName = "txtify"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val splitMsg = event.message.content.split(" ")
        val gradient = if (splitMsg.size > 2) splitMsg[2] else "─▒▓▓▀"

        val scale = if (splitMsg.size > 1) splitMsg[1].toInt() else 25
        val image = event.message.attachments.first().downloadAsImage().join()
        val outputHeight = ((image.height.toFloat()/image.width * scale * 9)/22).toInt()

        val ascii = StringBuilder(".\n`")

        for (y in 0 until outputHeight) {
            for (x in 0 until scale) {
                val relativeX = ((x.toFloat() + .5f)/scale * image.width).toInt()
                val relativeY = ((y.toFloat() + .5f)/outputHeight * image.height).toInt()
                val rgb = image.getRGB(relativeX, relativeY)
                val red = rgb shr 16 and 0xFF
                val green = rgb shr 8 and 0xFF
                val blue = rgb and 0xFF
                val charDistribution = (red + blue + green).toFloat()/(0xFF * 3) * (gradient.length - 1)
                val char = gradient[charDistribution.toInt()]

                ascii.append(char)
            }
            ascii.appendln()
        }
        ascii.append("`")
        event.channel.sendMessage(ascii.toString())
    }
}