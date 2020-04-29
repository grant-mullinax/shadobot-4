package app.commands.Pushbox

import app.parsing.ParserFailureException
import java.lang.Exception

enum class Tile {
    EMPTY, WALL, BOX, END, END_OCCUPIED, PLAYER
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

class PushboxGame {
    var state: List<List<Tile>>

    constructor(discordText: String) {
        state = discordText
            .substringAfter("```").substringBefore("```")
            .split("\n")
            .map { row ->
                row.map { c ->
                    when (c) {
                        '#' -> Tile.WALL
                        ' ' -> Tile.EMPTY
                        'O' -> Tile.BOX
                        'X' -> Tile.END
                        'Q' -> Tile.END_OCCUPIED
                        'P' -> Tile.PLAYER
                        else -> {
                            throw ParserFailureException("Failed to parse box game from game string")
                        }
                    }
                }
            }
    }

    fun move(direction: Direction) {
        val players = mutableListOf<Pair<Int, Int>>()
        for ((x, row) in state.withIndex()) {
            for ((y, tile) in row.withIndex()) {
                if (tile == Tile.PLAYER || tile == Tile.END_OCCUPIED) {
                    players.add(Pair(x,y))
                }
            }
        }
    }
}