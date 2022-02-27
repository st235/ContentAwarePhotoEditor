package st235.com.github.seamcarving.presentation.editor

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st235.com.github.seamcarving.interactors.GalleryInteractor
import st235.com.github.seamcarving.interactors.AspectRatiosInteractor
import st235.com.github.seamcarving.interactors.CarvingInteractor
import st235.com.github.seamcarving.interactors.ImagesInteractor
import st235.com.github.seamcarving.interactors.models.AspectRatio
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush
import st235.com.github.seamcarving.presentation.utils.requireValue
import st235.com.github.seamcarving.utils.MutableLiveEvent
import st235.com.github.seamcarving.utils.carving.CarvingQualityMode
import st235.com.github.seamcarving.utils.carving.CarvingRequest
import st235.com.github.seamcarving.utils.carving.CarvingResizeMode

class EditorViewModel(
    private val galleryInteractor: GalleryInteractor,
    private val carvingInteractor: CarvingInteractor,
    private val aspectRatiosInteractor: AspectRatiosInteractor,
    private val imagesInteractor: ImagesInteractor
): ViewModel() {

    private val aspectRatiosLiveData = MutableLiveData<List<AspectRatio>>()

    private val imageLiveData = MutableLiveData<Uri>()
    private val selectedBrushLiveData = MutableLiveData(EditorBrush.KEEP)
    private val selectedAspectRatio = MutableLiveData<AspectRatio?>(
        null /* null cause resize mode is keep by default */
    )
    private val selectedResizeLiveData = MutableLiveData(CarvingResizeMode.KEEP)
    private val selectedQualityLiveData = MutableLiveData(CarvingQualityMode.SPEED)

    private val imageStatusLiveData = MutableLiveEvent(ImageStatus.IDLING)

    fun observeImage(): LiveData<Uri> = imageLiveData

    fun updateImage(imageUri: Uri) {
        imageLiveData.value = imageUri
    }

    fun observeSelectedBrush(): LiveData<EditorBrush> = selectedBrushLiveData

    fun selectBrush(type: EditorBrush) {
        selectedBrushLiveData.value = type
    }

    fun observeSelectedAspectRatio(): LiveData<AspectRatio?> = selectedAspectRatio

    fun selectAspectRatio(aspectRatio: AspectRatio) {
        selectedAspectRatio.value = aspectRatio
    }

    fun observeSelectedResizeMode(): LiveData<CarvingResizeMode> = selectedResizeLiveData

    fun selectResizeMode(resizeMode: CarvingResizeMode) {
        selectedResizeLiveData.value = resizeMode

        val oldValue = selectedAspectRatio.value
        selectedAspectRatio.value = when {
            resizeMode == CarvingResizeMode.KEEP -> null
            resizeMode == CarvingResizeMode.RETARGET && oldValue != null -> oldValue
            else -> findFirstAspectRatio()
        }
    }

    fun observeSelectedQualityMode(): LiveData<CarvingQualityMode> = selectedQualityLiveData

    fun selectQualityMode(qualityMode: CarvingQualityMode) {
        selectedQualityLiveData.value = qualityMode
    }

    fun observeAspectRatios(): LiveData<List<AspectRatio>> = aspectRatiosLiveData

    fun loadAspectRatios() {
        viewModelScope.launch {
            val imageUri = imageLiveData.requireValue()
            val imageDimensionsResult = imagesInteractor.loadImageDimensions(imageUri)
            val dimension = imageDimensionsResult.getOrThrow()
            val aspectRatio = AspectRatio(dimension.width, dimension.height)

            val aspectRatios = aspectRatiosInteractor.getAvailableAspectRatiosForImage(aspectRatio)
            aspectRatiosLiveData.value = aspectRatios

            selectedAspectRatio.value = when (selectedResizeLiveData.requireValue()) {
                CarvingResizeMode.RETARGET -> findFirstAspectRatio()
                CarvingResizeMode.KEEP -> null
            }
        }
    }

    fun observeImageStatus(): LiveData<ImageStatus> = imageStatusLiveData

    private fun findFirstAspectRatio(): AspectRatio? {
        return aspectRatiosLiveData.value?.firstOrNull()
    }

    fun saveImage(filterMask: Bitmap) {
        viewModelScope.launch {
            val carvingRequest = CarvingRequest.Builder()
                .image(imageLiveData.requireValue())
                .aspectRatio(selectedAspectRatio.value)
                .filterMask(filterMask)
                .qualityMode(selectedQualityLiveData.requireValue())
                .resizeMode(selectedResizeLiveData.requireValue())
                .build()

            val image = carvingInteractor.process(carvingRequest)
            val uri = galleryInteractor.saveToGallery(image)

            imageStatusLiveData.setValue(ImageStatus.FINISHED)
        }
    }

}