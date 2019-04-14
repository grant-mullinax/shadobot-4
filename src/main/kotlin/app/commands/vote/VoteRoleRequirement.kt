package app.commands.vote

import kotlin.math.roundToInt

fun voteRoleRequirement(bitmask: Int): Int {
    return kotlin.math.log(bitmask.toDouble(), 1.5).roundToInt() - 43
}