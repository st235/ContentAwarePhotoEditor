package st235.com.github.seamcarving.presentation.gallery.list

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.editor.EditorActivity
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel

class GalleryListFragment: Fragment() {

    private val galleryViewModel: GalleryViewModel by sharedViewModel()

    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var createNewProjectFromGalleryButton: ExtendedFloatingActionButton

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val galleryAdapter = GalleryAdapter {
        Log.d("HelloWorld", it.toString())
    }

    private val onOpenGalleryResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback { result ->
            val imageUri = result.data?.data
            if (imageUri != null) {
                startActivity(EditorActivity.launchIntent(requireContext(), imageUri))
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryRecyclerView = view.findViewById(R.id.gallery_list_recycler_view)
        createNewProjectFromGalleryButton = view.findViewById(R.id.gallery_list_gallery_project)

        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        galleryRecyclerView.adapter = galleryAdapter
        galleryRecyclerView.layoutManager = layoutManager
        galleryRecyclerView.setHasFixedSize(true)

        galleryRecyclerView.addOnScrollListener(FabCollapseScrollListener())

        createNewProjectFromGalleryButton.setOnClickListener {
            onNewProjectClick()
        }

        galleryViewModel.observeImages().observe(viewLifecycleOwner) {
            galleryAdapter.setItems(it)
        }

        galleryViewModel.loadImages()
    }

    private fun onNewProjectClick() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        onOpenGalleryResultLauncher.launch(intent)
    }

    private inner class FabCollapseScrollListener: RecyclerView.OnScrollListener() {

        private val handler = Handler(Looper.getMainLooper())
        private val expandCallback = Runnable {
            createNewProjectFromGalleryButton.extend()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if ((dy > 0 || dy < 0) && createNewProjectFromGalleryButton.isExtended) {
                handler.removeCallbacks(expandCallback)
                createNewProjectFromGalleryButton.shrink()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                handler.postDelayed(expandCallback, 1_000L)
            }
        }
    }
}