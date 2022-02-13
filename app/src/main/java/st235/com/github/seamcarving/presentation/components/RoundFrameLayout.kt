package st235.com.github.seamcarving.presentation.components

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import kotlin.math.min
import st235.com.github.seamcarving.R

class RoundFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val bounds = Rect()

    private val isSquare: Boolean

    @Px
    var bgRoundRadius: Float = 0F
        set(value) {
            field = value
            invalidateOutline()
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout)

        bgRoundRadius =
            typedArray.getDimension(R.styleable.RoundFrameLayout_rfl_backgroundRoundRadius, 0F)
        setElevationCompat(typedArray.getDimension(R.styleable.RoundFrameLayout_rfl_elevation, 0F))

        isSquare = typedArray.getBoolean(R.styleable.RoundFrameLayout_rfl_isSquare, false)

        typedArray.recycle()

        isClickable = true
        isFocusable = true

        outlineProvider = RoundButtonOutlineProvider()
        clipToOutline = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (isSquare) {
            if (width < height) {
                super.onMeasure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                )
            } else {
                super.onMeasure(
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                )
            }
            return
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds.set(0, 0, w, h)
    }

    fun setElevationCompat(@Px elevation: Float) {
        ViewCompat.setElevation(this, elevation)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class RoundButtonOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setRoundRect(bounds,
                MathUtils.clamp(bgRoundRadius, 0F, min(height, width) / 2F)
            )
        }
    }
}
