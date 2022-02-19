package st235.com.github.seamcarving.interactors

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.utils.media.MediaScanner
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.interactors.models.ImageSize
import st235.com.github.seamcarving.utils.media.MediaSaver

class GalleryInteractor(
    private val mediaScanner: MediaScanner,
    private val mediaSaver: MediaSaver
) {

    suspend fun loadImages(
        album: String
    ): List<ImageInfo> = withContext(Dispatchers.IO) {
        mediaScanner.loadFiles(album)
            .map {
                ImageInfo(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    size = ImageSize(it.width, it.height),
                    uri = it.uri
                )
            }
    }

    suspend fun saveImage(
        album: String,
        title: String,
        description: String? = null,
        bitmap: Bitmap? = null
    ) = withContext(Dispatchers.IO) {
        mediaSaver.save(album, title, description, bitmap)
    }

}