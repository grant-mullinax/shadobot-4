package app.commands.wackynumbers

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Alignment: StandardCommand() {
    override val commandName = "alignment"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val mentionedSomeone = event.message.mentionedUsers.isEmpty()
        val id = if (mentionedSomeone) event.messageAuthor.id else event.message.mentionedUsers[0].id
        val targetName = if (mentionedSomeone) "Your" else "${event.message.mentionedUsers[0].name}'s"
        val alignmentCode = id % 9

        val alignment = when (alignmentCode) {
            0L -> "Lawful Good :angel:"
            1L -> "Lawful Neutral :zipper_mouth:"
            2L -> "Chaotic Evil :japanese_goblin:"
            3L -> "Chaotic Neutral :upside_down:"
            4L -> "Neutral Good :yum:"
            5L -> "Neutral Evil :smirk:"
            6L -> "Chaotic Good :upside_down:"
            7L -> "True Neutral :expressionless:"
            8L -> "Lawful Evil :levitate:"
            else -> "?"

        }

        event.channel.sendMessage(
            "$targetName alignment is $alignment")
    }
}