package app.commands

import app.Keys
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.parsing.ParserFailureException
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImages.BingImagesSearchDefinitionStages.WithExecute
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.SafeSearch
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.IIOException
import javax.imageio.ImageIO


class Img : StandardCommand() {
    private val client = BingImageSearchManager.authenticate(Keys.bingImages)
    override val commandName = "img"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        val parser = MessageParameterParser(event.message)
        val search = parser.extractMultiSpaceString("search")
        val query = client.bingImages().search()
                .withQuery(search)
                .withMarket("en-us")
                .withSafeSearch(if (event.channel.asServerTextChannel().get().isNsfw) SafeSearch.OFF else SafeSearch.MODERATE)
                .withCount(1)
        val image = searchWithRetry(query, 0) ?: throw ParserFailureException("query failed for some reason")

        val message = MessageBuilder()
        message.addAttachment(image, "result.png")

        message.send(event.channel)
    }

    private fun searchWithRetry(query: WithExecute, retry: Long): BufferedImage? {
        if (retry > 2)
            return null
        val imageResults = query.execute()

        val firstResult = imageResults.value().first()
        val urlText = firstResult.contentUrl()
        val url = URL(urlText)
        return try {
            ImageIO.read(url)
        } catch (ex: IIOException) {
            searchWithRetry(query.withOffset(retry + 1), retry + 1)
        }
    }
}