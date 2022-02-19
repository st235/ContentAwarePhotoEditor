package st235.com.github.seamcarving.utils

import kotlin.math.max
import kotlin.math.min
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.images.ArrayCarvableImage
import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.images.TransposedCarvableImage

internal object CarvingUtils {

    /**
     * Transposes the original image
     *
     * Runtime complexity: O(1)
     */
    fun transpose(image: CarvableImage): CarvableImage {
        if (image is TransposedCarvableImage) {
            return image.originalImage
        }

        return TransposedCarvableImage(image)
    }

    /**
     * Creates a new image using the given image
     * without the removed seam.
     *
     * Runtime complexity: O(w * h)
     */
    fun removeSeam(image: CarvableImage, seam: IntArray): CarvableImage {
        val newImage = Array(image.height) { IntArray(image.width - 1) }
        val maskMatrix = if (image.isMasked) {
            Array(image.height) { IntArray(image.width - 1) }
        } else {
            null
        }

        for (i in 0 until image.height) {
            for (j in 0 until image.width) {
                if (j == seam[i]) {
                    continue
                }

                val offset = if (j >= seam[i]) -1 else 0
                newImage[i][j + offset] = image.getPixelAt(i, j)
                maskMatrix?.get(i)?.set(j + offset, image.getMaskPixel(i, j) ?: 0)
            }
        }

        return ArrayCarvableImage(newImage, maskMatrix)
    }

    /**
     * Creates a new image using the given image
     * with the added seam.
     *
     * Runtime complexity: O(w * h)
     */
    fun addSeam(image: CarvableImage, seam: IntArray): CarvableImage {
        val newImage = Array(image.height) { IntArray(image.width + 1) }
        val maskMatrix = if (image.isMasked) {
            Array(image.height) { IntArray(image.width + 1) }
        } else {
            null
        }

        for (i in 0 until image.height) {
            for (j in 0 until image.width + 1) {
                val offset = if (j > seam[i]) -1 else 0
                newImage[i][j + offset] = image.getPixelAt(i, j)
                maskMatrix?.get(i)?.set(j + offset, image.getMaskPixel(i, j) ?: 0)
            }
        }

        return ArrayCarvableImage(newImage, maskMatrix)
    }

    /**
     * Retrieving a vertical seam
     * with the least energy on the image.
     *
     * Runtime complexity: O(w * h)
     * Space complexity: O(w * h)
     */
    fun retrieveVerticalSeam(energy: Energy, image: CarvableImage): IntArray {
        // Runtime complexity of this call is O(w * h)
        val lookup = calculateEnergy(energy, image)

        var minIndex = 0
        var minValue = Integer.MAX_VALUE

        // Finding the starting point for the algorithm
        // Runtime complexity is O(w)
        for (j in 0 until image.width) {
            val value = lookup[image.height - 1][j]

            if (value < minValue) {
                minIndex = j
                minValue = value
            }
        }

        // Runtime complexity is O(h)
        val seam = IntArray(image.height)

        seam[image.height - 1] = minIndex

        var previous = minIndex

        // Runtime complexity is O(h)
        for (i in image.height - 2 downTo 0) {
            val left = getOrDefault(lookup, i, previous - 1, Integer.MAX_VALUE)
            val center = getOrDefault(lookup, i, previous, Integer.MAX_VALUE)
            val right = getOrDefault(lookup, i, previous + 1, Integer.MAX_VALUE)

            val seamValue = if (left <= center && left <= right) {
                previous - 1
            } else if (center <= left && center <= right) {
                previous
            } else {
                // right <= left && right <= center
                previous + 1
            }

            seam[i] = min(max(seamValue, 0), image.width - 1)
            previous = seam[i]
        }

        return seam
    }

    /**
     * Calculates the energy map of the image for
     * the given [CarvableImage] using the given
     * [Energy] function.
     *
     * Runtime complexity: O(w * h)
     * Space complexity: O(w * h)
     */
    private fun calculateEnergy(energy: Energy, image: CarvableImage): Array<IntArray> {
        val lookup = Array(image.height) { IntArray(image.width) }

        for (j in 0 until image.width) {
            lookup[0][j] = energy.calculateAt(0, j, image)
        }

        for (i in 1 until image.height) {
            for (j in 0 until image.width) {
                val adjacentEnergy = min(
                    min(
                        getOrDefault(lookup, i - 1, j - 1, Integer.MAX_VALUE),
                        lookup[i - 1][j]
                    ),
                    getOrDefault(lookup, i - 1, j + 1, Integer.MAX_VALUE)
                )

                val pixelEnergy = energy.calculateAt(i, j, image)

                lookup[i][j] = if (adjacentEnergy == Energy.MAX_ENERGY || pixelEnergy == Energy.MAX_ENERGY) {
                    Energy.MAX_ENERGY
                } else {
                    adjacentEnergy + pixelEnergy
                }
            }
        }

        return lookup
    }

    /**
     * Retrieves a value from an array representation of the image.
     *
     * Runtime complexity: O(1)
     * Space complexity: O(1)
     */
    private fun getOrDefault(image: Array<IntArray>, i: Int, j: Int, default: Int): Int {
        if (i < 0 || i >= image.size || j < 0 || j >= image[i].size) {
            return default
        }

        return image[i][j]
    }

}