package app.parsing

import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.MessageAttachment
import org.javacord.api.entity.message.MessageAuthor
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import java.awt.image.BufferedImage
import java.lang.NumberFormatException
import java.util.*

class ParserFailureException(message:String): Exception(message)

class MessageParameterParser {
    private val author: MessageAuthor
    private val fullText: String
    private var parameterText: String
    private val attachments: LinkedList<MessageAttachment>
    private val channel: TextChannel
    private val server: Server?
    private var mentionedUsers: LinkedList<User>

    constructor(message: Message) {
        fullText = message.content
        val splitText = message.content.split(' ', limit =  2)
        parameterText = if (splitText.size > 1) splitText[1] else ""
        author = message.author
        attachments = LinkedList(message.attachments)
        channel = message.channel
        server = message.server.orElseGet { null }
        mentionedUsers = LinkedList(message.mentionedUsers)
    }

    private fun extractNullableString() : String? {
        if (parameterText == "")
            return null

        val splitText = parameterText.split(' ', limit = 2)
        parameterText = if (splitText.size == 1) {
            ""
        } else {
            splitText[1]
        }

        return splitText[0]
    }

    fun extractString(default: String? = null) : String {
        return extractNullableString() ?: default ?: throw ParserFailureException("Parameter not supplied")
    }

    fun extractMultiSpaceString(default: String? = null) : String {
        if (parameterText == "") default ?: throw ParserFailureException("Parameter not supplied")
        val parameter = parameterText
        parameterText = ""
        return parameter
    }

    private fun <T : Any> extractGeneric(f: (String) -> T, default: T?, typeName: String) : T {
        val text = extractNullableString()

        try {
            if (text == null) {
                if (default != null) {
                    return default
                } else {
                    throw ParserFailureException("Failed to supply $typeName")
                }
            }
            return f(text)
        } catch (ex: NumberFormatException) {
            throw ParserFailureException("Failed to parse $typeName from $text")
        }
    }

    fun extractInt(default: Int? = null) : Int {
        return extractGeneric(String::toInt, default,"integer")
    }

    fun extractFloat(default: Float? = null) : Float {
        return extractGeneric(String::toFloat, default, "decimal")
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

    fun getAuthorAsUser() : User {
        return author.asUser().orElseThrow { ParserFailureException("Author was not user") }
    }

    fun extractImage() : BufferedImage {
        if (attachments.size == 0) throw ParserFailureException("Failed to parse image")
        return attachments.remove().downloadAsImage().join()
    }

    fun extractImageAndLookUpward() : BufferedImage {
        // todo could be probably made a bit more efficient
        channel.getMessages(6).join().reversed().forEach { m ->
            if (m.attachments.size > 0) {
                return m.attachments.first().downloadAsImage().join()
            }
        }

        throw ParserFailureException("Failed to parse image")
    }

    fun getServer() : Server {
        return server ?: throw ParserFailureException("Message was not sent in server")
    }

    fun getServerTextChannel() : ServerTextChannel {
        return channel.asServerTextChannel().orElseThrow { ParserFailureException("Message was not sent in server") }
    }
}