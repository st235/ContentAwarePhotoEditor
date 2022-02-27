package st235.com.github.seamcarving.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import kotlin.math.abs
import kotlin.math.sqrt

@ColorInt
fun Int.distance(that: Int): Int {

    val dr = abs(Color.red(this) - Color.red(that))
    val dg = abs(Color.green(this) - Color.green(that))
    val db = abs(Color.blue(this) - Color.blue(that))

    return sqrt((dr * dr + dg * dg + db * db).toDouble()).toInt()
}
