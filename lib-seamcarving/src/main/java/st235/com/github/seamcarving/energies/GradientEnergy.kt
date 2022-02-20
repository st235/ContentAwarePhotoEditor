package st235.com.github.seamcarving.energies

import android.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.images.CarvableImage

internal class GradientEnergy: Energy() {

    override fun energyAt(i: Int, j: Int, image: CarvableImage): Long {
        if (i == 0 || i == image.height - 1 || j == 0 || j == image.width - 1) {
            return MAX_POSSIBLE_ENERGY
        }

        val right = image.getPixelAt(i, j + 1)
        val left = image.getPixelAt(i, j - 1)

        val top = image.getPixelAt(i + 1, j)
        val down = image.getPixelAt(i - 1, j)

        val dx = (Color.red(right) - Color.red(left)).toDouble().pow(2) +
                (Color.green(right) - Color.green(left)).toDouble().pow(2) +
                (Color.blue(right) - Color.blue(left)).toDouble().pow(2)

        val dy = (Color.red(top) - Color.red(down)).toDouble().pow(2) +
                (Color.green(top) - Color.green(down)).toDouble().pow(2) +
                (Color.blue(top) - Color.blue(down)).toDouble().pow(2)

        return sqrt(dx + dy).toLong()
    }

}