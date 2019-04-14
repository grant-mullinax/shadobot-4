package app.commands

import app.commands.Abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.net.URL
import javax.imageio.ImageIO

class Avatar: StandardCommand() {
    override val commandName = "avatar"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val url = URL("${parser.extractMentionedUser(true).avatar.url}?size=512")

        val message = MessageBuilder()
        message.addAttachment(url)
        message.send(event.channel)
    }
}