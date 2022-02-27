package st235.com.github.seamcarving.interactors.models

import android.graphics.Rect
import android.graphics.RectF
import st235.com.github.seamcarving.utils.compareWithTolerance

class AspectRatio(
    val width: Int,
    val height: Int
) {

    val aspectRatio: Double
    get() {
        return width.toDouble() / height
    }

    fun calculateHeight(newWidth: Int): Int {
        // ow/oh = nw/nh =>
        // nh = nw * oh / ow
        return (newWidth * height) / width
    }

    fun calculateWidth(newHeight: Int): Int {
        // ow/oh = nw/nh =>
        // nw = nh * ow / oh
        return (newHeight * width) / height
    }

    fun scale(factor: Float): AspectRatio {
        return AspectRatio((width * factor).toInt(), (height * factor).toInt())
    }

    fun fitInto(rect: Rect, out: Rect) {
        if (width > height) {
            val newHeight = calculateHeight(rect.width())
            out.set(rect.left, rect.top, rect.width(), newHeight)
        } else {
            val newWidth = calculateWidth(rect.height())
            out.set(rect.left, rect.top, newWidth, rect.height())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AspectRatio
        return aspectRatio.compareWithTolerance(other.aspectRatio)
    }

    override fun hashCode(): Int {
        return aspectRatio.hashCode()
    }


}