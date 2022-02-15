/*
 * MIT License
 *
 * Copyright (c) 2021 Aleksandr Dadukin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package st235.com.github.seamcarving.presentation.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.IntRange
import androidx.annotation.Px

/**
 * Simple object that
 * provides static
 * blur methods.
 */
object Blur {

    /**
     * Makes a blur image
     * from the given [Bitmap].
     *
     * The algorithm is not sensitive to
     * a bitmap's config.
     *
     * The desired complexity is:
     * O(n*m) for runtime, where n and m are dimensions of the image
     * O(k*z) for memory, where k and z are dimensions of the final image
     *
     * The proposed algorithm adds no extra complexity
     * to the quadratic image pixels traverse and
     * sequentially applies vertical and horizontal
     * motion blur algorithms to the image in order to get
     * Box Blur.
     *
     * Can potentially rise [OutOfMemoryError]
     *
     * @param radius the size of the blur's kernel in pixels. Should be at least 1.
     * @param downscale a special factor that shows how smaller than the original image the blurred image would be
     * @param bitmap the image to be blurred
     *
     * @return blurred [Bitmap]
     *
     * @see <a href="https://en.wikipedia.org/wiki/Box_blur">Box Blur</a>
     * @see Bitmap
     */
    fun apply(
        @IntRange(from = 1L) @Px radius: Int,
        @IntRange(from = 1L) downscale: Int,
        bitmap: Bitmap
    ): Bitmap {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / downscale, bitmap.height / downscale, false)

        val width = scaledBitmap.width
        val height = scaledBitmap.height

        val pixels = IntArray(width * height)
        scaledBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val factor = 1.0 / (2.0 * radius + 1)
        scaledBitmap.recycle()

        var pixelsCopy = pixels.clone()

        for (column in 0 until width) {
            var a = 0.0
            var r = 0.0
            var g = 0.0
            var b = 0.0

            for (i in 0..2*radius) {
                a += pixelsCopy.alphaAt(column, i, width, height)
                r += pixelsCopy.redAt(column, i, width, height)
                g += pixelsCopy.greenAt(column, i, width, height)
                b += pixelsCopy.blueAt(column, i, width, height)
            }

            for (row in 0 until height) {
                if (row < (height - radius)) {
                    a -= pixelsCopy.alphaAt(column, row - radius, width, height)
                    r -= pixelsCopy.redAt(column, row - radius, width, height)
                    g -= pixelsCopy.greenAt(column, row - radius, width, height)
                    b -= pixelsCopy.blueAt(column, row - radius, width, height)
                }

                if (row >= radius) {
                    a += pixelsCopy.alphaAt(column, row + radius, width, height)
                    r += pixelsCopy.redAt(column, row + radius, width, height)
                    g += pixelsCopy.greenAt(column, row + radius, width, height)
                    b += pixelsCopy.blueAt(column, row + radius, width, height)
                }

                pixels[(row * width) + column] = Color.argb(
                    (a * factor).toInt(),
                    (r * factor).toInt(),
                    (g * factor).toInt(),
                    (b * factor).toInt()
                )
            }
        }

        pixelsCopy = pixels.clone()

        for (row in 0 until height) {
            var a = 0.0
            var r = 0.0
            var g = 0.0
            var b = 0.0

            for (i in 0..2*radius) {
                a += pixelsCopy.alphaAt(i, row, width, height)
                r += pixelsCopy.redAt(i, row, width, height)
                g += pixelsCopy.greenAt(i, row, width, height)
                b += pixelsCopy.blueAt(i, row, width, height)
            }

            for (column in 0 until width) {
                if (column < (width - radius)) {
                    a -= pixelsCopy.alphaAt(column - radius, row, width, height)
                    r -= pixelsCopy.redAt(column - radius, row, width, height)
                    g -= pixelsCopy.greenAt(column - radius, row, width, height)
                    b -= pixelsCopy.blueAt(column - radius, row, width, height)
                }

                if (column >= radius) {
                    a += pixelsCopy.alphaAt(column + radius, row, width, height)
                    r += pixelsCopy.redAt(column + radius, row, width, height)
                    g += pixelsCopy.greenAt(column + radius, row, width, height)
                    b += pixelsCopy.blueAt(column + radius, row, width, height)
                }

                pixels[(row * width) + column] = Color.argb(
                    (a * factor).toInt(),
                    (r * factor).toInt(),
                    (g * factor).toInt(),
                    (b * factor).toInt()
                )
            }
        }

        val copy = Bitmap.createBitmap(width, height, bitmap.config)
        copy.setPixels(pixels, 0, width, 0, 0, width, height)
        return copy
    }

    private fun IntArray.alphaAt(x: Int, y: Int, w: Int, h: Int): Int {
        if (!canResolve(x, y, w, h)) {
            return 0
        }

        return Color.alpha(getPixel(x, y, w, h))
    }

    private fun IntArray.redAt(x: Int, y: Int, w: Int, h: Int): Int {
        if (!canResolve(x, y, w, h)) {
            return 0
        }

        return Color.red(getPixel(x, y, w, h))
    }

    private fun IntArray.greenAt(x: Int, y: Int, w: Int, h: Int): Int {
        if (!canResolve(x, y, w, h)) {
            return 0
        }

        return Color.green(getPixel(x, y, w, h))
    }

    private fun IntArray.blueAt(x: Int, y: Int, w: Int, h: Int): Int {
        if (!canResolve(x, y, w, h)) {
            return 0
        }

        return Color.blue(getPixel(x, y, w, h))
    }

    private fun canResolve(x: Int, y: Int, w: Int, h: Int): Boolean {
        return x >= 0 && x < w && y >= 0 && y < h
    }

    private fun IntArray.getPixel(x: Int, y: Int, w: Int, h: Int): Int {
        return this[(y * w) + x]
    }

}