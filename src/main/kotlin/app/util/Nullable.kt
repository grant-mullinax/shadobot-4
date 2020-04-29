package app.util

import java.util.*

fun <T> Optional<T>.nullable(): T? {
    return this.orElseGet { null }
}