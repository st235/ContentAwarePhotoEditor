package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.components.ToggleGroupLayout
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrushFragment

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

        editorViewDelegate.updateImage(extractImageUri())

        editorControlPanelLayout.onSelectedListener = { view ->
            val fragment = when (view.id) {
                R.id.editor_option_brush -> EditorBrushFragment()
                else -> null
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.editor_options_container, fragment, "option")
                    .addToBackStack("option")
                    .commit()
            }
        }

        editorViewModel.observeBrushType()
            .observe(this) { brushType ->
                editorViewDelegate.updateBrushType(brushType)
            }
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