package st235.com.github.seamcarving.presentation.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import st235.com.github.seamcarving.R

class GalleryActivity : AppCompatActivity() {

    private val viewModel: GalleryViewModel by viewModel()

    private lateinit var galleryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        galleryRecyclerView = findViewById(R.id.gallery_recycler_view)
    }
}