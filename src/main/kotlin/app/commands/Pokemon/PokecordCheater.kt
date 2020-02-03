package app.commands.Pokemon

import app.Keys
import app.commands.abstract.MessageProcess
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.core.util.FileContainer
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

class PokecordCheater : MessageProcess {
    private val pokemonMap: HashMap<String, String>

    init {
        val mapConverter = object: Converter {
            override fun fromJson(jv: JsonValue): HashMap<String, Any?> = HashMap(jv.obj!!)
            override fun canConvert(cls: Class<*>): Boolean {
                return true
            }
            override fun toJson(value: Any): String {
                return ""
            }
        }

        pokemonMap = Klaxon()
            .converter(mapConverter)
            .parse(File("pokemap.json").readText(Charset.defaultCharset()))!!
    }

    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageAuthor.id == 365975655608745985 &&
                event.message.embeds.size > 0 &&
                event.message.embeds.first().description.get() == "Guess the pokémon and type /catch <pokémon> to catch it!"
    }

    override fun action(event: MessageCreateEvent) {
        val data = FileContainer(event.message.embeds.first().image.get().url).asByteArray(event.api).join()
        val md = MessageDigest.getInstance("MD5")
        val hash = BigInteger(1, md.digest(data)).toString(16).padStart(32, '0')
        event.channel.sendMessage("a ${pokemonMap[hash]} appeared")
    }
}
