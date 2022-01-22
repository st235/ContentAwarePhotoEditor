package st235.com.github.seamcarving.images

import android.graphics.Bitmap

class BitmapCarvableImage(
    private val bitmap: Bitmap
): CarvableImage {

    override val width: Int
        get() = bitmap.width

    override val height: Int
        get() = bitmap.height

    override fun getPixelAt(i: Int, j: Int): Int {
        return bitmap.getPixel(j, i)
    }

}