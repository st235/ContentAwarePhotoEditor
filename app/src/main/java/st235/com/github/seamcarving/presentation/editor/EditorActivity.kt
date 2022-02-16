package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.editor.options.EditorHomeOptions
import st235.com.github.seamcarving.presentation.editor.options.EditorOptionsHomeFragment
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrushFragment

class EditorActivity: AppCompatActivity() {

    private val editorViewModel: EditorViewModel by viewModel()

    private lateinit var toolbar: MaterialToolbar

    private lateinit var editorViewDelegate: EditorViewDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editorViewDelegate = EditorViewDelegate(findViewById(R.id.editor_content_root_view))
        editorViewDelegate.updateImage(extractImageUri())

        supportFragmentManager.beginTransaction()
            .replace(R.id.editor_options_container, EditorOptionsHomeFragment(), EditorOptionsHomeFragment.TAG)
            .commit()

        editorViewModel.observeOptionScreenData()
            .observe(this) { type ->
                val fragment = when (type) {
                    EditorHomeOptions.BRUSH -> EditorBrushFragment()
                    else -> null
                }

                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.editor_options_container, fragment, EditorOptionsHomeFragment.CHILD_TAG)
                        .addToBackStack(EditorOptionsHomeFragment.CHILD_TAG)
                        .commit()
                }
            }

        editorViewModel.observeBrushType()
            .observe(this) { brushType ->
                editorViewDelegate.updateBrushType(brushType)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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
        return intent?.getParcelableExtra(ARGS_IMAGE_URI) ?:
            throw IllegalStateException("EditorActivity cannot be started without a uri")
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