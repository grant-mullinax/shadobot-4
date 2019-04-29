package app.commands

import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class RussianRoulette : StandardCommand() {
    override val commandName = "rnr"
    var bullet = 0

    override fun action(event: MessageCreateEvent) {
        if (bullet == 0) {
            event.channel.sendMessage("*squee... whrrrrrrrrr* (you just put the bullet in the gun and spun it)")
            bullet = (1..6).random()
        } else {
            bullet--
            if (bullet == 0) {
                event.channel.sendMessage(":boom: ***BANG*** :boom:")
                val invite = event.server.get().invites.join().find { i -> !i.isTemporary }
                if (invite != null) {
                    event.messageAuthor.asUser().get().sendMessage("cling to life\n${invite.url}")
                }
                event.server.get().kickUser(event.messageAuthor.asUser().get())
            } else {
                event.channel.sendMessage("*click*")
            }
        }
    }
}