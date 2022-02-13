package st235.com.github.seamcarving.presentation.gallery.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel

class GalleryListFragment: Fragment() {

    private val galleryViewModel: GalleryViewModel by sharedViewModel()

    private lateinit var galleryRecyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val galleryAdapter = GalleryAdapter {
        Log.d("HelloWorld", it.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryRecyclerView = view.findViewById(R.id.gallery_list_recycler_view)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        galleryRecyclerView.adapter = galleryAdapter
        galleryRecyclerView.layoutManager = layoutManager
        galleryRecyclerView.setHasFixedSize(true)

        galleryViewModel.observeImages().observe(viewLifecycleOwner) {
            galleryAdapter.setItems(it)
        }

        galleryViewModel.loadImages()
    }
}