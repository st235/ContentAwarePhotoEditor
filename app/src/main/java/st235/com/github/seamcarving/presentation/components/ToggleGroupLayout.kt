package st235.com.github.seamcarving.presentation.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import kotlin.math.max
import kotlin.math.min
import st235.com.github.seamcarving.R

class ToggleGroupLayout: LinearLayout {

    interface Toggleable {
        val isToggleable: Boolean
    }

    private var selectedItem: Int = -1

    var onSelectedListener: ((view: View) -> Unit)? = null
    set(newValue) {
        field = newValue

        if (selectedItem in 0 until childCount) {
            onSelectedListener?.invoke(getChildAt(selectedItem))
        }
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        for (i in 0 until childCount) {
            val view = getChildAt(i)
            prepareView(view, i)
        }

        selectView(findFirstToggleable())
    }

    override fun addView(child: View?) {
        super.addView(child)
        prepareView(child, childCount - 1)
    }

    override fun addView(child: View?, index: Int) {
        super.addView(child, index)
        prepareView(child, childCount - 1)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        super.addView(child, width, height)
        prepareView(child, childCount - 1)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        prepareView(child, childCount - 1)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        prepareView(child, childCount - 1)
    }

    private fun prepareView(view: View?, index: Int) {
        view?.setOnClickListener {
            selectView(index)
        }
    }

    private fun selectView(newIndex: Int) {
        if (newIndex < 0 || newIndex >= childCount) {
            return
        }

        val viewCandidate = getChildAt(newIndex)
        val isToggleable = (viewCandidate as? Toggleable)?.isToggleable ?: false

        if (!isToggleable) {
            onSelectedListener?.invoke(viewCandidate)
            return
        }

        selectedItem = newIndex

        for (i in 0 until childCount) {
            val view = getChildAt(i)

            val isSelected = i == selectedItem
            view.isSelected = isSelected
        }

        onSelectedListener?.invoke(viewCandidate)
    }

    private fun findFirstToggleable(): Int {
        for (i in 0 until childCount) {
            val view = getChildAt(i)

            if ((view as? Toggleable)?.isToggleable == true) {
                return i
            }
        }

        return -1
    }

}