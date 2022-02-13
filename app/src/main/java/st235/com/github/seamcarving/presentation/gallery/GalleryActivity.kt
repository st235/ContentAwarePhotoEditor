package st235.com.github.seamcarving.presentation.gallery

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.gallery.list.GalleryListFragment

class GalleryActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        openGalleryList()
    }

    private fun openGalleryList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GalleryListFragment())
            .commit()
    }
}