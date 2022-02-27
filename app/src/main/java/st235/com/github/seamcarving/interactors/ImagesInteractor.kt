package st235.com.github.seamcarving.interactors

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.interactors.models.ImageSize
import st235.com.github.seamcarving.utils.BitmapHelper

class ImagesInteractor(
    private val bitmapHelper: BitmapHelper
) {

    private val dispatcher = Dispatchers.IO

    suspend fun loadImageDimensions(uri: Uri): Result<ImageSize> = withContext(dispatcher) {
        val dimensions = bitmapHelper.loadDimensions(uri)
        Result.success(ImageSize(dimensions[0], dimensions[1]))
    }

}