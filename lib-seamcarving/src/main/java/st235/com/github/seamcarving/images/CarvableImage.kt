package st235.com.github.seamcarving.images

interface CarvableImage {

    val width: Int

    val height: Int

    fun getPixelAt(i: Int, j: Int): Int

}