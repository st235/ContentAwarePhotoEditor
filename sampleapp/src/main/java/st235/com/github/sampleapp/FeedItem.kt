package st235.com.github.sampleapp

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

data class Author(
    @DrawableRes val avatar: Int,
    val name: String
)

data class Likes(
    val count: String,
    val isLiked: Boolean
)

data class FeedItem(
    val id: Int,
    val description: String,
    val author: Author,
    val likes: Likes,
    val tags: List<String>,
    @DrawableRes val image: Int
)
