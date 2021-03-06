package st235.com.github.seamcarving

import st235.com.github.seamcarving.images.CarvableImage

abstract class Energy {

    internal fun calculateAt(i: Int, j: Int, image: CarvableImage): Long {
        val maskValue = image.getMaskPixel(i, j) ?: MASK_NO_VALUE

        return when (maskValue) {
            MASK_NO_VALUE -> energyAt(i, j, image)
            MASK_INF ->  MAX_ENERGY
            MASK_MINUS_INF -> MIN_ENERGY
            else -> throw IllegalStateException("Unexpected mask value $maskValue")
        }
    }

    protected abstract fun energyAt(i: Int, j: Int, image: CarvableImage): Long

    companion object {

        const val MIN_ENERGY = -1000L
        const val MAX_POSSIBLE_ENERGY = 1000L
        const val MAX_ENERGY = Integer.MAX_VALUE.toLong()

        const val MASK_INF = 1
        const val MASK_MINUS_INF = -1
        const val MASK_NO_VALUE = 0

    }

}