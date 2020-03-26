package app.commands

import app.CommunistBot
import app.commands.abstract.AdminCommand
import app.commands.abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent

class ListCommands(private val bot: CommunistBot) : StandardCommand() {
    override val commandName = "help"

    override fun action(event: MessageCreateEvent) {
        var commandString = ""
        bot.processes.forEach {
            when(it) {
                is AdminCommand -> commandString += "ADMIN ${it.prefix}${it.commandName}\n"
                is StandardCommand -> commandString += "${it.prefix}${it.commandName}\n"
            }
        }

        event.channel.sendMessage(commandString)
    }
}