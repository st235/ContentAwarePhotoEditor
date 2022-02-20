package st235.com.github.seamcarving.algorithms

import android.graphics.Bitmap
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.SeamCarver
import st235.com.github.seamcarving.images.BitmapCarvableImage
import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.utils.CarvingUtils
import st235.com.github.seamcarving.utils.asBitmap

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
    ): Bitmap {
        val carvableImage = BitmapCarvableImage(image, mask)
        return retargetHeight(retargetWidth(carvableImage, targetWidth), targetHeight).asBitmap()
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
            val seam = CarvingUtils.retrieveVerticalSeam(energy, currentImage)

            currentImage = if (targetWidth > currentImage.width) {
                CarvingUtils.addSeam(currentImage, seam)
            } else {
                CarvingUtils.removeSeam(currentImage, seam)
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
        val currentImage = CarvingUtils.transpose(image)
        return CarvingUtils.transpose(retargetWidth(currentImage, targetHeight))
    }

}