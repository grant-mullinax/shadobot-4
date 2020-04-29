package app.commands

import app.CommunistBot
import app.commands.abstract.AdminCommand
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.event.message.MessageCreateEvent

class Help(private val bot: CommunistBot) : StandardCommand() {
    override val commandName = "help"
    override val helpString = "who helps the helpers"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        var commandName = parser.extractNullableString()

        if (commandName == null) {
            var commandString = ""
            bot.processes.forEach {
                when (it) {
                    is AdminCommand -> commandString += "ADMIN ${it.prefix}${it.commandName}\t"
                    is StandardCommand -> commandString += "${it.prefix}${it.commandName}\t"
                }
            }
            event.channel.sendMessage(commandString)
        } else {
            val command = bot.processes.find {
                when (it) {
                    is StandardCommand -> it.commandName.toLowerCase() == commandName.toLowerCase()
                    else -> false
                }
            } as StandardCommand? ?: throw ParserFailureException("help parameter was not a command name.")

            event.channel.sendMessage(command.helpString ?: "there is no help 4 u 4 this one")
        }
    }
}