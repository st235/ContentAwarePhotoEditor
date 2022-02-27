package st235.com.github.seamcarving.presentation.editor.options.sizes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.utils.findViewById

class EditorSizesFragment: Fragment() {

    private val editorViewModel: EditorViewModel by sharedViewModel()

    private lateinit var sizesToggleGroupDelegate: SizesToggleGroupDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_editor_options_sizes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootView = findViewById<ViewGroup>(R.id.root_view)

        sizesToggleGroupDelegate = SizesToggleGroupDelegate(rootView)

        editorViewModel.observeAspectRatios()
            .observe(viewLifecycleOwner) { aspectRatios ->
                sizesToggleGroupDelegate.updateToggles(aspectRatios)
            }

        editorViewModel.loadAspectRatios()
    }
}
