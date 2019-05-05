package app.commands

import app.commands.abstract.StandardCommand
import app.parsing.MetaParser.FloatParser
import app.parsing.MetaParser.IntParser
import app.parsing.MetaParser.StringParser
import app.parsing.MetaParser.metaParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class MetaParserTestCommand : StandardCommand() {
    override val commandName = "mpt"
    private val countParser = IntParser(2, "count")
    private val nameParser = StringParser("default", "name")
    private val scaleParser = FloatParser(.5f, "scale")
    val parameters = arrayOf(countParser, nameParser, scaleParser)

    override fun action(event: MessageCreateEvent) {
        metaParser(event.message, parameters)
        event.channel.sendMessage(
            "your params were ${countParser.value} ${nameParser.value} ${scaleParser.value}"
        )
    }
}