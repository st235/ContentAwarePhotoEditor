package st235.com.github.seamcarving.utils

import kotlin.math.abs

fun Double.compareWithTolerance(that: Double): Boolean {
    val diff = abs(this - that)
    return diff < 1E-7
}
