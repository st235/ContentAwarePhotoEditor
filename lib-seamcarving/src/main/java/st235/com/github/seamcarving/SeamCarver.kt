package st235.com.github.seamcarving

import android.graphics.Bitmap
import st235.com.github.seamcarving.algorithms.FastSeamCarver
import st235.com.github.seamcarving.algorithms.SimpleDynamicSeamCarver
import st235.com.github.seamcarving.energies.SobelEnergy

interface SeamCarver {

    enum class Type {
        FIDELITY,
        SPEED
    }

    data class CarvingResult(
        val bitmap: Bitmap,
        val mask: Array<IntArray>?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as CarvingResult

            if (bitmap != other.bitmap) return false
            if (mask != null) {
                if (other.mask == null) return false
                if (!mask.contentDeepEquals(other.mask)) return false
            } else if (other.mask != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = bitmap.hashCode()
            result = 31 * result + (mask?.contentDeepHashCode() ?: 0)
            return result
        }
    }

    fun retarget(
        image: Bitmap,
        mask: Array<IntArray>?,
        targetWidth: Int,
        targetHeight: Int
    ): CarvingResult

    companion object {

        fun create(
            energy: Energy = SobelEnergy(),
            type: Type = Type.FIDELITY
        ): SeamCarver {
            return when(type) {
                Type.FIDELITY -> SimpleDynamicSeamCarver(energy)
                Type.SPEED -> FastSeamCarver(energy)
            }
        }

    }

}