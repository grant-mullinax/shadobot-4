package app.commands.trash

import app.commands.abstract.MessageProcess
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class TrollProp : MessageProcess {
    override fun qualifier(event: MessageCreateEvent): Boolean {
        return event.messageAuthor.id == 159489412550492161;
    }

    override fun action(event: MessageCreateEvent) {
        if ((1..5).random() == 1) event.channel.sendMessage("<:me:553304106433904652>")
    }
}
