package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.components.EditorView

class EditorActivity: AppCompatActivity() {

    private val editorViewModel: EditorViewModel by viewModel()

    private lateinit var toolbar: MaterialToolbar
    private lateinit var editorView: EditorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        editorView = findViewById(R.id.editor_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.vertical_cat)
        editorView.foregroundImage = BitmapDrawable(resources, bitmap)
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