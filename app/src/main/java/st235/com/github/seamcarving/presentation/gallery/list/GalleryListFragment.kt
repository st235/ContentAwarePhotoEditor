package st235.com.github.seamcarving.presentation.gallery.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.data.StatefulMediaRequest
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.presentation.editor.EditorActivity
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel
import st235.com.github.seamcarving.presentation.gallery.detailed.GalleryDetailedFragment


class GalleryListFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by sharedViewModel()

    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var createNewProjectFromGalleryButton: ExtendedFloatingActionButton
    private lateinit var createNewProjectFromCameraButton: FloatingActionButton

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val galleryAdapter = GalleryAdapter { imageInfo, imageView ->
        onGalleryItemClicked(imageInfo, imageView)
    }

    private val onOpenGalleryResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val imageUri = result.data?.data
        if (imageUri != null) {
            onFileWriteFinished(imageUri)
        }
    }

    private val onOpenCameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { wasSuccessful ->
        if (wasSuccessful) {
            galleryViewModel.consumePendingFile()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryRecyclerView = view.findViewById(R.id.gallery_list_recycler_view)
        createNewProjectFromGalleryButton = view.findViewById(R.id.gallery_list_gallery_project)
        createNewProjectFromCameraButton = view.findViewById(R.id.gallery_list_camera_project)

        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        galleryRecyclerView.adapter = galleryAdapter
        galleryRecyclerView.layoutManager = layoutManager
        galleryRecyclerView.setHasFixedSize(true)

        galleryRecyclerView.addOnScrollListener(FabCollapseScrollListener())

        createNewProjectFromGalleryButton.setOnClickListener {
            onNewProjectClick()
        }

        createNewProjectFromCameraButton.setOnClickListener {
            onCameraProjectClick()
        }

        galleryViewModel.observeAlbumImages().observe(viewLifecycleOwner) {
            galleryAdapter.setItems(it)
        }

        galleryViewModel.observeMediaRequests().observe(viewLifecycleOwner) { mediaRequest ->
            when (mediaRequest) {
                is StatefulMediaRequest.PendingFetch -> onCameraFilePrepared(mediaRequest.uri)
                is StatefulMediaRequest.FinishedFetch -> onFileWriteFinished(mediaRequest.uri)
            }
        }

        galleryViewModel.loadNextAlbumPage()
    }

    override fun onResume() {
        super.onResume()
        galleryViewModel.obtainCurrentAlbumPage()
    }

    private fun onNewProjectClick() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        onOpenGalleryResultLauncher.launch(intent)
    }

    private fun onCameraProjectClick() {
        galleryViewModel.requestEmptyFile()
    }

    private fun onCameraFilePrepared(uri: Uri) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        onOpenCameraResultLauncher.launch(uri)
    }

    private fun onFileWriteFinished(uri: Uri) {
        startActivity(EditorActivity.launchIntent(requireContext(), uri))
    }

    private fun onGalleryItemClicked(imageInfo: ImageInfo, view: View) {
        galleryViewModel.updateSelectedImage(imageInfo)

        val fragment = GalleryDetailedFragment.create(view.transitionName)
        fragment.sharedElementEnterTransition = GalleryItemTransition()
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        fragment.sharedElementReturnTransition = GalleryItemTransition()
        fragment.postponeEnterTransition()

        parentFragmentManager
            .beginTransaction()
            .addSharedElement(view, view.transitionName)
            .replace(R.id.fragment_container, fragment, GalleryDetailedFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    private inner class FabCollapseScrollListener : RecyclerView.OnScrollListener() {

        private val handler = Handler(Looper.getMainLooper())
        private val expandCallback = Runnable {
            createNewProjectFromGalleryButton.extend()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if ((dy > 0 || dy < 0) && createNewProjectFromGalleryButton.isExtended) {
                handler.removeCallbacks(expandCallback)
                createNewProjectFromGalleryButton.shrink()
            }

            if (!recyclerView.canScrollVertically(1) && dy > 0) {
                galleryViewModel.loadNextAlbumPage()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                handler.postDelayed(expandCallback, 1_000L)
            }
        }
    }
}