package st235.com.github.seamcarving.presentation.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush

class EditorViewModel: ViewModel() {

    private val brushTypeLiveData = MutableLiveData<EditorBrush>()

    fun updateEditorBrush(type: EditorBrush) {
        brushTypeLiveData.value = type
    }

    fun observeBrushType(): LiveData<EditorBrush> = brushTypeLiveData

}