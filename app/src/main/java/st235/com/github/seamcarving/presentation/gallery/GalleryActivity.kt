package st235.com.github.seamcarving.presentation.gallery

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.gallery.list.GalleryListFragment

class GalleryActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModel()

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        toolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(toolbar)

        openGalleryList()
    }

    private fun openGalleryList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GalleryListFragment())
            .commit()
    }
}