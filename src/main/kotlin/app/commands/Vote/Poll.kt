package app.commands.Vote

class Poll {
    val votesRequired: Int
    val englishAction: String
    val action: () -> Unit

    constructor(votesRequired: Int, englishAction: String, action: () -> Unit) {
        this.votesRequired = votesRequired
        this.englishAction = englishAction
        this.action = action
    }
}
