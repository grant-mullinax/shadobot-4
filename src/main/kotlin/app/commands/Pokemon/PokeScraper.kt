package app.commands.Pokemon

import app.Keys
import app.commands.abstract.MessageProcess
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager
import kotlinx.coroutines.channels.consumesAll
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.core.util.FileContainer
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

class PokeScraper : MessageProcess {
    private val file = File("pokemap2.json")
    private val nameRegex = Regex("(?<=Base stats for )(.*)(?= #)")

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageAuthor.id == 365975655608745985 &&
                event.message.embeds.size > 0 &&
                event.message.embeds.first().title.isPresent &&
                event.message.embeds.first().title.get().startsWith("Base stats for")
    }

    override fun action(event: MessageCreateEvent) {
        val data = FileContainer(event.message.embeds.first().image.get().url).asByteArray(event.api).join()
        val md = MessageDigest.getInstance("MD5")
        val hash = BigInteger(1, md.digest(data)).toString(16).padStart(32, '0')
        val name = nameRegex.find(event.message.embeds.first().title.get())!!.value
        file.appendText("\"$hash\": \"$name\",\n")
    }
}
