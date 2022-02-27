package st235.com.github.seamcarving.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.utils.carving.CarvingRequest
import st235.com.github.seamcarving.utils.carving.SeamCarvingProcessor

class CarvingInteractor(
    private val seamCarvingProcessor: SeamCarvingProcessor
) {

    private val dispatcher = Dispatchers.IO

    suspend fun process(carvingRequest: CarvingRequest) = withContext(dispatcher) {
        seamCarvingProcessor.processRequest(carvingRequest)
    }

}