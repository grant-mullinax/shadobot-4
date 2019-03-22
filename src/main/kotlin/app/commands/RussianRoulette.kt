package app.commands

import app.commands.Abstract.StandardCommand
import app.util.format
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class RussianRoulette: StandardCommand() {
    override val commandName = "rnr"
    var bullet = 0

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        if (bullet == 0) {
            event.channel.sendMessage("*squee... whrrrrrrrrr* (you just put the bullet in the gun and spun it)")
            bullet = (1..6).random()
        } else {
            bullet--
            if (bullet == 0) {
                event.channel.sendMessage(":boom: ***BANG*** :boom:")
                event.messageAuthor.asUser().get().sendMessage("cling to life\nhttps://discord.gg/XSt5pV4")
                event.server.get().kickUser(event.messageAuthor.asUser().get())
            } else {
                event.channel.sendMessage("*click*")
            }
        }
    }
}