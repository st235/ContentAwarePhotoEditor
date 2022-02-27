package st235.com.github.seamcarving.data

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import st235.com.github.seamcarving.R
import st235.com.github.seamcarving.data.models.AspectRatioResponse

class AspectRatiosRepository(
    private val resources: Resources,
    private val gson: Gson
) {

    val aspectRatios: List<AspectRatioResponse> by lazy {
        val rawJson = resources.openRawResource(R.raw.aspect_ratios).bufferedReader().use { it.readText() }
        return@lazy gson.fromJson(rawJson, object : TypeToken<List<AspectRatioResponse>>() { }.type)
    }

}
