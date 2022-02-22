package st235.com.github.seamcarving.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.data.StatefulMediaRepository
import st235.com.github.seamcarving.data.StatefulMediaRequest

class StatefulMediaInteractor(
    private val statefulMediaRepository: StatefulMediaRepository
) {

    private val dispatcher = Dispatchers.IO

    private val randomMediaTitle: String
    get() {
        return String.format("media_%d", System.currentTimeMillis())
    }

    suspend fun fetchEmptyFile(): StatefulMediaRequest  = withContext(dispatcher) {
        statefulMediaRepository.fetchEmptyFile(randomMediaTitle)
    }

    suspend fun consumePendingFile(): StatefulMediaRequest = withContext(dispatcher) {
        statefulMediaRepository.consumePendingFile()
    }

}