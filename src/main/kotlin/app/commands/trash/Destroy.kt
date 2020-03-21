package app.commands.trash

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.javacord.api.DiscordApi
import org.javacord.api.entity.permission.Role
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.random.Random

class Clone(val api: DiscordApi) : StandardCommand() {
    override val commandName = "clone"
    private var dupeServer = 0L
    private val peopleRoles = mutableMapOf<Long, List<Long>>()
    private val peopleNick = mutableMapOf<Long, String>()
    private val roleTranslation = mutableMapOf<Long, Role>()

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)

        val id = parser.extractLong("server id")

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
}