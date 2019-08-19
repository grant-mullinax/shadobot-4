package app.commands.trash

import app.commands.abstract.MessageProcess
import app.parsing.MessageParameterParser
import org.javacord.api.entity.channel.PrivateChannel
import org.javacord.api.event.message.MessageCreateEvent

class Avalon : MessageProcess {
    var playerCount = -1
    val players = mutableListOf<Pair<PrivateChannel, String>>()

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith("!av", ignoreCase = true)
    }

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        if (event.messageContent.startsWith("!avstart", ignoreCase = true)) {
            playerCount = parser.extractInt("number of players")
            players.clear()
            event.channel.sendMessage("started avalon game of size $playerCount")
        } else if (event.messageContent.startsWith("!avrole", ignoreCase = true)) {
            val role = parser.extractString("role name")
            val validRoles = setOf("badguy", "goodguy", "merlin", "percival", "morgana")
            if (!validRoles.contains(role)) {
                event.channel.sendMessage("try again! valid roles are:\n" +
                        validRoles.reduce { a, b -> "$a\n$b" }
                )
                return
            }
            players.add(Pair(event.privateChannel.get(), role))

            if (players.size == playerCount) {
                players.forEach { it.first.sendMessage("Everyone has submitted their role!") }

                val badGuys = players.filter { it.second == "badguy" || it.second == "morgana" }.shuffled()
                val stringOfBadGuys = "the bad guys are:\n" + badGuys.map { it.first.recipient.name }.reduce { a, b -> "$a\n$b" }
                badGuys.forEach {
                        it.first.sendMessage(stringOfBadGuys + "hitler is " + players.filter { it.second == "hitler" }
                        )
                }
                players.filter { it.second == "merlin" }.forEach { it.first.sendMessage(stringOfBadGuys) }

                players.filter { it.second == "percival" }.forEach {
                    it.first.sendMessage("merlin appears to be:" +
                        players.filter { it.second == "merlin" || it.second == "morgana" }
                            .shuffled().map { it.first.recipient.name }.reduce { a, b -> "$a\n$b" }
                    )
                }
                playerCount = 0
                players.clear()
            } else {
                event.channel.sendMessage("thanks !!!! waiting on ${playerCount - players.size} players")
            }
        }
    }
}