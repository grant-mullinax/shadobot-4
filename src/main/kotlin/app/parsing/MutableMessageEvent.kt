package app.parsing

import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.MessageAttachment
import org.javacord.api.entity.message.MessageAuthor
import org.javacord.api.entity.user.User
import java.lang.NumberFormatException
import java.util.*

class ParserFailureException(message:String): Exception(message)

class MessageParameterParser {
    private val author: MessageAuthor
    private val fullText: String
    private var parameterText: String
    private val attachments: MutableList<MessageAttachment>
    private val channel: TextChannel
    private var mentionedUsers: LinkedList<User>

    constructor(message: Message) {
        fullText = message.content
        parameterText = message.content.split(' ', limit =  2).last()
        author = message.author
        attachments = message.attachments
        channel = message.channel
        mentionedUsers = LinkedList(message.mentionedUsers)
    }

    private fun extractNullableString(limit: Int) : String? {
        val splitText = parameterText.split(' ', limit = limit)
        if (splitText.size < 2)
            return null
        parameterText = splitText[1]
        return splitText[0]
    }

    fun extractString() : String {
        return extractNullableString(2) ?: throw ParserFailureException("Parameter not supplied")
    }

    fun extractMultiSpaceString() : String {
        return extractNullableString(3) ?: throw ParserFailureException("Parameter not supplied")
    }

    private fun <T : Any> extractGeneric(f: (String) -> T, typeName: String) : T {
        val text = extractString()

        try {
            return f(text)
        } catch (ex: NumberFormatException) {
            throw ParserFailureException("Failed to parse $typeName from $text")
        }
    }

    fun extractInt() : Int {
        return extractGeneric(String::toInt, "integer")
    }

    fun extractFloat() : Float {
        return extractGeneric(String::toFloat, "decimal")
    }

    fun extractMentionedUser(defaultToAuthor: Boolean = false) : User {
        if (mentionedUsers.size == 0) {
            if (defaultToAuthor) {
                return author.asUser().get()
            } else {
                throw ParserFailureException("Failed to parse mention")
            }
        }
        return mentionedUsers.remove()
    }
}