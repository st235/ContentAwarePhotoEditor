package st235.com.github.seamcarving.presentation.editor.options.dimensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.utils.findViewById

class EditorDimensionsFragment: Fragment() {

    private val editorViewModel: EditorViewModel by sharedViewModel()

    private lateinit var aspectRatiosToggleGroupDelegate: AspectRatiosToggleGroupDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_editor_options_dimensions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootView = findViewById<ViewGroup>(R.id.root_view)

        aspectRatiosToggleGroupDelegate = AspectRatiosToggleGroupDelegate(rootView)

        aspectRatiosToggleGroupDelegate.onRatioSelected = { aspectRatio ->
            editorViewModel.selectAspectRatio(aspectRatio)
        }

        editorViewModel.observeAspectRatios()
            .observe(viewLifecycleOwner) { aspectRatios ->
                aspectRatiosToggleGroupDelegate.updateToggles(aspectRatios)
            }

        editorViewModel.observeSelectedAspectRatio()
            .observe(viewLifecycleOwner) { aspectRatio ->
                aspectRatio?.let { aspectRatiosToggleGroupDelegate.selectToggle(aspectRatio) }
            }
    }
}
