package st235.com.github.seamcarving.utils.carving

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.utils.distance

object  SeamCarvingHelper {

    fun Bitmap.filterColors(
        targetWidth: Int,
        targetHeight: Int,
        colorMapping: Map<Int, Int>
    ): Array<IntArray> {
        val resizedMatrix = Bitmap.createScaledBitmap(this, targetWidth, targetHeight, false)

        val finalMatrix = Array(targetHeight) { IntArray(targetWidth) }

        for (i in 0 until this.height) {
            for (j in 0 until this.width) {
                finalMatrix[i][j] = findValue(resizedMatrix.getPixel(j, i), colorMapping)
            }
        }

        return finalMatrix
    }

    private fun findValue(@ColorInt color: Int, colorMapping: Map<Int, Int>): Int {
        for (that in colorMapping.keys) {
            if (color.distance(that) <= 20) {
                return colorMapping.getValue(that)
            }
        }

        return Energy.MASK_NO_VALUE
    }

    fun Array<IntArray>.countRemovedAreas(removeValue: Int): IntArray {
        val height = size
        val width = get(0).size

        val verticalRemove = BooleanArray(width)
        val horizontalRemove = BooleanArray(height)

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (this[i][j] == removeValue) {
                    verticalRemove[j] = true
                    horizontalRemove[i] = true
                }
            }
        }

        return intArrayOf(verticalRemove.count { it }, horizontalRemove.count {it })
    }

}