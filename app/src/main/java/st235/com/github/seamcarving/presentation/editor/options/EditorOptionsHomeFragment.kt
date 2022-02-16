package st235.com.github.seamcarving.presentation.editor.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.editor.EditorViewModel
import st235.com.github.seamcarving.presentation.editor.options.adapter.EditorOption
import st235.com.github.seamcarving.presentation.editor.options.adapter.EditorOptionsAdapter
import st235.com.github.seamcarving.presentation.utils.findViewById

class EditorOptionsHomeFragment: Fragment() {

    private val editorViewModel: EditorViewModel by sharedViewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val editorOptionsAdapter = EditorOptionsAdapter { option ->
        when (option.id) {
            R.id.editor_option_brush -> editorViewModel.updateOptionScreenData(EditorHomeOptions.BRUSH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_editor_options_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = findViewById(R.id.recycler_view)
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = editorOptionsAdapter

        editorOptionsAdapter.setItems(
            listOf(
                EditorOption(
                    id = R.id.editor_option_brush,
                    text = R.string.fragment_editor_options_home_menu_brush,
                    icon = R.drawable.ic_twotone_brush
                ),
                EditorOption(
                    id = R.id.editor_option_sizes,
                    text = R.string.fragment_editor_options_home_menu_sizes,
                    icon = R.drawable.ic_twotone_straighten
                ),
                EditorOption(
                    id = R.id.editor_option_modes,
                    text = R.string.fragment_editor_options_home_menu_mode,
                    icon = R.drawable.ic_twotone_aspect_ratio
                )
            )
        )
    }

    companion object {
        const val TAG = "fragment.editor_options_home"
        const val CHILD_TAG = "fragment.editor_option_nested_fragment"
    }
}