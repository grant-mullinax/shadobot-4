package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color
import java.awt.Font
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import kotlin.math.cos
import kotlin.math.sin

class Meme : StandardCommand() {
    override val commandName = "meme"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val memeText = parser.extractMultiSpaceString("meme text").split("|")
        val topText = memeText[0]
        val bottomText = if (memeText.size == 2) memeText[1] else throw ParserFailureException(
            "failed to separate top and bottom text"
        )
        val size = parser.extractInt("text size", 70)

        val image = parser.extractImageAndLookUpward()

        val graphics = image.createGraphics()
        graphics.color = Color.black
        graphics.font = Font("Impact", Font.BOLD, size)

        val topBOffset = graphics.fontMetrics.stringWidth(topText)
        graphics.drawString(
            topText,
            image.width/2 - topBOffset/2,
            20 + size
        )

        val bottomBOffset = graphics.fontMetrics.stringWidth(bottomText)
        graphics.drawString(
            bottomText,
            image.width/2 - bottomBOffset/2,
            image.height - 20
        )


        val bigSize = (size * 1.1).toInt()
        graphics.color = Color.white
        graphics.font = Font("Impact", Font.PLAIN, bigSize)

        val topOffset = graphics.fontMetrics.stringWidth(topText)
        graphics.drawString(
            topText,
            image.width/2 - topOffset/2,
            20 + bigSize
        )

        val bottomOffset = graphics.fontMetrics.stringWidth(bottomText)
        graphics.drawString(
            bottomText,
            image.width/2 - bottomOffset/2,
            image.height - 20
        )

        graphics.dispose()

        val message = MessageBuilder()
        message.addAttachment(image, "result.png")

        message.send(event.channel)
    }
}