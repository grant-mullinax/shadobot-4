package app.commands

import app.commands.abstract.AdminCommand
import app.commands.abstract.StandardCommand
import app.parsing.MessageParameterParser
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.util.*

class Bf : AdminCommand() {
    override val commandName = "bf"

    override fun action(event: MessageCreateEvent) {
        val size = 10000
        val executionLimit = 1000000

        val parser = MessageParameterParser(event.message)
        val parameter = parser.extractMultiSpaceString("Brainfuck program").split("|")
        val program = parameter[0]
        val programInput = if (parameter.size > 1) parameter[1] else ""
        var programInputPtr = 0

        var cmdIdx = 0
        var ptr = 0
        val memory = Array(size) { 0 }
        val loopStartIdxs = Stack<Int>()
        var ignore = 0

        var executions = 0

        var out = ""

        while (cmdIdx < program.length) {
            executions++
            if (ignore == 0) {
                when (program[cmdIdx]) {
                    '>' -> ptr++
                    '<' -> ptr--
                    '+' -> memory[ptr] = (memory[ptr] + 1) % 256
                    '-' -> memory[ptr] = (memory[ptr] - 1) % 256
                    '.' -> out += memory[ptr].toChar()
                    ',' -> {
                        memory[ptr] = if (programInputPtr < programInput.length)
                            programInput[programInputPtr++].toInt()
                        else
                            0
                    }
                    '[' -> {
                        if (memory[ptr] == 0) {
                            ignore = 1
                        } else {
                            loopStartIdxs.push(cmdIdx - 1)
                        }
                    }
                    ']' -> {
                        if (memory[ptr] != 0) {
                            cmdIdx = loopStartIdxs.pop()
                        } else {
                            loopStartIdxs.pop()
                        }
                    }
                }
            } else {
                when (program[cmdIdx]) {
                    '[' -> {
                        ignore++
                    }
                    ']' -> {
                        ignore--
                    }
                }
            }

            cmdIdx++

            if (ptr < 0 || ptr >= size) {
                println(ptr)
                event.channel.sendMessage("bleh:\n$out")
                event.channel.sendMessage("data ptr went out of range")
                return
            }

            if (executions > executionLimit) {
                event.channel.sendMessage("bleh:\n$out")
                event.channel.sendMessage("program looped too hard")
                return
            }
        }

        event.channel.sendMessage(out)
    }
}