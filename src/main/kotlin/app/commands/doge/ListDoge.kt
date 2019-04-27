package app.commands.doge

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.io.File


class ListDoge : StandardCommand() {
    override val commandName = "querydoge"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        // todo fix maybe vuln?
        val dogeFolder = File("doge")

        val splitMsg = event.message.content.split(" ")
        val query = splitMsg.subList(1, splitMsg.size).reduce { a, b -> "$a $b" }

        val files = dogeFolder.listFiles().filter { f -> f.name.contains(query, ignoreCase = true) }
        if (files.size > 40) {
            event.channel.sendMessage("Query returned too many doges! get more specific!")
        }

        val dogeList = files.map { f -> f.name }.reduce { a, b -> "$a\n$b" }

        event.channel.sendMessage("Doges Returned:\n$dogeList")
    }
}