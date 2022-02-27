package st235.com.github.seamcarving.utils

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri

class BitmapHelper(
    private val contentResolver: ContentResolver
) {

    fun loadDimensions(uri: Uri): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        return contentResolver.openInputStream(uri).use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream, null, options)
            intArrayOf(options.outWidth, options.outHeight)
        }
    }

}
