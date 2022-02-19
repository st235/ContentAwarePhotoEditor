package st235.com.github.seamcarving.presentation.editor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import st235.com.github.seamcarving.Energy
import st235.com.github.seamcarving.SeamCarver
import st235.com.github.seamcarving.images.BitmapCarvableImage
import st235.com.github.seamcarving.interactors.GalleryInteractor
import st235.com.github.seamcarving.presentation.editor.options.brushes.EditorBrush

class EditorViewModel(
    private val galleryInteractor: GalleryInteractor
): ViewModel() {

    private val brushTypeLiveData = MutableLiveData<EditorBrush>()

    fun updateEditorBrush(type: EditorBrush) {
        brushTypeLiveData.value = type
    }

    fun observeBrushType(): LiveData<EditorBrush> = brushTypeLiveData

    fun saveImage(bitmap: Bitmap, matrix: Bitmap?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val seamCarver = SeamCarver.create()
                val size = min(bitmap.width, bitmap.height)

                val resizedMatrix = matrix?.let { Bitmap.createScaledBitmap(matrix, bitmap.width, bitmap.height, false) }

                val finalMatrix: Array<IntArray>?

                var removedCount = 0
                val verticalRemove = BooleanArray(bitmap.width)

                if (resizedMatrix != null) {
                    finalMatrix = Array(bitmap.height) { IntArray(bitmap.width) }

                    for (i in 0 until bitmap.height) {
                        for (j in 0 until bitmap.width) {
                            if (resizedMatrix.getPixel(j, i) == EditorBrush.KEEP.color) {
                                finalMatrix[i][j] = Energy.MASK_INF
                            } else if (resizedMatrix.getPixel(j, i) == EditorBrush.REMOVE.color) {
                                finalMatrix[i][j] = Energy.MASK_MINUS_INF
                                verticalRemove[j] = true
                            } else {
                                finalMatrix[i][j] = Energy.MASK_NO_VALUE
                            }
                        }
                    }


                    for (element in verticalRemove) {
                        if (element) {
                            removedCount++
                        }
                    }
                } else {
                    finalMatrix = null
                }


                val image = seamCarver.retarget(bitmap, finalMatrix, bitmap.width - removedCount, bitmap.height)
                galleryInteractor.saveImage("cropper", "${System.currentTimeMillis()}_image.png", null, image)
                Log.d("HelloWorld", "Saved")
            }
        }
    }

}