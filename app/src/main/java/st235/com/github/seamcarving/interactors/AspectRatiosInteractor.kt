package st235.com.github.seamcarving.interactors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.data.AspectRatiosRepository
import st235.com.github.seamcarving.interactors.models.AspectRatio

class AspectRatiosInteractor(
    private val aspectRatiosRepository: AspectRatiosRepository
) {

    private val dispatcher = Dispatchers.IO

    suspend fun getAvailableAspectRatiosForImage(imageAspectRatio: AspectRatio): List<AspectRatio> =
        withContext(dispatcher) {
            val availableAspectRatios = aspectRatiosRepository.aspectRatios
            availableAspectRatios.map { AspectRatio(it.width, it.height) }.filter { imageAspectRatio != it }
        }

}