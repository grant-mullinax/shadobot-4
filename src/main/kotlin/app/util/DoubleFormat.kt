package app.util

fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)