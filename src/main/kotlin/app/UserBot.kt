import app.Keys
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.javacord.api.AccountType
import org.javacord.api.DiscordApiBuilder
import org.javacord.core.util.FileContainer
import java.io.File
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

fun main() {
    val api = DiscordApiBuilder().setToken(Keys.userBot).setAccountType(AccountType.CLIENT).login().join()
    val prefixRegex = Regex("(?<=Guess the pokémon and type )(.*)(?=catch <pokémon> to catch it!)")
    val happy = listOf("yeah", "heck yeah", ":))", "woooooo", "yaya", "yay", "hooray", ":)", "awesome", "love this guy", "!!!!", "YOINK")
    val sad = listOf("DANGIT", "dang", "damn", "NOOOOOOOO", ":(((", ":(", "heck", "too slow :(", "grrr", "nice", "nice catch", "wow cool", "rip", "ripp")

    val pinged = listOf("no thanks", "blocked", "im busy", "shhhh im playin pokemon", "no thank you im busy",
        "please do not bother me", "im bzy", "sorry cant talk", "srry cant type nty", "no", "please dont ping me ok?")
    val legends = setOf("arceus", "articuno", "azelf", "celebi", "cobalion", "cosmoem", "cosmog", "cresselia",
        "darkrai", "deoxys", "dialga", "diancie", "entei", "genesect", "giratina", "groudon",
        "heatran", "ho-oh", "hoopa", "jirachi", "keldeo", "kyogre", "kyurem", "landorus",
        "latias", "latios", "lugia", "lunala", "magearna", "manaphy", "marshadow", "meloetta",
        "mesprit", "mew", "mewtwo", "moltres", "necrozma", "palkia", "phione", "raikou",
        "rayquaza", "regice", "regigigas", "regirock", "registeel", "reshiram", "shaymin", "silvally",
        "solgaleo", "suicune", "tapu bulu", "tapu fini", "tapu koko", "tapu lele", "terrakion", "thundurus",
        "tornadus", "type: null", "uxie", "victini", "virizion", "volcanion", "xerneas", "yveltal",
        "zapdos", "zekrom", "zeraora", "zygarde", "detective pikachu")

    println("selfbot ready")
    val rand = java.util.Random()

    val mapConverter = object: Converter {
        override fun fromJson(jv: JsonValue): HashMap<String, Any?> = HashMap(jv.obj!!)
        override fun canConvert(cls: Class<*>): Boolean {
            return true
        }
        override fun toJson(value: Any): String {
            return ""
        }
    }

    val pokemonMap: HashMap<String, String> = Klaxon()
        .converter(mapConverter)
        .parse(File("pokemap.json").readText(Charset.defaultCharset()))!!

    var lock = false

    api.addMessageCreateListener { event ->
        if (event.messageContent.contains("auto")) {
            println("said auto in https://discordapp.com/channels/${event.server.get().id}/${event.channel.id}")
        }

        if (event.messageAuthor.id != 365975655608745985 || lock)
            return@addMessageCreateListener

        // if (pokeScraper.qualifier(event))
        //     pokeScraper.action(event)

        if (event.messageContent.contains("You caught a level")) {
            val isSelf = event.message.mentionedUsers.first().id == 524347945282437161
            println(isSelf)
            if ((1..4).random() == 1) {
                if (!isSelf) {
                    GlobalScope.launch {
                        delay((rand.nextGaussian()*100+25).toLong())
                        val typing = event.channel.typeContinuously()
                        delay((rand.nextGaussian()*110+2723).toLong())
                        event.channel.sendMessage(sad.random())
                        typing.close()
                    }
                } else {
                    if ((1..4).random() != 1) {
                        GlobalScope.launch {
                            delay((rand.nextGaussian()*100+25).toLong())
                            val typing = event.channel.typeContinuously()
                            delay((rand.nextGaussian()*110+2523).toLong())
                            event.channel.sendMessage(happy.random())
                            typing.close()
                        }
                    }
                }

                return@addMessageCreateListener
            }
        }

        if (!(event.message.embeds.size > 0 &&
                    event.message.embeds.first().description.isPresent &&
                    event.message.embeds.first().image.isPresent))
            return@addMessageCreateListener

        val desc = event.message.embeds.first().description.get()
        val prefix = prefixRegex.find(desc) ?: return@addMessageCreateListener

        val data = FileContainer(event.message.embeds.first().image.get().url).asByteArray(event.api).join()
        val md = MessageDigest.getInstance("MD5")
        val hash = BigInteger(1, md.digest(data)).toString(16).padStart(32, '0')

        val pokemon = pokemonMap[hash] ?: return@addMessageCreateListener

        if (legends.contains(pokemon.toLowerCase())) {
            GlobalScope.launch {
                lock = true
                delay((rand.nextGaussian()*25+555).toLong())
                event.channel.sendMessage("${prefix.value}catch ${pokemon.toLowerCase()}")
                print("in https://discordapp.com/channels/${event.server.get().id}/${event.channel.id} caught LEGENDARY $pokemon")
                delay((rand.nextGaussian()*203+4065).toLong())
                event.channel.sendMessage(happy.random())
                delay((rand.nextGaussian()*203+3065).toLong())
                event.channel.sendMessage("${prefix.value}info latest")
                lock = false
            }
        } else {
            if ((1..7).random() <= 3)
                return@addMessageCreateListener

            val typing = event.channel.typeContinuously()
            GlobalScope.launch {
                delay((rand.nextGaussian()*25+255).toLong())
                val delay = ((rand.nextGaussian() * 210) + 3703).toLong() + if ((1..3).random() == 1) 2000L else 0
                print("in https://discordapp.com/channels/${event.server.get().id}/${event.channel.id} caught $pokemon with delay $delay ")
                delay(delay)
                event.channel.sendMessage("${prefix.value}catch ${pokemon.toLowerCase()}")
                typing.close()
            }
        }
    }
}