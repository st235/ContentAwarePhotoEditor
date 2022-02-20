package st235.com.github.seamcarving.images

internal class ArrayCarvableImage(
    private val image: Array<IntArray>,
    private val maskMatrix: Array<IntArray>?
) : CarvableImage() {

    init {
        if (maskMatrix != null &&
            (maskMatrix.size != image.size ||
                    maskMatrix[0].size != image[0].size)
        ) {
            throw IllegalStateException(
                "Mask matrix should have the same dimensions as" +
                        " the original image!"
            )
        }
    }

    override val width: Int
        get() = image[0].size

    override val height: Int
        get() = image.size

    override val isMasked: Boolean
        get() = maskMatrix != null

    override fun getMaskPixel(i: Int, j: Int): Int? {
        return maskMatrix?.get(i)?.get(j)
    }

    override fun getPixelAt(i: Int, j: Int): Int {
        return image[i][j]
    }
}
