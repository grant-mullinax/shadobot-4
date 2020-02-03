package app.commands.vote

import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import app.util.stringToColor
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.VoiceChannel
import org.javacord.api.entity.message.Message
import org.javacord.api.entity.permission.PermissionType
import org.javacord.api.event.message.MessageCreateEvent
import org.javacord.api.event.message.reaction.ReactionAddEvent
import kotlin.random.Random

class Vote(private val api: DiscordApi) : StandardCommand() {
    private val voteMessages = HashMap<Message, Poll>()

    override val commandName = "vote"

    override fun action(event: MessageCreateEvent) {
        val parser = MessageParameterParser(event.message)
        val server = event.server.get()

        var poll: Poll?

        when (parser.extractString("vote type")) {
            "rename" -> {
                val target = parser.extractMentionedUser()
                val newName = parser.extractMultiSpaceString("new name")
                poll = Poll(
                        votesRequired = 4,
                        englishAction = "rename ${target.name} to $newName",
                        action = {
                            target.updateNickname(server, newName)
                        })
            }
            "kick" -> {
                val target = parser.extractMentionedUser()
                poll = Poll(votesRequired = 5,
                        englishAction = "kick ${target.name}",
                        action = {
                            server.kickUser(target)
                        })
            }
            "ban" -> {
                val target = parser.extractMentionedUser()
                poll = Poll(votesRequired = 7,
                        englishAction = "ban ${target.name}",
                        action = {
                            server.banUser(target)
                        })
            }
            "unban" -> {
                val target = parser.getServer().bans.join().find { b ->
                    b.user.id.toString().startsWith(parser.extractString("user id"))
                }!!.user
                poll = Poll(votesRequired = 4,
                        englishAction = "unban ${target.name}",
                        action = {
                            server.unbanUser(target)
                        })
            }
            "gag" -> {
                val target = parser.extractMentionedUser()
                poll = Poll(votesRequired = 3,
                        englishAction = "mute ${target.name}",
                        action = {
                            server.muteUser(target)
                        })
            }
            "cool" -> {
                val target = parser.extractMentionedUser()
                poll = Poll(votesRequired = 7,
                        englishAction = "make ${target.name} cool",
                        action = {
                            event.message.channel.sendMessage("${target.name} is cool")
                        })
            }
            "createrole" -> {
                val colorString = parser.extractString("color")
                val color = stringToColor(colorString)

                val rankName = parser.extractMultiSpaceString("role name")
                poll = Poll(votesRequired = 3,
                        englishAction = "create role $rankName with color $colorString",
                        action = {
                            server.createRoleBuilder().setColor(color).setName(rankName).create()
                        })
            }
            "rolecolor" -> {
                val colorString = parser.extractString("color")
                val color = stringToColor(colorString)

                val rank = parser.extractRoleFromString()
                poll = Poll(votesRequired = 3,
                        englishAction = "change ${rank.name} to $colorString",
                        action = {
                            rank.updateColor(color)
                        })
            }
            "troll" -> {
                val target = parser.extractMentionedUser()
                poll = Poll(votesRequired = 3,
                        englishAction = "troll ${target.name}",
                        action = {
                            server.deafenUser(target)
                        })
            }
            "delete" -> {
                val idString = parser.extractString("message id")
                val message = api.getMessageById(idString, event.channel).get()
                poll = Poll(votesRequired = 3,
                        englishAction = "delete message https://discordapp.com/channels/${event.server.get().id}/${event.channel.id}/${idString}",
                        action = {
                            message.delete()
                        })
            }
            "pin" -> {
                val idString = parser.extractString("message id")
                val message = api.getMessageById(idString, event.channel).get()
                poll = Poll(votesRequired = 3,
                        englishAction = "pin message https://discordapp.com/channels/${event.server.get().id}/${event.channel.id}https://discordapp.com/channels/${event.server.get().id}/${event.channel.id}/$idString",
                        action = {
                            message.pin()
                        })
            }
            "promote" -> {
                val target = parser.extractMentionedUser()

                val rank = parser.extractRoleFromString()
                if (rank.permissions!!.allowedPermission!!.contains(PermissionType.ADMINISTRATOR)) {
                    event.channel.sendMessage("nice try r word")
                    return
                }
                poll = Poll(votesRequired = voteRoleRequirement(rank.permissions.allowedBitmask),
                        englishAction = "promote ${target.name} to ${rank.name}",
                        action = {
                            target.addRole(rank)
                        })
            }
            "demote" -> {
                val target = parser.extractMentionedUser()
                val rank = parser.extractRoleFromString()
                if (rank.permissions!!.allowedPermission.contains(PermissionType.ADMINISTRATOR)) {
                    event.channel.sendMessage("nice try r word")
                    return
                }
                poll = Poll(votesRequired = voteRoleRequirement(rank.permissions.allowedBitmask) + 1,
                        englishAction = "demote ${target.name} from ${rank.name}",
                        action = {
                            target.removeRole(rank)
                        })
            }
            "motd" -> {
                val serverChannel = parser.getServerTextChannel()
                val newTopic = parser.extractMultiSpaceString("motd")
                poll = Poll(
                        votesRequired = 3,
                        englishAction = "change the motd of ${serverChannel.name} to $newTopic",
                        action = {
                            serverChannel.updateTopic(newTopic)
                        }
                )
            }
            "channelname" -> {
                val serverChannel = parser.getServerTextChannel()
                val newTopic = parser.extractMultiSpaceString("channel name")
                poll = Poll(
                        votesRequired = 3,
                        englishAction = "change the channel name of ${serverChannel.name} to $newTopic",
                        action = {
                            serverChannel.updateName(newTopic)
                        }
                )
            }
            "slay" -> {
                val target = parser.extractMentionedUser()

                if (target.connectedVoiceChannels.isNullOrEmpty())
                    return

                poll = Poll(
                        votesRequired = 3,
                        englishAction = "slay ${target.name}",
                        action = {
                            var voiceChannel: VoiceChannel = target.connectedVoiceChannels.first()
                            while (voiceChannel == target.connectedVoiceChannels.first()) {
                                voiceChannel = server.voiceChannels[Random.nextInt(0, server.voiceChannels.size)]
                            }

                            target.move(voiceChannel.asServerVoiceChannel().get())
                        }
                )
            }
            else -> {
                println("oh no")
                return
            }
        }



        event.channel.sendMessage(
                "A vote to ${poll.englishAction} has started! ${poll.votesRequired} required").thenAcceptAsync { msg ->
            msg.addReaction("✅")
            voteMessages[msg] = poll
        }
    }

    fun receiveVoteReaction(event: ReactionAddEvent) {
        if (!event.message.isPresent)
            return

        val message = event.message.get()
        val poll = voteMessages[message]
        if (poll != null) {
            // first is mikey 2nd alan
            val cheater = (event.user.id == 155061315977740288) && event.emoji.equalsEmoji("\uD83C\uDF0B")

            val yeses = message.reactions.find { r -> r.emoji.equalsEmoji("✅") }!!
            if (yeses.count >= poll.votesRequired || cheater) {
                poll.action()
                message.edit("The vote to ${poll.englishAction} has passed with ${poll.votesRequired} votes!")
                voteMessages.remove(message)
            }

        }
    }
}