package st235.com.github.seamcarvingpicasso

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import st235.com.github.seamcarving.SeamCarver

class SeamCarvingTransformation(
    private val targetWidth: Int,
    private val targetHeight: Int,
    private val sampling: Int = 1
): Transformation {

    private companion object {
        val SEAM_CARVER = SeamCarver.create(type = SeamCarver.Type.SPEED)
    }

    override fun transform(source: Bitmap?): Bitmap {
        if (source == null) {
            throw IllegalArgumentException("Bitmap cannot be null")
        }

        val sampledBitmap = Bitmap.createScaledBitmap(source, source.width/ sampling, source.height / sampling, false)
        return SEAM_CARVER.retarget(sampledBitmap, null, targetWidth, targetHeight).bitmap
    }

    override fun key(): String {
        return "SeamCarvingTransformation {$targetWidth, $targetHeight}"
    }
}