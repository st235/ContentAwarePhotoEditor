package st235.com.github.seamcarving.utils

import android.graphics.Color
import st235.com.github.seamcarving.images.CarvableImage

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

