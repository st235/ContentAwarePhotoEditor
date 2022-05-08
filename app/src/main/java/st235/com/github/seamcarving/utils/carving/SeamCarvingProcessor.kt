package st235.com.github.seamcarving.utils.carving

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.SeamCarver
import st235.com.github.seamcarving.interactors.models.AspectRatio
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush
import st235.com.github.seamcarving.presentation.editor.options.brushes.colorRes
import st235.com.github.seamcarving.utils.carving.SeamCarvingHelper.countRemovedAreas
import st235.com.github.seamcarving.utils.carving.SeamCarvingHelper.filterColors

@WorkerThread
class SeamCarvingProcessor(
    private val context: Context
) {

    private companion object {
        const val MAX_SIZE = 1024

        const val NO_VALUE = Energy.MASK_NO_VALUE
        const val REMOVE_VALUE = Energy.MASK_MINUS_INF
        const val KEEP_VALUE = Energy.MASK_INF
    }

    private val fidelitySeamCarving = SeamCarver.create(type = SeamCarver.Type.FIDELITY)
    private val speedSeamCarving = SeamCarver.create(type = SeamCarver.Type.SPEED)

    fun processRequest(carvingRequest: CarvingRequest): Bitmap {
        val imageUri = carvingRequest.image

        val carver = when (carvingRequest.qualityMode) {
            CarvingQualityMode.FIDELITY -> fidelitySeamCarving
            CarvingQualityMode.SPEED -> speedSeamCarving
        }

        val image = loadBitmap(imageUri)

        val originWidth = image.width
        val originalHeight = image.height
        val targetSize = findSuitableSize(originWidth, originalHeight, carvingRequest.resizeMode, carvingRequest.aspectRatio)

        val bitmapFilter = carvingRequest.filterMask
        val filterMap = bitmapFilter.filterColors(
            image.width,
            image.height,
            getColorMap()
        )

        val removeArea = filterMap.countRemovedAreas(REMOVE_VALUE)
        val removeWidth = removeArea[0]
        val removeHeight = removeArea[1]

        val resizingWidth = targetSize[0] != originWidth

        val interimCarvingResult = if (removeHeight == 0 && removeWidth == 0) {
            SeamCarver.CarvingResult(image, filterMap)
        } else if (resizingWidth) {
            carver.retarget(image, filterMap, image.width - removeWidth, image.height)
        } else {
            carver.retarget(image, filterMap, image.width, image.height - removeHeight)
        }

        val interimImage = interimCarvingResult.bitmap
        val interimMask = interimCarvingResult.mask

        val finalCarvingResult = carver.retarget(interimImage, interimMask, targetSize[0], targetSize[1])
        return finalCarvingResult.bitmap
    }

    private fun findSuitableSize(width: Int, height: Int, resizeMode: CarvingResizeMode, aspectRatio: AspectRatio?): IntArray {
        return when (resizeMode) {
            CarvingResizeMode.KEEP -> intArrayOf(width, height)
            CarvingResizeMode.RETARGET -> findRetargetSize(width, height, aspectRatio)
        }
    }

    private fun findRetargetSize(width: Int, height: Int, aspectRatio: AspectRatio?): IntArray {
        if (aspectRatio == null) {
            throw IllegalArgumentException("AspectRatio cannot be null")
        }

        return if (width < height) {
            val newHeight = aspectRatio.calculateHeight(width)
            intArrayOf(width, newHeight)
        } else {
            val newWidth = aspectRatio.calculateWidth(height)
            intArrayOf(newWidth, height)
        }
    }

    private fun getColorMap(): Map<Int, Int> {
        return mapOf(
            ContextCompat.getColor(context, EditorBrush.KEEP.colorRes) to KEEP_VALUE,
            ContextCompat.getColor(context, EditorBrush.REMOVE.colorRes) to REMOVE_VALUE,
            ContextCompat.getColor(context, EditorBrush.CLEAR.colorRes) to NO_VALUE
        )
    }

    private fun loadBitmap(imageUri: Uri): Bitmap {
        return Glide.with(context)
            .asBitmap()
            .load(imageUri)
            .override(MAX_SIZE, MAX_SIZE)
            .submit()
            .get()
    }

}