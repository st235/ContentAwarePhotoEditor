package st235.com.github.seamcarving.presentation.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st235.com.github.seamcarving.interactors.GalleryInteractor
import st235.com.github.seamcarving.interactors.models.ImageInfo

class GalleryViewModel(
    private val galleryInteractor: GalleryInteractor
): ViewModel() {

    private val imagesLiveData = MutableLiveData<List<ImageInfo>>()

    fun observeImages(): LiveData<List<ImageInfo>> = imagesLiveData

    fun loadImages() {
        viewModelScope.launch {
            val items = galleryInteractor.loadImages(".*")
            imagesLiveData.value = items
        }
    }

}