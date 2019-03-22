package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import javax.imageio.ImageIO
import java.io.File



class AddDoge: StandardCommand() {
    override val commandName = "adddoge"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        // todo fix maybe vuln?
        val dogeFolder = File("doge")

        val splitMsg = event.message.content.split(" ")
        val fileName = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }

        if (File(dogeFolder, "$fileName.png").exists()) {
            event.channel.sendMessage("A doge with that name already exists!")
            return
        }


        event.message.addReaction("\uD83D\uDC15")
        event.channel.sendMessage("Doge waiting for approval! ${api.getUserById(155061315977740288).get().mentionTag}")
    }

    fun receiveVoteReaction(event: ReactionAddEvent) {
        if (!event.reaction.get().emoji.equalsEmoji("\uD83D\uDC15")) {
            return
        }

        if (event.user.id != 155061315977740288) {
            return
        }

        if (!event.message.get().content.startsWith("!adddoge", ignoreCase = true)) {
            return
        }

        val splitMsg = event.message.get().content.split(" ")
        val fileName = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }

        val image = event.message.get().attachments.first().downloadAsImage().join()
        val imageFile = File("doge/$fileName.png")
        ImageIO.write(image, "png", imageFile)
    }
}