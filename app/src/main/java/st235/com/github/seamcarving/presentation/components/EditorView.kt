package st235.com.github.seamcarving.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px

class EditorView: View {

    var foregroundImage: Drawable? = null
        set(newValue) {
            field = newValue
            field?.fitIntoView(foregroundBoundsRect)

            initEditableArea()
            invalidate()
        }

    @get:ColorInt
    var editBrush: Int
    get() {
        return paint.color
    }
    set(@ColorInt newValue) {
        paint.color = newValue
        invalidate()
    }

    @get:Px
    var editRadius: Float
    get() {
        return paint.strokeWidth
    }
    set(@Px newValue) {
        paint.strokeWidth = newValue
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = 64F
        strokeCap = Paint.Cap.ROUND
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    private val points = mutableListOf<PointF>()
    private val bufferPath = Path()


    private val editablePaint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG).apply {
        color = Color.BLACK
        isFilterBitmap = true
    }
    private var editableCanvas: Canvas? = null
    private var editableArea: Bitmap? = null

    private var foregroundBoundsRect = Rect()

    private val viewBounds = Rect()

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isFocusable = true
        isClickable = true
    }

    fun getAreaSnapshot(): Bitmap? {
        val area = editableArea

        if (area == null) {
            return null
        }

        return Bitmap.createBitmap(area)
    }

    private fun initEditableArea() {
        val width = foregroundBoundsRect.width()
        val height = foregroundBoundsRect.height()

        if (width <= 0 || height <= 0) {
            return
        }

        val area = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        editableArea = area
        editableCanvas = Canvas(area)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewBounds.set(0, 0, w, h)

        foregroundImage?.fitIntoView(foregroundBoundsRect)
        initEditableArea()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) {
            return
        }

        foregroundImage?.drawInCenter(canvas)

        val area = editableArea

        if (area != null) {
            canvas.drawBitmap(
                area,
                foregroundBoundsRect.left.toFloat(),
                foregroundBoundsRect.top.toFloat(),
                editablePaint
            )
        }

        drawPath(canvas, foregroundBoundsRect.left, foregroundBoundsRect.top)
    }

    private fun drawPath(canvas: Canvas, offsetX: Int, offsetY: Int) {
        if (points.isEmpty()) {
            return
        }

        bufferPath.reset()

        for ((index, point) in points.withIndex()) {
            val x = offsetX + point.x
            val y = offsetY + point.y

            if (index == 0) {
                bufferPath.moveTo(x, y)
            } else if (index < points.size - 1) {
                val nextX = offsetX + points[index + 1].x
                val nextY = offsetY + points[index + 1].y

                bufferPath.quadTo(x, y, nextX, nextY)
            } else {
                bufferPath.lineTo(x, y)
            }
        }

        canvas.drawPath(bufferPath, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                modifyTheArea(event.x, event.y, foregroundBoundsRect)
                invalidate()
                true
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                drawPath(editableCanvas!!, 0, 0)
                points.clear()
                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    private fun modifyTheArea(x: Float, y: Float, offsetRect: Rect) {
        val centerX = x - offsetRect.left
        val centerY = y - offsetRect.top

        points.add(PointF(centerX, centerY))
    }

    private fun Drawable.drawInCenter(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()

        canvas.save()
        canvas.translate(
            (viewBounds.width() - width) / 2F,
            (viewBounds.height() - height) / 2F
        )

        draw(canvas)
        canvas.restore()
    }

    private fun Drawable.fitIntoView(boundsRect: Rect) {
        val newImageBounds = fitIntoRect(viewBounds)
        boundsRect.set(
            (viewBounds.width() - newImageBounds.width()) / 2,
            (viewBounds.height() - newImageBounds.height()) / 2,
            (viewBounds.width() + newImageBounds.width()) / 2,
            (viewBounds.height() + newImageBounds.height()) / 2
        )
        bounds = newImageBounds
    }

    private fun Drawable.fitIntoRect(bounds: Rect): Rect {
        if (intrinsicWidth == -1 || intrinsicHeight == -1) {
            return Rect(bounds)
        }

        return if (intrinsicWidth > intrinsicHeight) {
            val aspectRatio = intrinsicHeight.toFloat() / intrinsicWidth
            Rect(0, 0, bounds.width(), (bounds.width() * aspectRatio).toInt())
        } else {
            val aspectRatio = intrinsicWidth.toFloat() / intrinsicHeight
            Rect(0, 0, (bounds.height() * aspectRatio).toInt(), bounds.height())
        }
    }
}