package app.commands.WhackyNumbers

import app.commands.Abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Alignment: StandardCommand() {
    override val commandName = "alignment"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val mentionedSomeone = event.message.mentionedUsers.isEmpty()
        val id = if (mentionedSomeone) event.messageAuthor.id else event.message.mentionedUsers[0].id
        val targetName = if (mentionedSomeone) "Your" else "${event.message.mentionedUsers[0].name}'s"
        val alignmentCode = id % 9
        var alignment = "none?"

        when (alignmentCode) {
            0L -> alignment = "Lawful Good :angel:"
            1L -> alignment = "Lawful Neutral :zipper_mouth:"
            2L -> alignment = "Lawful Evil :levitate:"
            3L -> alignment = "Neutral Good :yum:"
            4L -> alignment = "True Neutral :expressionless:"
            5L -> alignment = "Neutral Evil :smirk:"
            6L -> alignment = "Chaotic Good :upside_down:"
            7L -> alignment = "Chaotic Neutral :upside_down:"
            8L -> alignment = "Chaotic Evil :japanese_goblin:"

        }

        event.channel.sendMessage(
            "$targetName alignment is $alignment")
    }
}