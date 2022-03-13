package st235.com.github.seamcarving.presentation.utils

import androidx.lifecycle.LiveData

fun <T: Any, V: LiveData<T>> V.requireValue(): T {
    return value ?: throw IllegalStateException("The required value was null")
}
