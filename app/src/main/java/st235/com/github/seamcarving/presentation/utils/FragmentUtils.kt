package st235.com.github.seamcarving.presentation.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun <T: View> Fragment.findViewById(@IdRes viewId: Int): T {
    return requireView().findViewById(viewId)
}
