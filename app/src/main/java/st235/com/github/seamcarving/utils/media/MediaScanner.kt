package st235.com.github.seamcarving.utils.media

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.core.database.getStringOrNull
import st235.com.github.seamcarving.BuildConfig

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
        albumTitle: String,
        page: Page
    ): List<MediaInfo>

    companion object {
        fun create(contentResolver: ContentResolver): MediaScanner {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AndroidQMediaScanner(contentResolver)
            } else {
                AndroidLMediaScanner(contentResolver)
            }
        }
    }
}

@WorkerThread
class AndroidLMediaScanner(
    private val contentResolver: ContentResolver
) : MediaScanner {

    override fun loadFiles(
        albumTitle: String,
        page: MediaScanner.Page
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
            MediaStore.Images.ImageColumns.DATE_MODIFIED +
                    " DESC LIMIT ${page.limit} OFFSET ${page.offset}"
        ) ?: return emptyList()

        val mediaInfos = mutableListOf<MediaInfo>()

        while (cursor.moveToNext()) {
            val bucketName =
                cursor.getStringOrNull(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))

            if (bucketName?.contains(albumRegex) != true) {
                continue
            }

            val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)

            val id = cursor.getInt(idColumnIndex)
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

@TargetApi(Build.VERSION_CODES.Q)
class AndroidQMediaScanner(
    private val contentResolver: ContentResolver
): MediaScanner {

    override fun loadFiles(albumTitle: String, page: MediaScanner.Page): List<MediaInfo> {
        val albumRegex = albumTitle.toRegex()

        val what = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DESCRIPTION,
            MediaStore.Images.ImageColumns.DATE_MODIFIED,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
        )

        val whereCondition = "${MediaStore.Images.Media.MIME_TYPE}=? OR ${MediaStore.Images.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf(
            "image/png",
            "image/jpeg"
        )

        val selectionBundle = Bundle()

        selectionBundle.putInt(ContentResolver.QUERY_ARG_LIMIT, page.limit)
        selectionBundle.putInt(ContentResolver.QUERY_ARG_OFFSET, page.offset)
        selectionBundle.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Files.FileColumns.DATE_ADDED))
        selectionBundle.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
        selectionBundle.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, whereCondition)
        selectionBundle.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            what,
            selectionBundle,
            null
        ) ?: return emptyList()

        val mediaInfos = mutableListOf<MediaInfo>()

        while (cursor.moveToNext()) {
            val bucketName =
                cursor.getStringOrNull(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))

            if (bucketName?.contains(albumRegex) != true) {
                continue
            }

            val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)

            val id = cursor.getInt(idColumnIndex)
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
