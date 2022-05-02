package st235.com.github.seamcarving.images

import android.graphics.Color

internal class ByteArrayCarvableImage(
    override val width: Int,
    override val height: Int,
    private val image: ByteArray,
    override val maskMatrix: Array<IntArray>?
) : CarvableImage() {

    init {
        if (maskMatrix != null &&
            (maskMatrix.size != height ||
                    maskMatrix[0].size != width)
        ) {
            throw IllegalStateException(
                "Mask matrix should have the same dimensions as" +
                        " the original image!"
            )
        }
    }

    override val isMasked: Boolean
        get() = maskMatrix != null

    override fun getMaskPixel(i: Int, j: Int): Int? {
        return maskMatrix?.get(i)?.get(j)
    }

    override fun getPixelAt(i: Int, j: Int): Int {
        val index = i * width + j

        //r
        val r = image[index * 4 + 0].toUByte().toInt()
        //g
        val g = image[index * 4 + 1].toUByte().toInt()
        //b
        val b = image[index * 4 + 2].toUByte().toInt()
        //a
        val a = image[index * 4 + 3].toUByte().toInt()

        return Color.argb(a, r, g, b)
    }
}