package app.parsing.MetaParser

import app.parsing.MessageParameterParser
import org.javacord.api.entity.message.Message

fun metaParser(message: Message, parsers: Array<Parser>) {
    // im just piggybacking off of the parser i already made for this functionality
    val originalParser = MessageParameterParser(message)
    for (parser in parsers) {
        when(parser) {
            is IntParser -> parser.value = originalParser.extractInt(parser.description, parser.default)
            is StringParser -> parser.value = originalParser.extractString(parser.description, parser.default)
            is FloatParser -> parser.value = originalParser.extractFloat(parser.description, parser.default)
        }
    }
}