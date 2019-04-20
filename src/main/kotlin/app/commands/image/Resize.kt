package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.math.roundToInt

class Resize : StandardCommand() {
    override val commandName = "resize"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val image = parser.extractImageAndLookUpward()
        val scale = parser.extractDouble("rotation amount", 0.5)

        val transform = AffineTransform()

        transform.scale(scale, scale)
        val op = AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)

        val message = MessageBuilder()
        val rotatedImage = BufferedImage(abs(image.width * scale).roundToInt(), abs(image.height * scale).roundToInt(), image.type)
        op.filter(image, rotatedImage)
        message.addAttachment(rotatedImage, "result.png")

        message.send(event.channel)
    }
}