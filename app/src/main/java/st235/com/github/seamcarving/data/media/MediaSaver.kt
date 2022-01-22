package st235.com.github.seamcarving.data.media

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

@WorkerThread
interface MediaSaver {

    fun save(
        title: String,
        description: String,
        origin: Bitmap
    ): Uri?

    fun delete(uri: Uri)

    companion object {
        fun create(contentResolver: ContentResolver, albumTitle: String): MediaSaver {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NewApiMediaSaver(contentResolver, albumTitle)
            } else {
                OldApiMediaSaver(albumTitle)
            }
        }
    }
}

class OldApiMediaSaver(
    private val albumTitle: String
): MediaSaver {

    private val albumDir: File
    get() {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            albumTitle
        )
    }

    @WorkerThread
    override fun save(title: String, description: String, origin: Bitmap): Uri? {
        val directory = albumDir

        if (!directory.exists()) {
            directory.mkdir()
        }

        val image = File(albumDir, "$title.png")
        val outputStream = FileOutputStream(image)
        origin.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return Uri.fromFile(image)
    }

    override fun delete(uri: Uri) {
        val path = uri.path

        if (path == null) {
            return
        }

        val file = File(path)
        file.delete()
    }
}

@TargetApi(Build.VERSION_CODES.Q)
class NewApiMediaSaver(
    private val contentResolver: ContentResolver,
    private val albumTitle: String
) : MediaSaver {
    @WorkerThread
    override fun save(
        title: String,
        description: String,
        origin: Bitmap
    ): Uri? {
        val path = Environment.DIRECTORY_PICTURES + File.separator + albumTitle

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, path)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri == null) {
            return null
        }

        return try {
            val outputStream = contentResolver.openOutputStream(uri)

            outputStream.use { stream ->
                origin.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }

            uri
        } catch (fileNotFoundException: FileNotFoundException) {
            contentResolver.delete(uri, null, null)
            null
        }
    }

    override fun delete(uri: Uri) {
        contentResolver.delete(uri, null, null)
    }
}
