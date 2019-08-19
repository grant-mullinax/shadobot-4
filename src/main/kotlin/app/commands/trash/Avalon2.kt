package app.commands.trash

import app.commands.abstract.MessageProcess
import app.parsing.MessageParameterParser
import org.javacord.api.entity.channel.PrivateChannel
import org.javacord.api.event.message.MessageCreateEvent

class Avalon2 : MessageProcess {

    private var playerCount = -1
    private val rolesToChannels = mapOf<String, MutableSet<PrivateChannel>>(
        "badguy" to mutableSetOf(),
        "goodguy" to mutableSetOf(),
        "merlin" to mutableSetOf(),
        "percival" to mutableSetOf(),
        "oberon" to mutableSetOf(),
        "mordred" to mutableSetOf(),
        "morgana" to mutableSetOf(),
        "hitler" to mutableSetOf()
    )

    private fun tellGroupAboutGroup(prefix: String, tell: Collection<String>, about: Collection<String>) {
        tell.flatMap { rolesToChannels.getValue(it) }
            .forEach { channel ->
                channel.sendMessage(prefix + "\n" +
                    about.flatMap { rolesToChannels.getValue(it) }
                        .shuffled()
                        .map { it.recipient.name }
                        .reduce { a, b -> "$a\n$b" }
                )
            }
    }

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageContent.startsWith("!av", ignoreCase = true)
    }

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        if (event.messageContent.startsWith("!avstart", ignoreCase = true)) {
            playerCount = parser.extractInt("number of players")
            rolesToChannels.values.forEach { it.clear() }
            event.channel.sendMessage("started avalon game of size $playerCount")

        } else if (event.messageContent.startsWith("!avrole", ignoreCase = true)) {
            val playersInGame = rolesToChannels.map { it.value.size }.sum()
            if (playersInGame >= playerCount) {
                event.channel.sendMessage("this game is full! make a new one with !avstart [numberofplayers]")
                return
            }

            if (!event.isPrivateMessage) {
                event.channel.sendMessage("direct message me moron")
                event.message.delete()
                return
            }

            val privateChannel = event.privateChannel.get()

            if (rolesToChannels.values.map { it.contains(privateChannel) }.reduce { a, b ->  a || b}) {
                event.channel.sendMessage("hey! youre already in this game!")
                return
            }

            val role = parser.extractString("role name")
            if (!rolesToChannels.keys.contains(role)) {
                event.channel.sendMessage("try again! valid roles are:\n" +
                        rolesToChannels.keys.reduce { a, b -> "$a\n$b" }
                )
                return
            }
            rolesToChannels.getValue(role).add(privateChannel)

            if (playersInGame + 1 == playerCount) {
                val everyone = rolesToChannels.map { it.value }.flatten()
                everyone.forEach { it.sendMessage("Everyone has submitted their role!") }

                tellGroupAboutGroup("you and your fellow bad guys are:",
                    listOf("badguy", "morgana", "mordred"),
                    listOf("badguy", "morgana", "mordred")
                )

                tellGroupAboutGroup("the bad guys are:",
                    listOf("merlin"),
                    listOf("badguy", "morgana", "hitler", "oberon")
                )

                tellGroupAboutGroup("merlin and morgana are:",
                    listOf("percival"),
                    listOf("merlin", "morgana")
                )

                if (!rolesToChannels.getValue("hitler").isEmpty()) {
                    tellGroupAboutGroup("hitler is:",
                        listOf("badguy"),
                        listOf("hitler")
                    )
                }
            } else {
                event.channel.sendMessage("thanks !!!! waiting on ${playerCount - playersInGame} players")
            }
        }
    }
}