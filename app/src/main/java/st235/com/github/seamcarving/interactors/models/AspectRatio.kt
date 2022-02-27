package st235.com.github.seamcarving.interactors.models

import android.graphics.Rect
import st235.com.github.seamcarving.utils.compareWithTolerance

class AspectRatio(
    val width: Int,
    val height: Int
) {

    val rawAspectRatio: Double
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

    fun isTheSameAspectRatio(that: AspectRatio): Boolean {
        return rawAspectRatio.compareWithTolerance(that.rawAspectRatio)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AspectRatio

        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }

}