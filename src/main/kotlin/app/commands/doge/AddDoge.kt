package app.commands.doge

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import java.io.File
import javax.imageio.ImageIO


class AddDoge : StandardCommand() {
    override val commandName = "adddoge"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val dogeFolder = File("doge")

        val parser = MessageParameterParser(event.message)
        val fileName = parser.extractMultiSpaceString("filename")

        if (File(dogeFolder, "$fileName.png").exists()) {
            event.channel.sendMessage("A doge with that name already exists!")
            return
        }


        event.message.addReaction("\uD83D\uDC15")
        event.channel.sendMessage("doge waiting for approval! ${api.getUserById(155061315977740288).get().mentionTag}")
    }

    fun receiveVoteReaction(event: ReactionAddEvent) {
        if (!event.reaction.get().emoji.equalsEmoji("\uD83D\uDC15")) {
            return
        }

        // me katie laura
        if (event.user.id != 155061315977740288 && event.user.id != 276215481025822730 && event.user.id != 155500829929897984) {
            return
        }

        if (!event.message.get().content.startsWith("!adddoge", ignoreCase = true)) {
            return
        }

        val parser = MessageParameterParser(event.message.get())
        val fileName = parser.extractMultiSpaceString("filename")

        val image = parser.extractImageAndLookUpward()
        val imageFile = File("doge/$fileName.png")
        ImageIO.write(image, "png", imageFile)

        event.message.get().addReaction("\uD83C\uDF8A")
    }
}