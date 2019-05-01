package app.commands.abstract

import org.javacord.api.event.message.reaction.ReactionAddEvent

interface ReactionProcess {
    fun qualifier(event: ReactionAddEvent): Boolean
    fun action(event: ReactionAddEvent)
}