package app.commands

import app.Keys
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.SafeSearch
import org.javacord.api.entity.message.MessageBuilder
import java.net.URL
import javax.imageio.ImageIO


class Img: StandardCommand() {
    private val client = BingImageSearchManager.authenticate(Keys.bingImages)
    override val commandName = "img"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val search = parser.extractMultiSpaceString("search")
        val imageResults = client.bingImages().search()
            .withQuery(search)
            .withMarket("en-us")
            .withSafeSearch(if (event.channel.asServerTextChannel().get().isNsfw) SafeSearch.OFF else SafeSearch.MODERATE)
            .withCount(1)
            .execute()

        val firstResult = imageResults.value().first()
        val urlText = firstResult.contentUrl()
        val url = URL(urlText)
        val image = ImageIO.read(url)

        val message = MessageBuilder()
        message.addAttachment(image, "result.${firstResult.encodingFormat()}")

        message.send(event.channel)
    }
}