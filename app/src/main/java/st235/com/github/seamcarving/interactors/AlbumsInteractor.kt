package st235.com.github.seamcarving.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.data.AlbumRepository
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.interactors.models.ImageSize
import st235.com.github.seamcarving.utils.media.MediaScanner

class AlbumsInteractor(
    private val albumRepository: AlbumRepository
) {

    private val dispatcher = Dispatchers.IO

    suspend fun resetPages() = withContext(dispatcher) {
        albumRepository.reset()
    }

    suspend fun loadNextPage() = withContext(dispatcher) {
        albumRepository.loadNextPage()
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