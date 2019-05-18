package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent

class Broadcast : StandardCommand {
    override val commandName = "broadcast"
    private val broadcastChannelIds = listOf(548312509355130883, 366665395395887126, 567083500570673167)
    private val broadcastChannels: List<ServerTextChannel>

    constructor(api: DiscordApi) {
        this.broadcastChannels = broadcastChannelIds.map { c -> api.getServerTextChannelById(c).get() }
    }

    override fun action(event: MessageCreateEvent) {
        if (event.messageContent.contains("@everyone")) {
            event.channel.sendMessage("nice try r word")
            return
        }

        val embed = EmbedBuilder()
        embed.setAuthor(event.messageAuthor)
        event.messageAttachments.forEach { a -> embed.setImage(a.url.toString()) }
        embed.setTitle("broadcasted from ${event.server.get().name}/#${event.channel.asServerChannel().get().name}")
        embed.setDescription(event.messageContent.removePrefix("!broadcast "))
        embed.setTimestampToNow()

        broadcastChannels.forEach { c ->
            if (c != event.channel) {
                c.sendMessage(embed)
            }
        }

        event.message.addReaction("\uD83D\uDC4D")
    }
}