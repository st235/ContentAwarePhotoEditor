package st235.com.github.seamcarving.images

abstract class CarvableImage {

    abstract val width: Int

    abstract val height: Int

    abstract val isMasked: Boolean

    abstract fun getMaskPixel(i: Int, j: Int): Int?

    abstract fun getPixelAt(i: Int, j: Int): Int

}