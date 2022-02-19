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

internal fun CarvableImage.isValid(i: Int, j: Int): Boolean {
    return i >= 0 && i < height && j >= 0 && j < width
}

internal fun CarvableImage.redAt(x: Int, y: Int): Int {
    return Color.red(getPixelAt(x, y))
}

internal fun CarvableImage.greenAt(x: Int, y: Int): Int {
    return Color.green(getPixelAt(x, y))
}

internal fun CarvableImage.blueAt(x: Int, y: Int): Int {
    return Color.blue(getPixelAt(x, y))
}

