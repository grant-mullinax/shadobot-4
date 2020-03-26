package app.commands.trash

import app.commands.abstract.MessageProcess
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.permission.Role
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.random.Random

class WaifuTroll(val api: DiscordApi) : MessageProcess {
    private val embeds = api.getTextChannelById(689202809064783885).get().getMessages(100).get().filter { it.author.id == 472141928578940958 }

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.server.get().id == 605204252352053248 && Random.nextInt(20) == 1
    }

    override fun action(event: MessageCreateEvent) {
        embeds.random().toMessageBuilder().send(event.channel)
    }
}