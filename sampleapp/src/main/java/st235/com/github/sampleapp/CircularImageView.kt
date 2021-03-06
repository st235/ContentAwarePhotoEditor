package st235.com.github.sampleapp

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.Px
import androidx.core.content.res.ResourcesCompat

/**
 * Displays image resources, for example [android.graphics.drawable.Drawable] resources
 * with a circular mask.
 */
class CircularImageView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val center = PointF()

    private var extraText = DEFAULT_PLACEHOLDER

    @DrawableRes
    private var drawableId = -1

    private var targetImage: Bitmap? = null

    @ColorInt
    private var textColor = Color.BLACK

    @Px
    private var textSize = 0

    @FloatRange(from = 0.0)
    private var radius = 0f
    /**
     * Creates new one from xml with style from theme attribute
     * @param attrs an xml attributes set
     * @param defStyleAttr a style from theme
     */
    /**
     * Creates new one from xml
     * @param attrs an xml attributes set
     */
    /**
     * Creates new one from code
     */
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * Creates new one from xml with a style from theme attribute or style resource
     * Api 21 and above
     * @param attrs an xml attributes set
     * @param defStyleAttr a style from theme
     * @param defStyleRes a style resource
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    /**
     * Initialize current [CircularImageView] with attributes from xml
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView)
        val drawableId = ta.getResourceId(R.styleable.CircularImageView_cl_foreground, -1)
        if (drawableId != -1) {
            loadDrawable(drawableId)
        }
        textColor = ta.getColor(R.styleable.CircularImageView_cl_text_color, Color.BLACK)
        textSize = ta.getDimensionPixelSize(R.styleable.CircularImageView_cl_text_size, 0)
        val t = ta.getString(R.styleable.CircularImageView_cl_text)
        extraText = t ?: extraText
        ta.recycle()
    }

    /**
     * Set extra text
     * @param extraText is a text which will be displayed at center of image view if exists
     */
    fun setExtraText(extraText: String) {
        this.extraText = extraText
        invalidate()
    }

    fun setDrawable(drawable: Drawable?) {
        loadDrawable(drawable)
        invalidate()
    }

    fun setBitmap(bitmap: Bitmap?) {
        loadBitmap(bitmap)
        invalidate()
    }

    /**
     * Set current image drawable resource
     * @param drawableId is identifier of drawable which will be displayed at image view
     */
    fun setDrawableResource(@DrawableRes drawableId: Int) {
        loadDrawable(drawableId)
        invalidate()
    }

    /**
     * {@inheritDoc}
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fontMetrics = paint.fontMetrics
        val textWidth = paint.measureText(extraText)
        val textHeight = -fontMetrics.top + fontMetrics.bottom
        val desiredWidth = Math.round(textWidth + paddingLeft + paddingRight)
        val desiredHeight = Math.round(textHeight * 2f + paddingTop + paddingBottom)
        val measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec)
        val measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    /**
     * Reconcile a desired size for the view contents with a [android.view.View.MeasureSpec]
     * constraint passed by the parent.
     *
     * This is a simplified version of [View.resolveSize]
     *
     * @param contentSize Size of the view's contents.
     * @param measureSpec A [android.view.View.MeasureSpec] passed by the parent.
     * @return A size that best fits `contentSize` while respecting the parent's constraints.
     */
    private fun reconcileSize(contentSize: Int, measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> {
                if (contentSize < specSize) {
                    contentSize
                } else specSize
            }
            MeasureSpec.UNSPECIFIED -> contentSize
            else -> contentSize
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        radius = Math.min(
            measuredWidth / 2.0f - paddingLeft - paddingRight,
            measuredHeight / 2.0f - paddingTop - paddingBottom
        )
        center[measuredWidth / 2.0f] = measuredHeight / 2.0f
        if (targetImage == null && drawableId != -1) {
            loadDrawable(drawableId)
        }
        updateShader()
        canvas.drawCircle(center.x, center.y, radius, paint)
        paint.shader = null
        paint.color = textColor
        paint.textSize = textSize.toFloat()
        val textWidth = paint.measureText(extraText)
        val textY = (center.y - (paint.descent() + paint.ascent()) / 2).toInt()
        canvas.drawText(extraText, center.x - textWidth / 2, textY.toFloat(), paint)
    }

    /**
     * Loads targetImage to be shown into memory
     * @param drawableId which will be loaded as target
     */
    private fun loadDrawable(@DrawableRes drawableId: Int) {
        this.drawableId = drawableId
        val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
        loadDrawable(drawable)
    }

    private fun loadDrawable(drawable: Drawable?) {
        targetImage = drawableToBitmap(drawable)
        targetImage = cropBitmap(targetImage)
    }

    private fun loadBitmap(bitmap: Bitmap?) {
        targetImage = bitmap
        targetImage = cropBitmap(targetImage)
    }

    /**
     * Updates paint shader with custom targetImage target
     */
    private fun updateShader() {
        if (targetImage == null) {
            return
        }
        val shader = BitmapShader(targetImage!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        matrix.setScale(
            measuredWidth.toFloat() / targetImage!!.width.toFloat(),
            measuredHeight.toFloat() / targetImage!!.height.toFloat()
        )
        shader.setLocalMatrix(matrix)
        paint.shader = shader
    }

    /**
     * Creates center cropped targetImage from origin
     * @param bitmap which need to be cropped
     * @return targetImage instance
     */
    @CheckResult
    private fun cropBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) {
            return null
        }
        return if (bitmap.width >= bitmap.height) {
            Bitmap.createBitmap(
                bitmap,
                bitmap.width / 2 - bitmap.height / 2,
                0,
                bitmap.height, bitmap.height
            )
        } else Bitmap.createBitmap(
            bitmap,
            0,
            bitmap.height / 2 - bitmap.width / 2,
            bitmap.width, bitmap.width
        )
    }

    /**
     * Converts drawable to targetImage.
     * If the drawable has no intrinsic
     * width or height the laid out sizes will be set up as current viewport.
     * @param drawable which need to be shown
     * @return targetImage instance of drawable
     */
    @CheckResult
    private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var intrinsicWidth = drawable.intrinsicWidth
        var intrinsicHeight = drawable.intrinsicHeight
        if (intrinsicWidth == -1 || intrinsicHeight == -1) {
            intrinsicWidth = measuredWidth
            intrinsicHeight = measuredHeight
        }
        return if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            null
        } else try {
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
            Log.e(TAG, "OutOfMemory while creating targetImage!")
            null
        }
    }

    companion object {
        private const val TAG = "CircularImageView"
        private const val DEFAULT_PLACEHOLDER = "Ex"
    }
}
