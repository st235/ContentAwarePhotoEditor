package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R


class EditorActivity: AppCompatActivity() {

    private val editorViewModel: EditorViewModel by viewModel()

    private lateinit var toolbar: MaterialToolbar

    private lateinit var editorViewDelegate: EditorViewDelegate

    private lateinit var galleryPickerButton: MaterialButton
    private lateinit var cameraPickerButton: MaterialButton

    private val onOpenGalleryResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            val imageUri = result.data?.data
            if (imageUri != null) {
                editorViewDelegate.updateImage(imageUri)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editorViewDelegate = EditorViewDelegate(findViewById(R.id.editor_content_root_view))
        cameraPickerButton = findViewById(R.id.camera_picker_image_button)
        galleryPickerButton = findViewById(R.id.gallery_picker_image_button)

        cameraPickerButton.setOnClickListener {
            onOpenCameraClick()
        }

        galleryPickerButton.setOnClickListener {
            onOpenGalleryClick()
        }
    }

    private fun onOpenCameraClick() {
        val intent = Intent(
            MediaStore.ACTION_IMAGE_CAPTURE
        )

        onOpenGalleryResultLauncher.launch(intent)
    }

    private fun onOpenGalleryClick() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        onOpenGalleryResultLauncher.launch(intent)
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

    companion object {

        fun launchIntent(context: Context): Intent {
            return Intent(context, EditorActivity::class.java)
        }

    }
}