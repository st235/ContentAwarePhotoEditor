package st235.com.github.seamcarving.presentation.editor

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st235.com.github.seamcarving.interactors.AlbumsInteractor
import st235.com.github.seamcarving.interactors.AspectRatiosInteractor
import st235.com.github.seamcarving.interactors.ImagesInteractor
import st235.com.github.seamcarving.interactors.models.AspectRatio
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush
import st235.com.github.seamcarving.presentation.utils.requireValue
import st235.com.github.seamcarving.utils.carving.CarvingQualityMode
import st235.com.github.seamcarving.utils.carving.CarvingResizeMode

class EditorViewModel(
    private val albumsInteractor: AlbumsInteractor,
    private val aspectRatiosInteractor: AspectRatiosInteractor,
    private val imagesInteractor: ImagesInteractor
): ViewModel() {

    private val aspectRatiosLiveData = MutableLiveData<List<AspectRatio>>()

    private val imageLiveData = MutableLiveData<Uri>()
    private val selectedAspectRatio = MutableLiveData<AspectRatio>()
    private val selectedBrushLiveData = MutableLiveData(EditorBrush.KEEP)
    private val selectedResizeLiveData = MutableLiveData(CarvingResizeMode.INCREASE)
    private val selectedQualityLiveData = MutableLiveData(CarvingQualityMode.SPEED)

    fun observeImage(): LiveData<Uri> = imageLiveData

    fun updateImage(imageUri: Uri) {
        imageLiveData.value = imageUri
    }

    fun observeSelectedBrush(): LiveData<EditorBrush> = selectedBrushLiveData

    fun selectBrush(type: EditorBrush) {
        selectedBrushLiveData.value = type
    }

    fun observeSelectedAspectRatio(): LiveData<AspectRatio> = selectedAspectRatio

    fun selectAspectRatio(aspectRatio: AspectRatio) {
        selectedAspectRatio.value = aspectRatio
    }

    fun observeSelectedResizeMode(): LiveData<CarvingResizeMode> = selectedResizeLiveData

    fun selectResizeMode(resizeMode: CarvingResizeMode) {
        selectedResizeLiveData.value = resizeMode
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

            if (aspectRatios.isNotEmpty()) {
                selectedAspectRatio.value = aspectRatios.first()
            }
        }
    }

    fun saveImage(bitmap: Bitmap, matrix: Bitmap?) {
        viewModelScope.launch {
        }
    }

}