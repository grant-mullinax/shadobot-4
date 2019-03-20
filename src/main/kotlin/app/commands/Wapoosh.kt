package app.commands

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Wapoosh: StandardCommand() {
    override val commandName = "wapoosh"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        var str = ""
        for (user in event.message.mentionedUsers) {
            str += "*${event.messageAuthor.asUser().get().name} and ${user.name} wapoosh*\n"
        }

        val r = (0..5).random()
        print(r)
        for (i in 1 until r) {
            event.channel.sendMessage(str)
        }
    }
}