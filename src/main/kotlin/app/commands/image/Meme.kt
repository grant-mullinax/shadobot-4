package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.BasicStroke
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
        val bottomText = if (memeText.size > 1) memeText[1] else ""

        //val size = parser.extractInt("text size", 70)

        val image = parser.extractImageAndLookUpward()

        val scale = if (memeText.size == 3) memeText[2].toInt() else 10
        val size = image.width/scale

        val graphics = image.createGraphics()
        graphics.color = Color.black
        graphics.font = Font("Impact", Font.PLAIN, size)
        graphics.stroke = BasicStroke(size/10f)

        val topBOffset = graphics.fontMetrics.stringWidth(topText)
        val topTextGV = graphics.font.createGlyphVector(graphics.fontRenderContext, topText)
        val topTextOutline = topTextGV.outline

        graphics.translate(image.width/2 - topBOffset/2, ((4.0/3.0) * size).toInt())
        graphics.draw(topTextOutline)
        graphics.color = Color.white
        graphics.fill(topTextOutline)

        if (bottomText != "") {
            val bottomBOffset = graphics.fontMetrics.stringWidth(bottomText)
            val bottomTextGV = graphics.font.createGlyphVector(graphics.fontRenderContext, bottomText)
            val bottomTextOutline = bottomTextGV.outline
            graphics.color = Color.black
            graphics.translate((image.width/2 - bottomBOffset/2) - (image.width/2 - topBOffset/2), image.height - ((5.0/3.0) * size).toInt())
            graphics.draw(bottomTextOutline)
            graphics.color = Color.white
            graphics.fill(bottomTextOutline)
        }

        graphics.dispose()

        val message = MessageBuilder()
        message.addAttachment(image, "result.png")

        message.send(event.channel)
    }
}