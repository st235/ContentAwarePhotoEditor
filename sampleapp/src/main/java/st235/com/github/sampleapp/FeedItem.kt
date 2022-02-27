package st235.com.github.sampleapp

import androidx.annotation.DrawableRes

data class FeedItem(
    val id: Int,
    @DrawableRes val image: Int
)
