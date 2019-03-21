package app

import org.tensorflow.Graph
import org.tensorflow.Tensor
import java.io.File


fun main() {
    val graph = Graph()

    val file = File("path.txt")
    val intLines = file.readLines().map { l -> l.map { c -> c.toInt() }.toCollection(mutableListOf()) }
    Tensor.create(intLines)
}