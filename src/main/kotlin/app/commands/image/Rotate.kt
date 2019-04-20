package app.commands.image

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import kotlin.math.cos
import kotlin.math.sin

class Rotate: StandardCommand() {
    override val commandName = "rotate"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val image = parser.extractImageAndLookUpward()
        val rotation = parser.extractInt("rotation amount", 90) * 3.14159/180

        val transform = AffineTransform()
        val width = image.width.toDouble()
        val height = image.height.toDouble()

        transform.rotate(rotation, width/2, height/2)
        transform.translate(
            cos(rotation) * width + sin(rotation) * height,
            cos(rotation) * height + sin(rotation) * width
            )
        // transform.shear(1.0, 0.0)
        val op = AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)

        val message = MessageBuilder()
        val rotatedImage = BufferedImage(image.width*2, image.height*2, image.type)
        op.filter(image, rotatedImage)
        message.addAttachment(rotatedImage, "result.png")

        message.send(event.channel)
    }
}