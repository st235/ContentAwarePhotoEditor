package st235.com.github.seamcarving.data

import st235.com.github.seamcarving.utils.media.MediaInfo
import st235.com.github.seamcarving.utils.media.MediaScanner

class AlbumRepository(
    private val mediaScanner: MediaScanner
) {

    private companion object {
        const val ALBUM_TITLE = ".*"
        const val PAGE_LIMIT = 40
    }

    @Volatile
    private var page: Int = 0

    private val images = mutableListOf<MediaInfo>()

    @Synchronized
    fun loadNextPage(): List<MediaInfo> {
        val nextPage = mediaScanner.load(ALBUM_TITLE, MediaScanner.Page(PAGE_LIMIT, page * PAGE_LIMIT))

        page += 1

        images.addAll(nextPage)

        val imagesCopy = mutableListOf<MediaInfo>()
        imagesCopy.addAll(images)
        return imagesCopy
    }

    @Synchronized
    fun reset() {
        page = 0
        images.clear()
    }

}
