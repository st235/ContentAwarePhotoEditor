package st235.com.github.seamcarving.presentation.editor.options.dimensions

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.interactors.models.AspectRatio
import st235.com.github.seamcarving.presentation.components.AspectRatioDrawable
import st235.com.github.seamcarving.presentation.components.ToggleGroupLayout
import st235.com.github.seamcarving.presentation.editor.options.EditorControlPanelView
import st235.com.github.seamcarving.utils.dp

class AspectRatiosToggleGroupDelegate(
    private val rootView: ViewGroup
) {

    private val ratioToToggleLookup = mutableMapOf<AspectRatio, Int>()
    private val toggleToRatioLookup = mutableMapOf<Int, AspectRatio>()

    private val sizesToggleGroupLayout: ToggleGroupLayout = rootView.findViewById(R.id.sizes_options_toggle_group)

    private val context: Context
    get() {
        return rootView.context
    }

    var onRatioSelected: ((aspectRatio: AspectRatio) -> Unit)? = null

    init {
        sizesToggleGroupLayout.onSelectedListener = { view ->
            val aspectRatio = toggleToRatioLookup.getValue(view.id)
            onRatioSelected?.invoke(aspectRatio)
        }
    }

    fun selectToggle(aspectRatio: AspectRatio) {
        val id = ratioToToggleLookup.getValue(aspectRatio)
        sizesToggleGroupLayout.selectView(id)
    }

    fun updateToggles(aspectRatios: List<AspectRatio>) {
        ratioToToggleLookup.clear()
        toggleToRatioLookup.clear()

        sizesToggleGroupLayout.removeAllViews()

        for (aspectRatio in aspectRatios) {
            val id = View.generateViewId()

            ratioToToggleLookup[aspectRatio] = id
            toggleToRatioLookup[id] = aspectRatio

            val controlPanelView = EditorControlPanelView(ContextThemeWrapper(context, R.style.EditorControlPanelView_SubItem))

            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            controlPanelView.id = id
            controlPanelView.isToggleable = true
            controlPanelView.setIcon(AspectRatioDrawable(aspectRatio, Color.BLACK, 2.5F.dp, 5F.dp), ImageView.ScaleType.FIT_XY)
            controlPanelView.setText(String.format("%d:%d", aspectRatio.width, aspectRatio.height))

            sizesToggleGroupLayout.addView(controlPanelView, layoutParams)
        }
    }

}