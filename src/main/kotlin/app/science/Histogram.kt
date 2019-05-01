package app.science

import java.io.File
import java.lang.Math.min
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun main(args: Array<String>) {
    val blocked = setOf("\uD83D\uDD2A", "", "i", "to", "the", "a", "u", "is", "it", "and", "me", "my", "like", "that",
        "you", "in", "just", "not", "of", "im", "I", "this", "what", "for", "but", "so", "at", "i", "to", "the", "a",
        "u", "is", "it", "and", "me", "my", "like", "that", "you", "in", "just", "not", "of", "im", "I", "this", "what",
        "for", "but", "so", "at", "no", "have", "be", "on", "do", "are", "with", "was", "ur", "if", "he", "ok", "dont",
        "yeah", "get", "can", "want", "up", "its", "hi", "they", "one", "how", "about", "when", "all", "we", "will",
        "would", "did", "hey", "oh", "your", "too", "she", "or", "girl", "don't", "has", "now", "go", "am", "her",
        "guys", "it's", "got", "really", "gonna", "actually", "some", "who")

    val histograms = hashMapOf<String, HashMap<String, Int>>()

    File("path.txt").forEachLine { line ->
        val split = line.split(" ")
        val user = split.first()
        if (!histograms.containsKey(user)){
            histograms[user] = hashMapOf()
        }

        split.subList(1, split.size).forEach { word ->
            if (blocked.contains(word)) return@forEach
            val userHistogram = histograms[user]!!
            if (!userHistogram.containsKey(word)){
                userHistogram[word] = 0
            }
            userHistogram[word] = userHistogram[word]!! + 1
        }
    }

    histograms.entries.forEach {
        val histogram = it.value
        println("user ${it.key}:")
        val results = histogram.entries.toList().sortedByDescending { e -> e.value }
        results.subList(0, min(results.size, 10)).forEach { wordCounts ->
            println("${wordCounts.key}: ${wordCounts.value}")
        }
        print("\n")
    }
}