package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.util.nullable
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent

class Broadcast : StandardCommand {
    override val commandName = "broadcast"
    private val broadcastChannels: List<ServerTextChannel>

    constructor(api: DiscordApi) {
        this.broadcastChannels = api.servers.mapNotNull { it.systemChannel.nullable() }
    }

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        var broadcastText = parser.extractMultiSpaceString("broadcast text")

        if (event.messageContent.contains("@everyone")) {
            event.channel.sendMessage("nice try buddy")
            return
        }

        val embed = EmbedBuilder()
        embed.setAuthor(event.messageAuthor)
        event.messageAttachments.forEach { a -> embed.setImage(a.url.toString()) }
        embed.setTitle("broadcasted from ${event.server.get().name}/#${event.channel.asServerChannel().get().name}")
        embed.setDescription(event.messageContent.removePrefix(broadcastText))
        embed.setTimestampToNow()

        broadcastChannels.forEach { c ->
            if (c != event.channel) {
                c.sendMessage(embed)
            }
        }

        event.message.addReaction("\uD83D\uDC4D")
    }
}