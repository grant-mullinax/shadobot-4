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
        val files = dogeFolder.listFiles()
        val file = files[(0..(files.size-1)).random()]

        val embed = EmbedBuilder()
        embed.setImage(file)
        embed.setTitle(file.name)

        event.channel.sendMessage(embed)
    }
}