package st235.com.github.seamcarving.presentation.editor.options.quality

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
import st235.com.github.seamcarving.utils.carving.CarvingQualityMode

class EditorQualityModesFragment: Fragment() {

    private val editorViewModel: EditorViewModel by sharedViewModel()

    private lateinit var qualityModesToggleGroupLayout: ToggleGroupLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_editor_options_quality_modes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qualityModesToggleGroupLayout = findViewById(R.id.quality_modes_options_layout)

        editorViewModel.observeSelectedQualityMode()
            .observe(viewLifecycleOwner) { mode ->
                when (mode) {
                    CarvingQualityMode.SPEED -> qualityModesToggleGroupLayout.selectView(R.id.editor_quality_speed)
                    CarvingQualityMode.FIDELITY -> qualityModesToggleGroupLayout.selectView(R.id.editor_option_quality_modes)
                }
            }

        qualityModesToggleGroupLayout.onSelectedListener = { view ->
            val mode = when (view.id) {
                R.id.editor_quality_speed -> CarvingQualityMode.SPEED
                R.id.editor_quality_fidelity -> CarvingQualityMode.FIDELITY
                else -> throw IllegalStateException("Unknown id $id")
            }

            editorViewModel.selectQualityMode(mode)
        }
    }
}