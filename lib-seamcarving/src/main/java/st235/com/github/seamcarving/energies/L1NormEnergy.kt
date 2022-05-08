package st235.com.github.seamcarving.energies

import android.graphics.Color
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.images.CarvableImage
import st235.com.github.seamcarving.utils.Vector3

internal class L1NormEnergy: Energy() {

    override fun energyAt(i: Int, j: Int, image: CarvableImage): Long {
        if (i == 0 || i == image.height - 1 || j == 0 || j == image.width - 1) {
            return MAX_POSSIBLE_ENERGY
        }

        val right = Vector3.fromColor(image.getPixelAt(i, j + 1))
        val left = Vector3.fromColor(image.getPixelAt(i, j - 1))

        val top = Vector3.fromColor(image.getPixelAt(i + 1, j))
        val down = Vector3.fromColor(image.getPixelAt(i - 1, j))

        val dx = right.subtract(left)
        val dy = top.subtract(down)

        return (dx.l1Norm() + dy.l1Norm()).toLong()
    }

}