package st235.com.github.seamcarving.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IdRes

class ToggleGroupLayout : LinearLayout {

    interface Toggleable {
        val isToggleable: Boolean
    }

    @IdRes
    private var selectedViewId: Int = -1

    val selectedView: View
    get() {
        return findViewById(selectedViewId)
    }

    var onSelectedListener: ((view: View) -> Unit)? = null

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
            prepareView(view)
        }

        selectView(findFirstToggleable())
    }

    override fun addView(child: View?) {
        super.addView(child)
        prepareView(child)
    }

    override fun addView(child: View?, index: Int) {
        super.addView(child, index)
        prepareView(child)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        super.addView(child, width, height)
        prepareView(child)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        prepareView(child)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        prepareView(child)
    }

    private fun prepareView(view: View?) {
        view?.setOnClickListener {
            selectView(view.id)
        }
    }

    fun selectView(@IdRes viewId: Int) {
        if (selectedViewId == viewId) {
            return
        }

        val viewCandidate = findViewById<View>(viewId)

        if (viewCandidate == null) {
            return
        }

        val isToggleable = (viewCandidate as? Toggleable)?.isToggleable ?: false

        if (!isToggleable) {
            onSelectedListener?.invoke(viewCandidate)
            return
        }

        selectedViewId = viewId

        for (i in 0 until childCount) {
            val view = getChildAt(i)

            val isSelected = view.id == selectedViewId
            view.isSelected = isSelected
        }

        onSelectedListener?.invoke(viewCandidate)
    }

    private fun findFirstToggleable(): Int {
        for (i in 0 until childCount) {
            val view = getChildAt(i)

            if ((view as? Toggleable)?.isToggleable == true) {
                return view.id
            }
        }

        return -1
    }

}