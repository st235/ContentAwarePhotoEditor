package st235.com.github.seamcarving.utils.carving

import android.graphics.Bitmap
import android.net.Uri
import st235.com.github.seamcarving.interactors.models.AspectRatio

class CarvingRequest(
    val image: Uri,
    val qualityMode: CarvingQualityMode,
    val resizeMode: CarvingResizeMode,
    val aspectRatio: AspectRatio?,
    val filterMask: Bitmap
) {

    class Builder {
        private var image: Uri? = null
        private var qualityMode: CarvingQualityMode? = null
        private var resizeMode: CarvingResizeMode? = null
        private var aspectRatio: AspectRatio? = null
        private var filterMask: Bitmap? = null

        fun image(image: Uri) = apply { this.image = image }

        fun qualityMode(qualityMode: CarvingQualityMode) = apply { this.qualityMode = qualityMode }

        fun resizeMode(resizeMode: CarvingResizeMode) = apply { this.resizeMode = resizeMode }

        fun aspectRatio(aspectRatio: AspectRatio?) = apply { this.aspectRatio = aspectRatio }

        fun filterMask(filterMask: Bitmap) = apply { this.filterMask = filterMask }

        fun build(): CarvingRequest {
            val image = image
            val qualityMode = qualityMode
            val resizeMode = resizeMode
            val filterMask = filterMask

            if (image == null ||
                qualityMode == null ||
                resizeMode == null ||
                filterMask == null
            ) {
                throw IllegalStateException("Element was null")
            }

            return CarvingRequest(image, qualityMode, resizeMode, aspectRatio, filterMask)
        }

    }

}
