package st235.com.github.seamcarving.images

import android.graphics.Bitmap

class BitmapCarvableImage(
    internal val bitmap: Bitmap,
    private val maskMatrix: Array<IntArray>?
): CarvableImage() {

    override val width: Int
        get() = bitmap.width

    override val height: Int
        get() = bitmap.height

    override val isMasked: Boolean
        get() = maskMatrix != null

    override fun getMaskPixel(i: Int, j: Int): Int? {
        return maskMatrix?.get(i)?.get(j)
    }

    override fun getPixelAt(i: Int, j: Int): Int {
        return bitmap.getPixel(j, i)
    }

}