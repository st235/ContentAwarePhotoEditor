package st235.com.github.seamcarving.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.data.GalleryRepository
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.interactors.models.ImageSize
import st235.com.github.seamcarving.utils.media.MediaInfo

class AlbumsInteractor(
    private val galleryRepository: GalleryRepository
) {

    private val dispatcher = Dispatchers.IO

    suspend fun loadCurrentPage() = withContext(dispatcher) {
        galleryRepository.currentImages.map { it.toImageInfo() }
    }

    suspend fun loadNextPage() = withContext(dispatcher) {
        galleryRepository.loadNextPage()
            .map { it.toImageInfo() }
    }

    private fun MediaInfo.toImageInfo(): ImageInfo {
        return ImageInfo(
            id = id,
            name = name,
            description = description,
            size = ImageSize(width, height),
            uri = uri
        )
    }

}