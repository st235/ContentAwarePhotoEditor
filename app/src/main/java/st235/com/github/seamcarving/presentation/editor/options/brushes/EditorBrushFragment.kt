package st235.com.github.seamcarving.presentation.editor.options.brushes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.components.ToggleGroupLayout
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.utils.findViewById

class EditorBrushFragment: Fragment() {

    private val editorViewModel: EditorViewModel by sharedViewModel()

    private lateinit var brushOptionsLayout: ToggleGroupLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_editor_options_brush, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        brushOptionsLayout = findViewById(R.id.brush_options_layout)

        brushOptionsLayout.onSelectedListener = { view ->
            when (view.id) {
                R.id.editor_brush_keep -> editorViewModel.updateEditorBrush(EditorBrush.KEEP)
                R.id.editor_brush_remove -> editorViewModel.updateEditorBrush(EditorBrush.REMOVE)
                R.id.editor_brush_erase -> editorViewModel.updateEditorBrush(EditorBrush.CLEAR)
            }
        }
    }
}