package st235.com.github.seamcarving

import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.utils.CarvingUtils

class SimpleSeamCarver(
    private val energy: Energy
) {

    fun retargetWidth(image: CarvableImage, newWidth: Int): CarvableImage {
        var currentImage = image

        while (currentImage.width != newWidth) {
            val seam = CarvingUtils.calculateSeam(energy, currentImage)
            currentImage = CarvingUtils.remove(currentImage, seam)
        }

        return currentImage
    }

}