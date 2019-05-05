package app.parsing.MetaParser

sealed class Parser()
data class IntParser(val default: Int?, val description: String, var value: Int? = null) : Parser()
data class StringParser(val default: String?, val description: String, var value: String? = null) : Parser()
data class FloatParser(val default: Float?, val description: String, var value: Float? = null) : Parser()