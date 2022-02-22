package st235.com.github.seamcarving.data

import android.net.Uri

sealed class StatefulMediaRequest {

    data class Failure(
        val throwable: Throwable? = null
    ): StatefulMediaRequest()

    data class PendingFetch(val uri: Uri): StatefulMediaRequest()

    data class FinishedFetch(val uri: Uri): StatefulMediaRequest()

}