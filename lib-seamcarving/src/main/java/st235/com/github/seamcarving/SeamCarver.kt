package st235.com.github.seamcarving

import android.graphics.Bitmap
import st235.com.github.seamcarving.algorithms.SimpleDynamicSeamCarver
import st235.com.github.seamcarving.energies.GradientEnergy
import st235.com.github.seamcarving.images.CarvableImage

interface SeamCarver {

    enum class Type {
        FIDELITY,
        SPEED
    }

    fun retarget(
        image: Bitmap,
        mask: Array<IntArray>?,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap

    companion object {

        fun create(
            energy: Energy = GradientEnergy(),
            type: Type = Type.FIDELITY
        ): SeamCarver {
            return when(type) {
                Type.FIDELITY -> SimpleDynamicSeamCarver(energy)
                Type.SPEED -> throw IllegalArgumentException()
            }
        }

    }

}