package app.science

import java.awt.image.BufferedImage
import java.time.LocalDateTime
import kotlin.reflect.KFunction

fun test(i: Int, s:String, bi: BufferedImage?) : Int {
    return i + s.length + (bi?.width ?: 0)
}

fun main() {
    val before = System.currentTimeMillis()
    println("b4: $before")
    for (i in 0..100000) {
        val a = ::test as KFunction<*>
        //a.
        val b = a.parameters.size
        test(b + i, "abc", null)
    }
    val after = System.currentTimeMillis()
    println("after: $after")
    println("diff: ${(after-before)/100000}")

    val before2 = System.currentTimeMillis()
    println("b4: $before")
    for (i in 0..100000) {
        test(i, "abc", null)
    }
    val after2 = System.currentTimeMillis()
    println("after: $after2")
    println("diff: ${(after2-before2)/100000}")
}