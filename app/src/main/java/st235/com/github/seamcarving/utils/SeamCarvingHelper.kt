package st235.com.github.seamcarving.utils

import android.graphics.Bitmap
import st235.com.github.seamcarving.utils.SeamCarvingHelper.filterColors

object  SeamCarvingHelper {

    fun Bitmap?.filterColors(
        targetWidth: Int,
        targetHeight: Int,
        colorMapping: Map<Int, Int>
    ): Array<IntArray>? {
        if (this == null) {
            return null
        }

        val resizedMatrix = Bitmap.createScaledBitmap(this, targetWidth, targetHeight, false)

        val finalMatrix = Array(targetHeight) { IntArray(targetWidth) }

        for (i in 0 until this.height) {
            for (j in 0 until this.width) {
                finalMatrix[i][j] = colorMapping.getValue(resizedMatrix.getPixel(j, i))
            }
        }

        return finalMatrix
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

        return intArrayOf(verticalRemove.count(), horizontalRemove.count())
    }

}