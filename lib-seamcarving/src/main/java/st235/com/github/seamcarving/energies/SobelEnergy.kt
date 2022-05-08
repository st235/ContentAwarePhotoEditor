package st235.com.github.seamcarving.energies

import android.graphics.Color
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.images.CarvableImage

internal class SobelEnergy: Energy() {

    private companion object {

        val GX = arrayOf(
            intArrayOf(1, 0, -1),
            intArrayOf(2, 0, -2),
            intArrayOf(1, 0, -1)
        )

        val GY = arrayOf(
            intArrayOf(1, 2, 1),
            intArrayOf(0, 0, 0),
            intArrayOf(-1, -2, -1)
        )

    }


    override fun energyAt(i: Int, j: Int, image: CarvableImage): Long {
        if (i == 0 || i == image.height - 1 || j == 0 || j == image.width - 1) {
            return MAX_POSSIBLE_ENERGY
        }

        var dx = 0.0
        var dy = 0.0

        for (di in 0 until 3) {
            for (dj in 0 until 3) {
                val gxFactor = GX[di][dj]
                val gyFactor = GY[di][dj]

                val pixel = image.getPixelAt(i - 1 + di, j - 1 + dj)

                dx += gxFactor * (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel))
                dy += gyFactor * (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel))
            }
        }

        return sqrt(dx.pow(2.0) + dy.pow(2.0)).toLong()
    }

}