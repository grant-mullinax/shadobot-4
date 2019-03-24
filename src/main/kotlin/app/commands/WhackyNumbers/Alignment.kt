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
            0L -> alignment = "Lawful Good"
            1L -> alignment = "Lawful Neutral"
            2L -> alignment = "Lawful Evil"
            3L -> alignment = "Neutral Good"
            4L -> alignment = "True Neutral"
            5L -> alignment = "Neutral Evil"
            6L -> alignment = "Chaotic Good"
            7L -> alignment = "Chaotic Neutral"
            8L -> alignment = "Chaotic Evil"

        }

        event.channel.sendMessage(
            "$targetName alignment is $alignment")
    }
}