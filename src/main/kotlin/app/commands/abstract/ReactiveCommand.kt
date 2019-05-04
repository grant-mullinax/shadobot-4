package app.commands.abstract

import org.javacord.api.event.message.reaction.ReactionAddEvent

abstract class ReactiveCommand : StandardCommand(), ReactionProcess {

    override fun qualifier(event: ReactionAddEvent): Boolean {
        return true
    }

    override fun action(event: ReactionAddEvent) {

    }
}