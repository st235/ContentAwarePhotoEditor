package st235.com.github.seamcarving.utils

import android.graphics.Bitmap
import java.nio.ByteBuffer
import st235.com.github.seamcarving.images.ByteArrayCarvableImage
import st235.com.github.seamcarving.images.CarvableImage


fun Bitmap.toCarvableImage(maskMatrix: Array<IntArray>?): CarvableImage {
    val dst = ByteBuffer.allocate(byteCount)
    copyPixelsToBuffer(dst)
    val byteArray = dst.array()

    return ByteArrayCarvableImage(
        width = width,
        height = height,
        image = byteArray,
        maskMatrix = maskMatrix
    )
}
