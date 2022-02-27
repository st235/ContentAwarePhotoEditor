package st235.com.github.seamcarving.presentation.editor.options.resize

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
import st235.com.github.seamcarving.utils.carving.CarvingResizeMode

class EditorResizeModesFragment: Fragment() {

    private val editorViewModel: EditorViewModel by sharedViewModel()

    private lateinit var resizeModesToggleGroupLayout: ToggleGroupLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_editor_options_resize_modes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resizeModesToggleGroupLayout = findViewById(R.id.resize_modes_options_layout)

        editorViewModel.observeSelectedResizeMode()
            .observe(viewLifecycleOwner) { mode ->
                when (mode) {
                    CarvingResizeMode.INCREASE -> resizeModesToggleGroupLayout.selectView(R.id.editor_modes_increase)
                    CarvingResizeMode.DECREASE -> resizeModesToggleGroupLayout.selectView(R.id.editor_modes_decrease)
                }
            }

        resizeModesToggleGroupLayout.onSelectedListener = { view ->
            val mode = when (view.id) {
                R.id.editor_modes_increase -> CarvingResizeMode.INCREASE
                R.id.editor_modes_decrease -> CarvingResizeMode.DECREASE
                else -> throw IllegalStateException("Unknown id $id")
            }

            editorViewModel.selectResizeMode(mode)
        }
    }
}