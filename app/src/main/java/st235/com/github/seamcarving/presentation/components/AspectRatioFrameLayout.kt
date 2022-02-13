package st235.com.github.seamcarving.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

class AspectRatioFrameLayout: FrameLayout {

    private data class AspectRatio(
        val width: Int,
        val height: Int
    )

    private var aspectRatio = AspectRatio(1, 1)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    fun setAspectRatio(width: Int, height: Int) {
        aspectRatio = AspectRatio(width, height)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)

        val realWidth: Int
        val realHeight: Int

        if (aspectRatio.width > aspectRatio.height || maxHeight == 0) {
            realWidth = maxWidth
            realHeight = ((maxWidth * aspectRatio.height).toDouble() / aspectRatio.width).toInt()
        } else {
            realHeight = maxHeight
            realWidth = ((maxHeight * aspectRatio.width).toDouble() / aspectRatio.height).toInt()
        }

        setMeasuredDimension(realWidth, realHeight)

        for (childIndex in 0 until childCount) {
            val view = getChildAt(childIndex)

            val childWidth = realWidth - view.marginLeft - view.marginRight
            val childHeight = realHeight - view.marginTop - view.marginBottom

            view.measure(
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
            )
        }
    }
}