package st235.com.github.seamcarving.utils

import android.graphics.Bitmap
import java.nio.ByteBuffer
import st235.com.github.seamcarving.images.ByteArrayCarvableImage
import st235.com.github.seamcarving.images.CarvableImage

internal fun Bitmap.toCarvableImage(maskMatrix: Array<IntArray>?): CarvableImage {
    val buffer = ByteBuffer.allocate(byteCount)
    copyPixelsToBuffer(buffer)
    val byteArray = buffer.array()

    return ByteArrayCarvableImage(
        width = width,
        height = height,
        image = byteArray,
        maskMatrix = maskMatrix
    )
}
