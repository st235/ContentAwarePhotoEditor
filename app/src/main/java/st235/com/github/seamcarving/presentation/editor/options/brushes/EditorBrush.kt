package st235.com.github.seamcarving.presentation.editor.options.brushes

import android.graphics.Color
import androidx.annotation.ColorInt

enum class EditorBrush(
    @ColorInt
    val color: Int
) {
    KEEP(Color.argb((0.7 * 255).toInt(), 0, 255, 0)),
    REMOVE(Color.argb((0.7 * 255).toInt(), 255, 0, 0)),
    CLEAR(Color.TRANSPARENT)
}
