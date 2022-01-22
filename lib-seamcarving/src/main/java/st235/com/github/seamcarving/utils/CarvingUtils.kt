package st235.com.github.seamcarving.utils

import android.graphics.Color
import android.util.Log
import java.util.Arrays
import kotlin.math.min
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.images.ArrayCarvableImage
import st235.com.github.seamcarving.images.CarvableImage

object CarvingUtils {

    fun transpose(image: CarvableImage): CarvableImage {
        val newImage = Array(image.width) { IntArray(image.height) }

        for (i in 0 until image.height) {
            for (j in 0 until image.width) {
                newImage[i][j] = image.getPixelAt(j, i)
            }
        }

        return ArrayCarvableImage(newImage)
    }

    fun remove(image: CarvableImage, seam: IntArray): CarvableImage {
        val newImage = Array(image.height) { IntArray(image.width - 1) }

        for (i in 0 until image.height) {
            for (j in 0 until image.width) {
                if (j == seam[i]) {
                    continue
                }

                val offset = if (j >= seam[i]) -1 else 0
                newImage[i][j + offset] = image.getPixelAt(i, j)
            }
        }

        return ArrayCarvableImage(newImage)
    }

    fun calculateSeam(energy: Energy, image: CarvableImage): IntArray {
        val lookup = Array(image.height) { IntArray(image.width) }

        for (j in 0 until image.width) {
            lookup[0][j] = energy.calculateAt(0, j, image)
        }

        for (i in 1 until image.height) {
            for (j in 0 until image.width) {
                lookup[i][j] = min(
                    min(
                        getOrDefault(lookup, i - 1, j - 1, Integer.MAX_VALUE),
                        lookup[i - 1][j]
                    ),
                    getOrDefault(lookup, i - 1, j + 1, Integer.MAX_VALUE)
                ) + energy.calculateAt(i, j, image)
            }
        }

        var minIndex = 0
        var minValue = Integer.MAX_VALUE

        for (j in 0 until image.width) {
            val value = lookup[image.height - 1][j]

            if (value < minValue) {
                minIndex = j
                minValue = value
            }
        }

        val seam = IntArray(image.height)

        seam[image.height - 1] = minIndex

        var previous = minIndex
        for (i in image.height - 2 downTo 0) {
            val left = getOrDefault(lookup, i, previous - 1, Integer.MAX_VALUE)
            val center = getOrDefault(lookup, i, previous, Integer.MAX_VALUE)
            val right = getOrDefault(lookup, i, previous + 1, Integer.MAX_VALUE)

            if (left <= center && left <= right) {
                seam[i] = previous - 1
            } else if (center <= left && center <= right) {
                seam[i] = previous
            } else if (right <= left && right <= center) {
                seam[i] = previous + 1
            }

            previous = seam[i]
        }

        return seam
    }

    private fun getOrDefault(image: Array<IntArray>, i: Int, j: Int, default: Int): Int {
        if (i < 0 || i >= image.size || j < 0 || j >= image[i].size) {
            return default
        }

        return image[i][j]
    }

}