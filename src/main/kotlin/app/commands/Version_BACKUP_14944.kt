package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class Version : StandardCommand() {
    override val commandName = "version"

    override fun action(event: MessageCreateEvent) {
<<<<<<< HEAD
        event.channel.sendMessage("Built on: 05/04 19:32:58")
=======
        event.channel.sendMessage("Built on: 05/04 14:46:25")
>>>>>>> a7c1114a39f2a03532808045b1dc1509657f686e
    }
}