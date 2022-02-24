package st235.com.github.seamcarving.presentation.editor

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st235.com.github.seamcarving.interactors.AlbumsInteractor
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush

class EditorViewModel(
    private val albumsInteractor: AlbumsInteractor
): ViewModel() {

    private val brushTypeLiveData = MutableLiveData<EditorBrush>()

    fun updateEditorBrush(type: EditorBrush) {
        brushTypeLiveData.value = type
    }

    fun observeBrushType(): LiveData<EditorBrush> = brushTypeLiveData

    fun saveImage(bitmap: Bitmap, matrix: Bitmap?) {
        viewModelScope.launch {
        }
    }

}