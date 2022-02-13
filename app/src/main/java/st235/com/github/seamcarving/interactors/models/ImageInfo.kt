package st235.com.github.seamcarving.interactors.models

import android.net.Uri

data class ImageSize(
    val width: Int,
    val height: Int
)

data class ImageInfo(
    val id: String,
    val name: String,
    val description: String?,
    val size: ImageSize,
    val uri: Uri
)
