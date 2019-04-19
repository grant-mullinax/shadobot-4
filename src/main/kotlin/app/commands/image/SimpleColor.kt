package app.commands.image

import kotlin.math.abs

class SimpleColor {
    var alpha: Int
    var red: Int
    var green: Int
    var blue: Int

    constructor(argb: Int) {
        red = argb shr 16 and 0xFF
        green = argb shr 8 and 0xFF
        blue = argb and 0xFF
        alpha = 255
    }

    constructor(r: Int, g: Int, b: Int) {
        red = r
        green = g
        blue = b
        alpha = 255
    }

    constructor(r: Int, g: Int, b: Int, a: Int) {
        red = r
        green = g
        blue = b
        alpha = a
    }

    fun darkness(): Int {
        return (red + green + blue) / 3
    }

    fun darknessToAlpha(): SimpleColor {
        return SimpleColor(red, green, blue, darkness())
    }

    fun toInt(): Int {
        return (alpha.coerceIn(0, 255) shl 24) or (red.coerceIn(0, 255) shl 16) or maxOf(green.coerceIn(0, 255) shl 8, 0) or maxOf(blue.coerceIn(0, 255), 0)
    }

    fun abs(): SimpleColor {
        red = abs(red)
        green = abs(green)
        blue = abs(blue)
        return this
    }

    operator fun plus(other: SimpleColor): SimpleColor {
        return SimpleColor(red + other.red, green + other.green, blue + other.blue)
    }

    operator fun minus(other: SimpleColor): SimpleColor {
        return SimpleColor(red - other.red, green - other.green, blue - other.blue)
    }

    operator fun times(other: SimpleColor): SimpleColor {
        return SimpleColor(red * other.red, green * other.green, blue * other.blue)
    }

    operator fun div(other: SimpleColor): SimpleColor {
        return SimpleColor(red / other.red, green / other.green, blue / other.blue)
    }

    operator fun times(other: Int): SimpleColor {
        return SimpleColor(red * other, green * other, blue * other)
    }

    operator fun div(other: Int): SimpleColor {
        return SimpleColor(red / other, green / other, blue / other)
    }
}