package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.math.abs

class Love: StandardCommand() {
    override val commandName = "love"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val users = event.message.mentionedUsers
        var love = 100 - users.map { u -> u.id % 100 }.reduce{ a, b -> abs(a - b) }

        if (users.find { u -> u.id == 105639759593877504 || u.id == 496423321974341664} != null) {
            love = (89..100).random().toLong()
        }

        val userNames = users.subList(0, users.size - 1).map { u -> u.name }.reduce { a, b -> "$a, $b" } + " and ${users[users.size-1].name}"

        event.channel.sendMessage("$userNames's compatibility for love is $love%")
    }
}