package st235.com.github.seamcarving.presentation.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st235.com.github.seamcarving.data.StatefulMediaRequest
import st235.com.github.seamcarving.interactors.GalleryInteractor
import st235.com.github.seamcarving.interactors.StatefulMediaInteractor
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.utils.MutableLiveEvent

class GalleryViewModel(
    private val galleryInteractor: GalleryInteractor,
    private val statefulMediaInteractor: StatefulMediaInteractor
): ViewModel() {

    private val imagesLiveData = MutableLiveData<List<ImageInfo>>()

    private val mediaRequestsData = MutableLiveEvent<StatefulMediaRequest>()

    fun observeAlbumImages(): LiveData<List<ImageInfo>> = imagesLiveData

    fun observeMediaRequests(): LiveData<StatefulMediaRequest> = mediaRequestsData

    fun requestEmptyFile() {
        viewModelScope.launch {
            mediaRequestsData.setValue(statefulMediaInteractor.fetchEmptyFile())
        }
    }

    fun consumePendingFile() {
        viewModelScope.launch {
            mediaRequestsData.setValue(statefulMediaInteractor.consumePendingFile())
        }
    }

    fun obtainCurrentAlbumPage() {
        viewModelScope.launch {
            val items = galleryInteractor.loadCurrentPage()
            imagesLiveData.value = items
        }
    }

    fun loadNextAlbumPage() {
        viewModelScope.launch {
            val items = galleryInteractor.loadNextPage()
            imagesLiveData.value = items
        }
    }

}