package st235.com.github.seamcarving.images

class ArrayCarvableImage(
    private val image: Array<IntArray>
): CarvableImage {

    override val width: Int
        get() = image[0].size

    override val height: Int
        get() = image.size

    override fun getPixelAt(i: Int, j: Int): Int {
        return image[i][j]
    }

}