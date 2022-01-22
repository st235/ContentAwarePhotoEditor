package st235.com.github.seamcarving

import st235.com.github.seamcarving.images.CarvableImage

interface Energy {

    fun calculateAt(i: Int, j: Int, image: CarvableImage): Int

}