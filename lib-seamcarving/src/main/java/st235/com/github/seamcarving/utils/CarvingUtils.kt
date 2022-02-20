package st235.com.github.seamcarving.utils

import android.graphics.Color
import kotlin.math.max
import kotlin.math.min
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.images.ArrayCarvableImage
import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.images.TransposedCarvableImage

internal object CarvingUtils {

    /**
     * left (j - 1) - down (j) - right (j + 1)
     *               current (j)
     */
    const val MATCH_RIGHT = 1
    const val MATCH_UP = 0
    const val MATCH_LEFT = -1

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
     * The bipartite matching algorithm (Hungarian's algorithm optimisation)
     * can use the following approach to calculate the energy of ith row.
     *
     * F(i) = max(
     *             F(i - 1) + w(i, i),
     *             F(i - 2) + w(i, i - 1) + w(i - 1, i)
     *           )
     *
     * Corner cases:
     * F(-1) = F(0) = 0
     */
    fun establishBipartiteConnections(
        row: Int,
        seamsEnergy: Array<LongArray>,
        optimumEnergy: Array<LongArray>,
        matches: LongArray
    ) {
        val width = matches.size

        matches[0] = calculateWeight(row, 0, 0, optimumEnergy, seamsEnergy)
        matches[1] = max(
            matches[0] + calculateWeight(row, 1, 1, optimumEnergy, seamsEnergy),
            calculateWeight(row, 0, 1, optimumEnergy, seamsEnergy) +
                    calculateWeight(row, 1, 0, optimumEnergy, seamsEnergy)
        )

        for (i in 2 until width) {
            val f1 = matches[i - 1] +
                    calculateWeight(row, i, i, optimumEnergy, seamsEnergy)

            val f2 = matches[i - 2] +
                    calculateWeight(row, i, i - 1, optimumEnergy, seamsEnergy) +
                    calculateWeight(row, i - 1, i, optimumEnergy, seamsEnergy)

            matches[i] = max(f1, f2)
        }
    }

    /**
     * A weight of a graph edge can be
     * calculated using the following function
     *
     * |i-j|<=1 therefore w(i,j) = A(i,row) * M(j,row+1)
     * and -inf otherwise
     *
     * A is a cumulative seams energy function's matrix
     * M is a cumulative optimal energy function's matrix, can be calculated using dp
     */
    fun calculateWeight(
        row: Int,
        columnA: Int,
        columnB: Int,
        optimumEnergy: Array<LongArray>,
        seamsEnergy: Array<LongArray>
    ): Long {
        return seamsEnergy[row][columnA] * optimumEnergy[row + 1][columnB]
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
     * without the removed seam.
     *
     * Runtime complexity: O(w * h)
     */
    fun removeSeams(image: CarvableImage, seamsToRemove: Int, mask: Array<BooleanArray>): CarvableImage {
        val newImage = Array(image.height) { IntArray(image.width ) }
        val maskMatrix = if (image.isMasked) {
            null//Array(image.height) { IntArray(image.width - seamsToRemove) }
        } else {
            null
        }

        for (i in 0 until image.height) {
            var newJ = 0

            for (j in 0 until image.width) {
                if (mask[i][j]) {
                    newImage[i][j] = Color.RED
                    continue
                }

                newImage[i][j] = image.getPixelAt(i, j)
//                maskMatrix?.get(i)?.set(newJ, image.getMaskPixel(i, j) ?: 0)
//                newJ++
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
     * Creates a new image using the given image
     * without the removed seam.
     *
     * Runtime complexity: O(w * h)
     */
    fun addSeams(image: CarvableImage, seamsToRemove: Int, mask: Array<BooleanArray>): CarvableImage {
        val newImage = Array(image.height) { IntArray(image.width + seamsToRemove) }
        val maskMatrix = if (image.isMasked) {
            Array(image.height) { IntArray(image.width + seamsToRemove) }
        } else {
            null
        }

        for (i in 0 until image.height) {
            var newJ = 0

            for (j in 0 until image.width) {
                if (mask[i][j]) {
                    newImage[i][newJ] = image.getPixelAt(i, j)
                    maskMatrix?.get(i)?.set(newJ, image.getMaskPixel(i, j) ?: 0)

                    newImage[i][newJ + 1] = image.getPixelAt(i, j)
                    maskMatrix?.get(i)?.set(newJ + 1, image.getMaskPixel(i, j) ?: 0)

                    newJ += 2
                    continue
                }

                newImage[i][newJ] = image.getPixelAt(i, j)
                maskMatrix?.get(i)?.set(newJ, image.getMaskPixel(i, j) ?: 0)
                newJ++
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
        var minValue: Long = Integer.MAX_VALUE.toLong()

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
            val left = getOrDefault(lookup, i, previous - 1, Integer.MAX_VALUE.toLong())
            val center = getOrDefault(lookup, i, previous, Integer.MAX_VALUE.toLong())
            val right = getOrDefault(lookup, i, previous + 1, Integer.MAX_VALUE.toLong())

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
    fun calculateEnergy(energy: Energy, image: CarvableImage): Array<LongArray> {
        val lookup = Array(image.height) { LongArray(image.width) }

        for (j in 0 until image.width) {
            lookup[0][j] = energy.calculateAt(0, j, image)
        }

        for (i in 1 until image.height) {
            for (j in 0 until image.width) {
                val adjacentEnergy = min(
                    min(
                        getOrDefault(lookup, i - 1, j - 1, Integer.MAX_VALUE.toLong()),
                        lookup[i - 1][j]
                    ),
                    getOrDefault(lookup, i - 1, j + 1, Integer.MAX_VALUE.toLong())
                )

                val pixelEnergy = energy.calculateAt(i, j, image)

                lookup[i][j] =
                    if (adjacentEnergy == Energy.MAX_ENERGY || pixelEnergy == Energy.MAX_ENERGY) {
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
    private fun getOrDefault(image: LongArray, i: Int, default: Long): Long {
        if (i < 0 || i >= image.size) {
            return default
        }

        return image[i]
    }

    /**
     * Retrieves a value from a 2d array representation of the image.
     *
     * Runtime complexity: O(1)
     * Space complexity: O(1)
     */
    private fun getOrDefault(image: Array<LongArray>, i: Int, j: Int, default: Long): Long {
        if (i < 0 || i >= image.size || j < 0 || j >= image[i].size) {
            return default
        }

        return image[i][j]
    }

}