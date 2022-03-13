package st235.com.github.seamcarving.presentation.gallery.detailed

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.data.StatefulMediaRequest
import st235.com.github.seamcarving.interactors.models.ImageInfo
import st235.com.github.seamcarving.presentation.components.GlideBlurTransformation
import st235.com.github.seamcarving.presentation.gallery.GalleryViewModel
import st235.com.github.seamcarving.presentation.utils.findViewById
import st235.com.github.seamcarving.utils.dp

class GalleryDetailedFragment: Fragment() {

    private val galleryViewModel: GalleryViewModel by sharedViewModel()

    private lateinit var imageView: ImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var shareButton: Button
    private lateinit var removeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_gallery_detailed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = findViewById(R.id.image_view)
        backgroundImageView = findViewById(R.id.background_image_view)
        shareButton = findViewById(R.id.share_button)
        removeButton = findViewById(R.id.delete_button)

        imageView.transitionName = extractSharedTransactionName()

        galleryViewModel.observeSelectedImage()
            .observe(viewLifecycleOwner) { imageInfo ->
                updateSelectedImageInfo(imageInfo)
            }

        galleryViewModel.observeMediaRequests()
            .observe(viewLifecycleOwner) { mediaRequest ->
                if (mediaRequest is StatefulMediaRequest.MediaRemoved) {
                    parentFragmentManager.popBackStack()
                }
            }

        removeButton.setOnClickListener {
            galleryViewModel.removeMedia()
        }

    }

    private fun updateSelectedImageInfo(imageInfo: ImageInfo) {
        Glide.with(requireContext())
            .load(imageInfo.uri)
            .centerCrop()
            .apply(RequestOptions().transform(GlideBlurTransformation(64.dp)))
            .into(backgroundImageView)

        Glide.with(requireContext())
            .load(imageInfo.uri)
            .centerInside()
            .listener(EnterTransitionRequestListener())
            .into(imageView)
    }

    private fun extractSharedTransactionName(): String {
        return arguments?.getString(ARG_SHARED_TRANSITION) ?:
            throw IllegalStateException("Fragment does not have a shared transition name")
    }

    private inner class EnterTransitionRequestListener: RequestListener<Drawable> {

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }
    }

    companion object {
        const val TAG = "GalleryDetailedFragment"
        const val ARG_SHARED_TRANSITION = "args.shared_transition"

        fun create(sharedTransitionName: String): Fragment {
            val fragment = GalleryDetailedFragment()

            val bundle = Bundle()
            bundle.putString(ARG_SHARED_TRANSITION, sharedTransitionName)

            fragment.arguments = bundle
            return fragment
        }
    }

}