import kotlin.math.cos
import kotlin.math.sin

class Projection(private val point: Point, private val rx: Float, private val ry: Float) {

    // plane -> 0 = ((x-point.x, (y-point.y), (z-point.z)) dot product normal
    fun getNormal() : Point {
        return Point(sin(rx)*cos(ry), sin(rx)*sin(ry), cos(rx))
    }

}
