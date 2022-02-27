package st235.com.github.seamcarving.presentation.editor.options.brushes

import androidx.annotation.ColorRes
import st235.com.github.seamcarving.R

@get:ColorRes
val EditorBrush.colorRes: Int
    get() {
        return when (this) {
            EditorBrush.KEEP -> R.color.brush_keep_area
            EditorBrush.REMOVE -> R.color.brush_remove_area
            EditorBrush.CLEAR -> R.color.brush_clear_area
        }
    }
