package st235.com.github.seamcarving.data

import android.net.Uri
import st235.com.github.seamcarving.utils.media.MediaSaver

class StatefulMediaRepository(
    private val mediaSaver: MediaSaver
) {

    private companion object {
        const val EMPTY_ALBUM = ""
    }

    @field:Volatile
    private var pendingUri: Uri? = null

    @get:Synchronized
    val isPending: Boolean
    get() {
        return pendingUri == null
    }

    @Synchronized
    fun fetchEmptyFile(title: String): StatefulMediaRequest {
        val uri = mediaSaver.save(EMPTY_ALBUM, title, null, null)

        if (uri == null) {
            return StatefulMediaRequest.Failure()
        }

        pendingUri = uri
        return StatefulMediaRequest.PendingFetch(uri)
    }

    @Synchronized
    fun consumePendingFile(): StatefulMediaRequest {
        val uri = pendingUri

        if (uri == null) {
            return StatefulMediaRequest.Failure(
                IllegalStateException("There is no pending file to consume")
            )
        }

        pendingUri = null

        return StatefulMediaRequest.FinishedFetch(uri)
    }

}