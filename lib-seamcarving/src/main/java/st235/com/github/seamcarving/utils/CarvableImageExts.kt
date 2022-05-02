package st235.com.github.seamcarving.utils

import android.graphics.Bitmap
import st235.com.github.seamcarving.SeamCarver
import st235.com.github.seamcarving.images.CarvableImage

internal fun <T: CarvableImage> T.asCarvingResult(): SeamCarver.CarvingResult {
    val resultingImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    for (i in 0 until height) {
        for (j in 0 until width) {
            resultingImage.setPixel(j, i, getPixelAt(i, j))
        }
    }

    return SeamCarver.CarvingResult(resultingImage, maskMatrix)
}
