package st235.com.github.seamcarving.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AspectRatioResponse(
    @SerializedName("width")
    @Expose
    val width: Int,
    @SerializedName("height")
    @Expose
    val height: Int
)
