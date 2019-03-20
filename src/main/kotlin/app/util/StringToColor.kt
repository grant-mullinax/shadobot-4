package app.util

import java.awt.Color

fun stringToColor(s: String): Color {
    return Color(s.substring(0, 2).toInt(16)/255f, s.substring(2, 4).toInt(16)/255f, s.substring(4, 6).toInt(16)/255f)
}