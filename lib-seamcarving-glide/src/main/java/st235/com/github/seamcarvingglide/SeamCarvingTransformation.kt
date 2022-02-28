package st235.com.github.seamcarvingglide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import st235.com.github.seamcarving.SeamCarver

class SeamCarvingTransformation(
    private val sampling: Int = 1
): BitmapTransformation() {

    private companion object {
        const val VERSION = 2
        const val ID = "st235.com.github.seamcarvingglide.SeamCarvingTransformation.$VERSION"

        val SEAM_CARVER = SeamCarver.create(type = SeamCarver.Type.SPEED)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + sampling).toByteArray(CHARSET))
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val sampledBitmap = Bitmap.createScaledBitmap(
            toTransform,
            toTransform.width / sampling,
            toTransform.height / sampling,
            false
        )
        val targetImage = SEAM_CARVER.retarget(
            sampledBitmap,
            null,
            outWidth /  sampling,
            outHeight / sampling
        ).bitmap
        return targetImage
    }
}