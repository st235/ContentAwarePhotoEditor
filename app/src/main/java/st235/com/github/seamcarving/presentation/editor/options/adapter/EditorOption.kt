package st235.com.github.seamcarving.presentation.editor.options.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

data class EditorOption(
    @IdRes val id: Int,
    @DrawableRes val icon: Int,
    @StringRes val text: Int
)
