package st235.com.github.seamcarving.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.utils.media.MediaScanner
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.interactors.models.ImageSize

class GalleryInteractor(
    private val mediaScanner: MediaScanner
) {

    suspend fun loadImages(): List<ImageInfo> = withContext(Dispatchers.IO) {
        mediaScanner.loadFiles()
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

}