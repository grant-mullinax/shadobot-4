package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import javax.imageio.ImageIO
import java.io.File



class RandomDoge: StandardCommand() {
    override val commandName = "doge"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val dogeFolder = File("doge")
        var file: File

        val splitMsg = event.message.content.split(" ")
        if (splitMsg.size > 1) {
            val fileName = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }
            file = File(dogeFolder, "$fileName.png")

            if (!file.exists()) {
                event.channel.sendMessage("Doge $fileName.png not found!")
                return
            }
        } else {
            val files = dogeFolder.listFiles()
            file = files[(0..(files.size - 1)).random()]
        }

        val embed = EmbedBuilder()
        embed.setImage(file)
        embed.setTitle(file.nameWithoutExtension)

        event.channel.sendMessage(embed)
    }
}