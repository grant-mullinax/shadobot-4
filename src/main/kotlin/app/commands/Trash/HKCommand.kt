package app.commands.Trash

import app.commands.Abstract.StandardCommand
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent

class HKCommand: StandardCommand() {
    override val commandName = "punish"

    override fun action(event: MessageCreateEvent, api: DiscordApi) {
        if (!(event.messageAuthor.asUser().get().getRoles(event.server.get()).contains(api.getRoleById( 550546244788027412).get()) ||
            event.messageAuthor.asUser().get().getRoles(event.server.get()).contains(api.getRoleById( 549840418024587265).get()))){
            return
        }
        val target = event.message.mentionedUsers.first()
        val role = event.server.get().roles.find { r -> r.name == "n-word sayer" }
        target.addRole(role)
        target.removeRole(api.getRoleById( 555490458948730897).get())

        event.channel.sendMessage("${target.name} has been punished :japanese_goblin:")
    }
}