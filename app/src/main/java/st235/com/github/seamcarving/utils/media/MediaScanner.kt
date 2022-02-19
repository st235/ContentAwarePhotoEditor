package st235.com.github.seamcarving.utils.media

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.core.database.getStringOrNull
import java.util.Locale

data class MediaInfo(
    val id: String,
    val name: String,
    val description: String?,
    val height: Int,
    val width: Int,
    val uri: Uri
)

interface MediaScanner {

    data class Page(
        val limit: Int,
        val offset: Int
    )

    fun loadFiles(
        albumTitle: String
    ): List<MediaInfo>

    companion object {
        fun create(contentResolver: ContentResolver): MediaScanner {
            return NewApiMediaScanner(contentResolver)
        }
    }
}

@WorkerThread
class NewApiMediaScanner(
    private val contentResolver: ContentResolver
) : MediaScanner {

    override fun loadFiles(
        albumTitle: String
    ): List<MediaInfo> {
        val albumRegex = albumTitle.toRegex()

        val what = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DESCRIPTION,
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

        val mediaInfos = mutableListOf<MediaInfo>()

        while (cursor.moveToNext()) {
            val bucketName =
                cursor.getStringOrNull(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))

            if (bucketName?.contains(albumRegex) != true) {
                continue
            }

            val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)

            val id = cursor.getInt(idColumnIndex);
            val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION))

            val imageUri = ContentUris
                .withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getInt(idColumnIndex).toLong()
                )

            val realSize = MediaHelper.loadSize(
                contentResolver,
                imageUri
            )

            mediaInfos.add(
                MediaInfo(
                    id = id.toString(),
                    name = name,
                    description = description,
                    width = realSize[0],
                    height = realSize[1],
                    uri = imageUri
                )
            )
        }

        cursor.close()

        return mediaInfos
    }
}
