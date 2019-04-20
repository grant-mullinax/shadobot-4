package app.parsing

import org.javacord.api.entity.channel.ServerTextChannel
import org.javacord.api.entity.channel.TextChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.message.MessageAttachment
import org.javacord.api.entity.message.MessageAuthor
import org.javacord.api.entity.permission.Role
import org.javacord.api.entity.server.Server
import org.javacord.api.entity.user.User
import java.awt.image.BufferedImage
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

    fun extractString(description: String, default: String? = null) : String {
        return extractNullableString() ?: default ?: throw ParserFailureException("$description not supplied")
    }

    fun extractMultiSpaceString(description: String, default: String? = null) : String {
        if (parameterText == "") default ?: throw ParserFailureException("$description not supplied")
        val parameter = parameterText
        parameterText = ""
        return parameter
    }

    private fun <T : Any> extractGeneric(f: (String) -> T, default: T?, description: String, typeName: String) : T {
        val text = extractNullableString()

        try {
            if (text == null) {
                if (default != null) {
                    return default
                } else {
                    throw ParserFailureException("Failed to supply $description")
                }
            }
            return f(text)
        } catch (ex: NumberFormatException) {
            throw ParserFailureException("Failed to parse $typeName $description from $text")
        }
    }

    fun extractInt(description: String, default: Int? = null) : Int {
        return extractGeneric(String::toInt, default, description,"integer")
    }

    fun extractFloat(description: String, default: Float? = null) : Float {
        return extractGeneric(String::toFloat, default, description, "decimal")
    }

    fun extractDouble(description: String, default: Double? = null) : Double {
        return extractGeneric(String::toDouble, default, description, "decimal (double)")
    }

    fun extractMentionedUser(defaultToAuthor: Boolean = false) : User {
        if (mentionedUsers.size == 0) {
            if (defaultToAuthor) {
                return author.asUser().get()
            } else {
                throw ParserFailureException("Failed to parse mention")
            }
        }
        extractString("user mention")
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
        channel.getMessages(10).join().reversed().forEach { m ->
            if (m.attachments.size > 0) {
                return m.attachments.first().downloadAsImage().join()
            }
        }

        throw ParserFailureException("Failed to parse image")
    }

    fun extractRoleFromString() : Role {
        val roleName = extractMultiSpaceString("role name")
        return (server ?: throw ParserFailureException("Message was not sent in server"))
            .roles.find { r -> r.name == roleName } ?: throw ParserFailureException("Could not find role $roleName")
    }

    fun getServer() : Server {
        return server ?: throw ParserFailureException("Message was not sent in server")
    }

    fun getServerTextChannel() : ServerTextChannel {
        return channel.asServerTextChannel().orElseThrow { ParserFailureException("Message was not sent in server") }
    }
}