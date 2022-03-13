package st235.com.github.seamcarving.data

import android.graphics.Bitmap
import android.net.Uri
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
    val currentMedia: List<MediaInfo>
    get() {
        val imagesCopy = mutableListOf<MediaInfo>()
        imagesCopy.addAll(images.values)
        return imagesCopy
    }

    private val images = mutableMapOf<String, MediaInfo>()

    @Synchronized
    fun findById(id: String): MediaInfo? {
        return images[id]
    }

    @Synchronized
    fun save(title: String, origin: Bitmap): Uri? {
        val uri = mediaSaver.save(ALBUM_TITLE, title, null, origin)
        reset()
        loadNextPage()
        return uri
    }

    @Synchronized
    fun loadNextPage(): List<MediaInfo> {
        val nextPage = mediaScanner.load(ALBUM_TITLE, MediaScanner.Page(PAGE_LIMIT, page * PAGE_LIMIT))
        page += 1
        for (mediaInfo in nextPage) {
            images[mediaInfo.id] = mediaInfo
        }
        return currentMedia
    }

    @Synchronized
    fun remove(id: String) {
        val mediaInfo = images.getValue(id)
        mediaSaver.delete(mediaInfo.uri)
        images.remove(id)
    }

    @Synchronized
    fun reset() {
        page = 0
        images.clear()
    }

}
