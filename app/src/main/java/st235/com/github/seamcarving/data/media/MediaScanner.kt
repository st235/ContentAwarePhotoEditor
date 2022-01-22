package st235.com.github.seamcarving.data.media

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.core.database.getStringOrNull
import java.io.File
import java.io.FilenameFilter
import java.util.Locale

interface MediaScanner {

    fun loadFiles(): List<Uri>

    companion object {
        fun create(contentResolver: ContentResolver, albumTitle: String): MediaScanner {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                OldApiMediaScanner(albumTitle)
            } else {
                NewApiMediaScanner(contentResolver, albumTitle)
            }
        }
    }
}

@WorkerThread
class OldApiMediaScanner(
    private val albumTitle: String
) : MediaScanner {

    private val albumDir: File
        get() {
            return File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumTitle
            )
        }

    override fun loadFiles(): List<Uri> {
        val album = albumDir
        val regexp = "/${albumTitle}/.*\\.(png|jpg)".toRegex()

        val filter = FilenameFilter { dir, name ->
            val fullPath = dir.absolutePath + File.separator + name
            fullPath.contains(regexp)
        }

        return album.listFiles(filter)?.sortedByDescending { it.lastModified() }?.mapNotNull { Uri.fromFile(it) } ?: emptyList()
    }
}

@TargetApi(Build.VERSION_CODES.Q)
@WorkerThread
class NewApiMediaScanner(
    private val contentResolver: ContentResolver,
    private val albumTitle: String
) : MediaScanner {

    override fun loadFiles(): List<Uri> {
        val what = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.DATE_MODIFIED,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
        )

        val where = MediaStore.Images.Media.MIME_TYPE + "='image/jpeg'" +
                " OR " + MediaStore.Images.Media.MIME_TYPE + "='image/png'"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            what,
            where,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC"
        ) ?: return emptyList()

        val files = mutableListOf<Uri>()

        while (cursor.moveToNext()) {
            val bucketName =
                cursor.getStringOrNull(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))

            if (bucketName?.lowercase(Locale.US) != albumTitle.lowercase(Locale.US)) {
                continue
            }

            val columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)

            if (columnIndex < 0) {
                continue
            }

            val imageUri = ContentUris
                .withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getInt(columnIndex).toLong()
                )

            files.add(imageUri)
        }

        cursor.close()

        return files
    }
}
