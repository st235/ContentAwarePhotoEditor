package st235.com.github.seamcarving.algorithms

import android.graphics.Bitmap
import java.util.PriorityQueue
import kotlin.math.abs
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.SeamCarver
import st235.com.github.seamcarving.images.BitmapCarvableImage
import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.utils.CarvingUtils
import st235.com.github.seamcarving.utils.asBitmap

internal class FastSeamCarver(
    private val energy: Energy
): SeamCarver {

    private class Seam(
        height: Int
    ) {

        var value: Long = 0L
        private set

        private var index = 0
        private var columns = IntArray(height)

        fun add(column: Int, energy: Long) {
            value += energy

            columns[index] = column
            index++
        }

        fun columnAt(row: Int): Int {
            return columns[row]
        }

    }

    private class SeamsComparator: Comparator<Seam> {
        override fun compare(o1: Seam, o2: Seam): Int {
            return -o1.value.compareTo(o2.value)
        }
    }

    override fun retarget(
        image: Bitmap,
        mask: Array<IntArray>?,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap {
        val carvableImage = BitmapCarvableImage(image, mask)
        return retargetHeight(retargetWidth(carvableImage, targetWidth), targetHeight).asBitmap()
    }

    private fun retargetHeight(image: CarvableImage, targetHeight: Int): CarvableImage {
        val currentImage = CarvingUtils.transpose(image)
        return CarvingUtils.transpose(retargetWidth(currentImage, targetHeight))
    }

    private fun retargetWidth(image: CarvableImage, targetWidth: Int): CarvableImage {
        if (targetWidth == image.width) {
            return image
        }

        //A
        val seamsEnergy: Array<LongArray> = Array(image.height) { LongArray(image.width) }

        //M
        val optimalEnergy: Array<LongArray> = CarvingUtils.calculateEnergy(energy, image)

        val seams = Array<Seam>(image.width) { Seam(image.height) }

        for (i in 0 until image.width) {
            seams[i].add(i, energy.calculateAt(0, i, image))
        }

        val matches = LongArray(image.width)
        for (row in 0 until image.height - 1) {
            CarvingUtils.establishBipartiteConnections(row, seamsEnergy, optimalEnergy, matches)
            extendSeams(row, image, seams, seamsEnergy, optimalEnergy, matches)
        }

        val k = abs(image.width - targetWidth)
        val pq = PriorityQueue<Seam>(k, SeamsComparator())

        for (seam in seams) {
            pq.add(seam)

            if (pq.size > k) {
                pq.remove()
            }
        }

        val mask: Array<BooleanArray> = Array(image.height) { BooleanArray(image.width) }

        for (seam in pq) {
            removeSeam(mask, seam)
        }

        return if (targetWidth < image.width) {
            CarvingUtils.removeSeams(image, k, mask)
        } else {
            CarvingUtils.addSeams(image, k, mask)
        }
    }

    private fun removeSeam(mask: Array<BooleanArray>, seam: Seam) {
        var row = mask.size - 1

        while (row >= 0) {
            mask[row][seam.columnAt(row)] = true
            row--
        }
    }

    private fun extendSeams(
        row: Int,
        image: CarvableImage,
        seams: Array<Seam>,
        seamsEnergy: Array<LongArray>,
        optimumEnergy: Array<LongArray>,
        matches: LongArray
    ) {
        var column = image.width - 1;

        while (column >= 0) {
            val lastMatch = if (column == 0) 0 else matches[column - 1]

            if (matches[column] == lastMatch + CarvingUtils.calculateWeight(row, column, column, optimumEnergy, seamsEnergy)) {
                seams[column].add(column, energy.calculateAt(row, column, image))
                seamsEnergy[row + 1][column] = seamsEnergy[row][column] + energy.calculateAt(row + 1, column, image)
                column -= 1
            } else {
                seams[column - 1].add(column, energy.calculateAt(row, column - 1, image))
                seams[column].add(column - 1, energy.calculateAt(row, column, image))

                val t = seams[column]
                seams[column] = seams[column - 1]
                seams[column - 1] = t

                seamsEnergy[row + 1][column - 1] = seamsEnergy[row][column] + energy.calculateAt(row  + 1, column - 1, image)
                seamsEnergy[row + 1][column] = seamsEnergy[row][column - 1] + energy.calculateAt(row + 1, column, image)
                column -= 2
            }
        }
    }

}