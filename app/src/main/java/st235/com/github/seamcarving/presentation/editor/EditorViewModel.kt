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

class EditorViewModel(
    private val albumsInteractor: AlbumsInteractor,
    private val aspectRatiosInteractor: AspectRatiosInteractor,
    private val imagesInteractor: ImagesInteractor
): ViewModel() {

    private val imageLiveData = MutableLiveData<Uri>()
    private val brushTypeLiveData = MutableLiveData<EditorBrush>()
    private val aspectRatiosLiveData = MutableLiveData<List<AspectRatio>>()

    fun observeImage(): LiveData<Uri> = imageLiveData

    fun updateImage(imageUri: Uri) {
        imageLiveData.value = imageUri
    }

    fun observeBrushType(): LiveData<EditorBrush> = brushTypeLiveData

    fun updateEditorBrush(type: EditorBrush) {
        brushTypeLiveData.value = type
    }

    fun observeAspectRatios(): LiveData<List<AspectRatio>> = aspectRatiosLiveData

    fun loadAspectRatios() {
        viewModelScope.launch {
            val imageUri = imageLiveData.requireValue()
            val imageDimensionsResult = imagesInteractor.loadImageDimensions(imageUri)
            val dimension = imageDimensionsResult.getOrThrow()
            val aspectRatio = AspectRatio(dimension.width, dimension.height)

            aspectRatiosLiveData.value = aspectRatiosInteractor.getAvailableAspectRatiosForImage(aspectRatio)
        }
    }

    fun saveImage(bitmap: Bitmap, matrix: Bitmap?) {
        viewModelScope.launch {
        }
    }

}