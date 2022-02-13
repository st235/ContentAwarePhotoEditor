package st235.com.github.seamcarving.utils.media

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.IOException


object MediaHelper {

    fun loadSize(contentResolver: ContentResolver, imageUri: Uri): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(
            contentResolver.openInputStream(imageUri),
            null,
            options
        )

        val exif = loadExifFromUri(contentResolver, imageUri)

        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        return calculateRealSizeAccordingToExif(imageWidth, imageHeight, exif)
    }

    private fun calculateRealSizeAccordingToExif(
        originalWidth: Int,
        originalHeight: Int,
        exifInterface: ExifInterface?
    ): IntArray {
        val exifOrientation = exifInterface?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        ) ?: ExifInterface.ORIENTATION_UNDEFINED

        val realOrientation = convertExifToAngle(exifOrientation)

        return when(realOrientation) {
            90, 270 -> intArrayOf(originalHeight, originalWidth)
            else -> intArrayOf(originalWidth, originalHeight)
        }
    }

    private fun loadExifFromUri(contentResolver: ContentResolver, imageUri: Uri): ExifInterface? {
        return try {
            val inputStream = contentResolver.openInputStream(imageUri) ?: return null
            val exif = ExifInterface(inputStream)
            inputStream.close()
            return exif
        } catch (e: IOException) {
            null
        }
    }

    private fun convertExifToAngle(exifOrientation: Int): Int {
        return when(exifOrientation) {
            ExifInterface.ORIENTATION_TRANSPOSE,
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_270,
            ExifInterface.ORIENTATION_TRANSVERSE -> 270
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            else -> 0
        }
    }

}