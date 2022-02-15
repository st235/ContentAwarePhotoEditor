package st235.com.github.seamcarving.presentation.components

import android.graphics.Bitmap
import androidx.annotation.Px
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.max

class GlideBlurTransformation(
    @androidx.annotation.IntRange(from = 1L) @Px private val radius: Int
): BitmapTransformation() {

    private companion object {
        private const val ID =
            "st235.com.github.seamcarving.presentation.components.GlideBlurTransformation.1.0"
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
       messageDigest.update(
           String.format("%s-%d", ID, radius).toByteArray(CHARSET)
       )
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val downscale = max(toTransform.width / outWidth, toTransform.height / outHeight)
        return Blur.apply(radius, downscale, toTransform)
    }
}