package st235.com.github.seamcarving.algorithms

import android.graphics.Bitmap
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.SeamCarver
import st235.com.github.seamcarving.images.BitmapCarvableImage
import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.utils.CarvingHelper
import st235.com.github.seamcarving.utils.asCarvingResult

internal class SimpleDynamicSeamCarver(
    private val energy: Energy
): SeamCarver {

    /**
     * Retargets the given image
     * to the new width and height
     *
     * Runtime complexity: O(w * h * (w + h))
     */
    override fun retarget(
        image: Bitmap,
        mask: Array<IntArray>?,
        targetWidth: Int,
        targetHeight: Int
    ): SeamCarver.CarvingResult {
        val carvableImage = BitmapCarvableImage(image, mask)
        return retargetHeight(retargetWidth(carvableImage, targetWidth), targetHeight).asCarvingResult()
    }

    /**
     * Retargets the given image
     * to the given width
     *
     * Runtime complexity: O(w * w * h), ie O(w^2 * h)
     */
    private fun retargetWidth(image: CarvableImage, targetWidth: Int): CarvableImage {
        var currentImage = image

        while (currentImage.width != targetWidth) {
            val seam = CarvingHelper.retrieveVerticalSeam(energy, currentImage)

            currentImage = if (targetWidth > currentImage.width) {
                CarvingHelper.addSeam(currentImage, seam)
            } else {
                CarvingHelper.removeSeam(currentImage, seam)
            }
        }

        return currentImage
    }

    /**
     * Retargets the given image
     * to the given height
     *
     * Runtime complexity: O(w * h * h), ie O(w * h^2)
     */
    private fun retargetHeight(image: CarvableImage, targetHeight: Int): CarvableImage {
        val currentImage = CarvingHelper.transpose(image)
        return CarvingHelper.transpose(retargetWidth(currentImage, targetHeight))
    }

}