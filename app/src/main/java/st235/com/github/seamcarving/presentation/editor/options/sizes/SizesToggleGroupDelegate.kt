package st235.com.github.seamcarving.presentation.editor.options.sizes

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.interactors.models.AspectRatio
import st235.com.github.seamcarving.presentation.components.AspectRatioDrawable
import st235.com.github.seamcarving.presentation.components.ToggleGroupLayout
import st235.com.github.seamcarving.presentation.editor.options.EditorControlPanelView
import st235.com.github.seamcarving.utils.dp

class SizesToggleGroupDelegate(
    private val rootView: ViewGroup
) {

    private val sizesToggleGroupLayout: ToggleGroupLayout = rootView.findViewById(R.id.sizes_options_toggle_group)

    private val context: Context
    get() {
        return rootView.context
    }

    fun updateToggles(aspectRatios: List<AspectRatio>) {
        sizesToggleGroupLayout.removeAllViews()

        for (aspectRatio in aspectRatios) {
            val controlPanelView = EditorControlPanelView(ContextThemeWrapper(context, R.style.EditorControlPanelView_SubItem))

            val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            controlPanelView.isToggleable = true
            controlPanelView.setIcon(AspectRatioDrawable(aspectRatio, Color.BLACK, 2.5F.dp, 5F.dp), ImageView.ScaleType.FIT_XY)
            controlPanelView.setText(String.format("%d:%d", aspectRatio.width, aspectRatio.height))

            sizesToggleGroupLayout.addView(controlPanelView, layoutParams)
        }
    }

}