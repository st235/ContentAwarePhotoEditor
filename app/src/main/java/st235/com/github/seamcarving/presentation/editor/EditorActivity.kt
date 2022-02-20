package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
            when (view.id) {
                R.id.editor_option_brush -> openFragment(EditorBrushFragment())
                R.id.editor_option_reset -> editorViewDelegate.reset()
                R.id.editor_option_apply -> render()
                else -> null
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

    private fun render() {
        val uri = extractImageUri()

        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>(540, 540) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    editorViewModel.saveImage(resource, editorViewDelegate.getMatrix())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
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