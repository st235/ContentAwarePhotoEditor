package st235.com.github.seamcarving.presentation.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import st235.com.github.seamcarving.presentation.editor.options.EditorHomeOptions
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush

class EditorViewModel: ViewModel() {

    private val optionScreenLiveData = MutableLiveData<EditorHomeOptions>()

    private val brushTypeLiveData = MutableLiveData<EditorBrush>()

    fun updateOptionScreenData(type: EditorHomeOptions) {
        optionScreenLiveData.value = type
    }

    fun observeOptionScreenData(): LiveData<EditorHomeOptions> = optionScreenLiveData

    fun updateEditorBrush(type: EditorBrush) {
        brushTypeLiveData.value = type
    }

    fun observeBrushType(): LiveData<EditorBrush> = brushTypeLiveData

}