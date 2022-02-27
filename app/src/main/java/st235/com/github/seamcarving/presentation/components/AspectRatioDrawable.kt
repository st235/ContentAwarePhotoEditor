package st235.com.github.seamcarving.presentation.components

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Px
import st235.com.github.seamcarving.interactors.models.AspectRatio
import st235.com.github.seamcarving.utils.dp

class AspectRatioDrawable(
    private val aspectRatio: AspectRatio,
    @ColorInt private val borderColor: Int,
    @Px private val strokeRadius: Float,
    @Px private val roundRadius: Float
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeRadius
        color = borderColor
    }

    private var tintColorStateList: ColorStateList? = null

    private val viewport = Rect()
    private val paddedBounds = Rect()
    private val buffer = RectF()

    override fun getIntrinsicWidth(): Int {
        return aspectRatio.width
    }

    override fun getIntrinsicHeight(): Int {
        return aspectRatio.height
    }

    override fun getMinimumWidth(): Int {
        return aspectRatio.width
    }

    override fun getMinimumHeight(): Int {
        return aspectRatio.height
    }

    override fun draw(canvas: Canvas) {
        tintColorStateList?.let { tintColorStateList ->
            paint.color = tintColorStateList.getColorForState(state, Color.BLACK)
        }

        paddedBounds.set(bounds)

        paddedBounds.left += 6.dp
        paddedBounds.top += 6.dp
        paddedBounds.right -= 6.dp
        paddedBounds.bottom -= 6.dp

        aspectRatio.fitInto(paddedBounds, viewport)

        viewport.offset(
            (paddedBounds.width() - viewport.width()) / 2,
            (paddedBounds.height() - viewport.height()) / 2
        )

        buffer.set(viewport)
        canvas.drawRoundRect(buffer, roundRadius, roundRadius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun isStateful(): Boolean {
        return true
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setTintList(tint: ColorStateList?) {
        tintColorStateList = tint
        invalidateSelf()
    }
}