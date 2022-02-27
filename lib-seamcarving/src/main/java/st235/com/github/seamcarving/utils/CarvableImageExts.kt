package st235.com.github.seamcarving.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import st235.com.github.seamcarving.images.BitmapCarvableImage
import st235.com.github.seamcarving.images.CarvableImage

internal fun <T: CarvableImage> T.asBitmap(): Bitmap {
    if (this is BitmapCarvableImage) {
        return bitmap
    }

    val empty = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    for (i in 0 until height) {
        for (j in 0 until width) {
            empty.setPixel(j, i, getPixelAt(i, j))
        }
    }

    return empty
}
