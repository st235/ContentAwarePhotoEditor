package st235.com.github.seamcarving.data

import android.graphics.Bitmap
import st235.com.github.seamcarving.utils.media.MediaInfo
import st235.com.github.seamcarving.utils.media.MediaSaver
import st235.com.github.seamcarving.utils.media.MediaScanner

class GalleryRepository(
    private val mediaScanner: MediaScanner,
    private val mediaSaver: MediaSaver
) {

    private companion object {
        const val ALBUM_TITLE = ".*"
        const val PAGE_LIMIT = 40
    }

    @Volatile
    private var page: Int = 0

    @get:Synchronized
    val currentImages: List<MediaInfo>
    get() {
        val imagesCopy = mutableListOf<MediaInfo>()
        imagesCopy.addAll(images)
        return imagesCopy
    }

    private val images = mutableListOf<MediaInfo>()

    @Synchronized
    fun save(title: String, origin: Bitmap) {
        mediaSaver.save(ALBUM_TITLE, title, null, origin)
        reset()
    }

    @Synchronized
    fun loadNextPage(): List<MediaInfo> {
        val nextPage = mediaScanner.load(ALBUM_TITLE, MediaScanner.Page(PAGE_LIMIT, page * PAGE_LIMIT))
        page += 1
        images.addAll(nextPage)
        return currentImages
    }

    @Synchronized
    fun reset() {
        page = 0
        images.clear()
    }

}
