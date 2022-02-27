package st235.com.github.seamcarving.presentation.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.presentation.components.EditorView
import st235.com.github.seamcarving.presentation.components.GlideBlurTransformation
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush
import st235.com.github.seamcarving.presentation.editor.options.brushes.colorRes
import st235.com.github.seamcarving.utils.dp

class EditorViewDelegate(
    private val rootView: ViewGroup
) {

    private val backgroundView: ImageView = rootView.findViewById(R.id.background_image_view)
    private val editorView: EditorView = rootView.findViewById(R.id.editor_view)

    private val context: Context
    get() {
        return rootView.context
    }

    fun updateBrushType(type: EditorBrush) {
        editorView.setEditorBrushRes(type.colorRes)
    }

    fun updateImage(uri: Uri) {
        backgroundView.visibility = View.VISIBLE
        editorView.visibility = View.VISIBLE

        val requestOptions = RequestOptions().transform(GlideBlurTransformation(64.dp))

        Glide.with(context)
            .load(uri)
            .apply(requestOptions)
            .into(backgroundView)

        Glide.with(context)
            .load(uri)
            .into(object : CustomViewTarget<EditorView, Drawable>(editorView) {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    editorView.foregroundImage = resource
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                }
            })
    }

    fun getMatrix(): Bitmap {
        return editorView.getAreaSnapshot()
    }

    fun reset() {
        editorView.clear()
    }

}