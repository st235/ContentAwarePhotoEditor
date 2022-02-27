package st235.com.github.seamcarving.presentation.editor.options

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.components.ToggleGroupLayout
import st235.com.github.seamcarving.utils.dp

class EditorControlPanelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), ToggleGroupLayout.Toggleable {

    private val iconView: AppCompatImageView
    private val textView: AppCompatTextView

    override var isToggleable: Boolean = false
    set(newValue) {
        field = newValue
        invalidate()
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        setPadding(12.dp, 4.dp, 12.dp, 4.dp)

        LayoutInflater.from(context).inflate(R.layout.content_editor_control_panel_view, this)

        iconView = findViewById(R.id.icon_view)
        textView = findViewById(R.id.text_view)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditorControlPanelView)

        isToggleable = typedArray.getBoolean(R.styleable.EditorControlPanelView_toggleable, false)

        val iconRef = typedArray.getResourceId(R.styleable.EditorControlPanelView_ecpv_icon, -1)
        if (iconRef != -1) {
            iconView.setImageResource(iconRef)
        }

        val iconBackgroundRef = typedArray.getResourceId(R.styleable.EditorControlPanelView_ecpv_iconBackground, -1)
        if (iconBackgroundRef != -1) {
            iconView.setBackgroundResource(iconBackgroundRef)
        }

        val iconTintColorState = typedArray.getColorStateList(R.styleable.EditorControlPanelView_ecpv_iconTint)
        iconView.imageTintList = iconTintColorState

        textView.text = typedArray.getString(R.styleable.EditorControlPanelView_ecpv_text)

        typedArray.recycle()
    }

    fun setText(text: String?) {
        textView.text = text
    }

    fun setIcon(image: Drawable, scaleType: ImageView.ScaleType? = null) {
        iconView.setImageDrawable(image)
        scaleType?.let {
            iconView.scaleType = it
        }
    }

    fun setIconBackground(image: Drawable) {
        iconView.background = image
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        textView.isSelected = selected
        iconView.isSelected = selected
    }
}