package slang

import app.parsing.ParserFailureException
import org.javacord.api.event.message.MessageCreateEvent
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

class SLangFunc {
    val kfn: KFunction<*>
    val name: String

    constructor(kfn: KFunction<*>, name: String = kfn.name) {
        this.kfn = kfn
        this.name = name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SLangFunc

        if (kfn != other.kfn) return false
        if (name != other.name) return false

        return true
    }
}

class SLangEnvironment {
    private val functions: Map<String, SLangFunc> = mutableMapOf()

    fun interpret(event: MessageCreateEvent): Any? {
        val splitInput = event.messageContent.split(' ', limit = 2)
        val slangFunc = functions[splitInput[0]] ?: throw ParserFailureException("Initial function does not exist")
        val remainingString = if (splitInput.size > 1) splitInput[1] else ""

        return slangFunc.kfn.callBy(
            mapOf(
                *slangFunc.kfn.parameters.map {
                    it to interpretParam(event, remainingString, it.type)
                }.toTypedArray()
            )
        )
    }

    private fun interpretParam(event: MessageCreateEvent, remainingString: String, type: KType): Any? {
        val splitInput = remainingString.split(' ', limit = 2)
        val remainingString = if (splitInput.size > 1) splitInput[1] else ""

        val slangFunc = functions[splitInput[0]] ?: throw ParserFailureException("Initial function does not exist")

        when (type.jvmErasure) {
            String::class -> if (parameterText.startsWith('"')) {
                val quoteRegex = "([\"'])(?:(?=(\\\\?))\\2.)*?\\1".toRegex()
                val parameterResult = quoteRegex.find(parameterText) ?: throw ParserFailureException("close quote on multi space string not supplied")
                val parameter = parameterResult.value.substring(1, parameterResult.value.length -1)

                parameterText = if (parameterResult.value.length == parameterText.length) {
                    ""
                } else {
                    parameterText.substring(parameterResult.value.length + 1, parameterText.length)
                }

                parameter

        }
    }
}

