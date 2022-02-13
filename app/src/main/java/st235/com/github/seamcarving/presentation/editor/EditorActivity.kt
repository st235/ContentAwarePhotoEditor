package st235.com.github.seamcarving.presentation.editor

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R

class EditorActivity: AppCompatActivity() {

    private val editorViewModel: EditorViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_editor)
    }
}