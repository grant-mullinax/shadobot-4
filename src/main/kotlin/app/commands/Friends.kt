package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.io.File

class Friends : StandardCommand() {
    override val commandName = "friend"

    override fun action(event: MessageCreateEvent) {
        val friendsFolder = File("friends")
        val files = friendsFolder.listFiles()!!
        val index = ((event.messageAuthor.id % 25565).toInt() % files.size)
        val file = files[index]

        val embed = EmbedBuilder()
        embed.setImage(file)

        event.channel.sendMessage(embed)
    }
}