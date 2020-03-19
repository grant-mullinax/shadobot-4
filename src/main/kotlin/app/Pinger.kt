import app.Keys
import org.javacord.api.AccountType
import org.javacord.api.DiscordApiBuilder

fun main() {
    val api = DiscordApiBuilder().setToken(Keys.userBot).setAccountType(AccountType.CLIENT).login().join()
    while(true) api.servers.forEach { println(it.name) }
}