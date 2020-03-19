package app

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.javacord.api.AccountType
import org.javacord.api.DiscordApiBuilder
import java.time.Instant
import kotlin.random.Random

fun main() {
    val api = DiscordApiBuilder().setToken("Njg4MTczMjAyNjk4MDEwNjc2.XmwfKQ.lcX5xsWMpYRw4YBnZ7tGACazu9A").login().join()
    val me = DiscordApiBuilder().setAccountType(AccountType.CLIENT).setToken("MTU1MDYxMzE1OTc3NzQwMjg4.Xmwcug.kbI9SR3rLdUo-iKUfljs36GLYWY").login().join()

    val server = me.getServerById(291927855317778433).get()
    val dupe = api.getServerById(688201868165251105).get()

    dupe.updateIcon(server.icon.get().url)
    dupe.updateName(server.name)

    server.voiceChannels.filter { !it.category.isPresent && !it.userLimit.isPresent }.forEach { vc -> dupe.createVoiceChannelBuilder().setName(vc.name).create() }
    server.voiceChannels.filter { !it.category.isPresent && it.userLimit.isPresent }.forEach { vc -> dupe.createVoiceChannelBuilder().setUserlimit(vc.userLimit.get()).setName(vc.name).create() }
    server.textChannels.filter { !it.category.isPresent }.forEach { vc -> dupe.createTextChannelBuilder().setName(vc.name).create() }

    server.channelCategories.forEach { cat ->
        val dupeCat = dupe.createChannelCategoryBuilder().setName(cat.name).create().get()
        cat.channels.forEach {
            it.asServerTextChannel().ifPresent { dupe.createTextChannelBuilder().setName(it.name).setCategory(dupeCat).create() }
            it.asServerVoiceChannel().filter { !it.userLimit.isPresent }.ifPresent { dupe.createVoiceChannelBuilder().setName(it.name).setCategory(dupeCat).create() }
            it.asServerVoiceChannel().filter { it.userLimit.isPresent }.ifPresent { dupe.createVoiceChannelBuilder().setName(it.name).setUserlimit(it.userLimit.get()).setCategory(dupeCat).create() }
        }
    }

    println("---------- BOT ONLINE ----------")
}