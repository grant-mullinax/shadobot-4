package app.science

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.sin
import javax.imageio.ImageIO



fun main() {
    val image = ImageIO.read(File("testimg.png"))
    val rotation = Math.PI

    val transform = AffineTransform()
    val width = image.width.toDouble()
    val height = image.height.toDouble()

    transform.translate(
        sin(rotation) * height, //cos(rotation) * width + sin(rotation) * height,
        0.0 // + sin(rotation) * width
    )
    transform.rotate(rotation, width, height)
    // transform.shear(1.0, 0.0)
    val op = AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)

    val rotatedImage = BufferedImage(image.width * 2, image.height * 5, image.type)
    op.filter(image, rotatedImage)

    val outputFile = File("out.png")
    ImageIO.write(rotatedImage, "png", outputFile)
}