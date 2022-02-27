package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.components.ToggleGroupLayout
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrushFragment
import st235.com.github.seamcarving.presentation.editor.options.resize.EditorResizeModesFragment
import st235.com.github.seamcarving.presentation.editor.options.quality.EditorQualityModesFragment
import st235.com.github.seamcarving.presentation.editor.options.dimensions.EditorDimensionsFragment

class EditorActivity : AppCompatActivity() {

    private val editorViewModel: EditorViewModel by viewModel()

    private lateinit var editorViewDelegate: EditorViewDelegate

    private lateinit var toolbar: MaterialToolbar
    private lateinit var editorControlPanelLayout: ToggleGroupLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        toolbar = findViewById(R.id.toolbar)
        editorControlPanelLayout = findViewById(R.id.editor_control_panel_view)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val rootView = findViewById<ViewGroup>(R.id.editor_content_root_view)

        editorViewDelegate = EditorViewDelegate(rootView)

        editorControlPanelLayout.onSelectedListener = { view ->
            onControlItemSelected(view)
        }

        editorViewModel.observeImage()
            .observe(this) { image ->
                editorViewDelegate.updateImage(image)
            }

        editorViewModel.observeSelectedBrush()
            .observe(this) { brushType ->
                editorViewDelegate.updateBrushType(brushType)
            }

        editorViewModel.observeSelectedAspectRatio()
            .observe(this) { aspectRatio ->
                if (aspectRatio == null) {
                    editorControlPanelLayout.hideToggle(R.id.editor_option_dimensions)
                } else {
                    editorControlPanelLayout.showToggle(R.id.editor_option_dimensions)
                }
            }

        editorViewModel.observeImageStatus()
            .observe(this) { status ->
                when (status) {
                    ImageStatus.FINISHED -> finish()
                }
            }

        onControlItemSelected(editorControlPanelLayout.selectedView)
        editorViewModel.updateImage(extractImageUri())
        editorViewModel.loadAspectRatios()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun onControlItemSelected(view: View) {
        when (view.id) {
            R.id.editor_option_brush -> openFragment(EditorBrushFragment())
            R.id.editor_option_dimensions -> openFragment(EditorDimensionsFragment())
            R.id.editor_option_resize_modes -> openFragment(EditorResizeModesFragment())
            R.id.editor_option_quality_modes -> openFragment(EditorQualityModesFragment())
            R.id.editor_option_reset -> editorViewDelegate.reset()
            R.id.editor_option_apply -> render()
        }
    }

    private fun render() {
        editorViewModel.saveImage(editorViewDelegate.getMatrix())
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.editor_options_container, fragment, "option")
            .commit()
    }

    private fun extractImageUri(): Uri {
        return intent?.getParcelableExtra(ARGS_IMAGE_URI)
            ?: throw IllegalStateException("EditorActivity cannot be started without a uri")
    }

    companion object {

        private const val ARGS_IMAGE_URI = "args.image_uri"

        fun launchIntent(context: Context, imageUri: Uri): Intent {
            val intent = Intent(context, EditorActivity::class.java)

            val bundle = Bundle()
            bundle.putParcelable(ARGS_IMAGE_URI, imageUri)

            intent.putExtras(bundle)

            return intent
        }

    }
}