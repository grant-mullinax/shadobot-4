package app.science

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.Color

class ImgTest : StandardCommand() {
    override val commandName = "imgtest"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val image = event.message.attachments.first().downloadAsImage().join()

        val outputHeight = image.height / image.width * 50 + 1

        for (x in 0 until 50) {
            for (y in 0 until outputHeight) {
                val relativeX = x.toFloat() / 50 * image.width
                val relativeY = y.toFloat() / outputHeight * image.height
                image.setRGB(relativeX.toInt(), relativeY.toInt(), Color.red.rgb)
            }
        }

        val messageBuilder = MessageBuilder()
        messageBuilder.addAttachment(image, "xd.png").setContent("hey")
        messageBuilder.send(event.channel)
    }
}