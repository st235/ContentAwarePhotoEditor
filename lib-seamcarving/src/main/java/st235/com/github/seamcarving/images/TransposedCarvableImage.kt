package st235.com.github.seamcarving.images

internal class TransposedCarvableImage(
    internal val originalImage: CarvableImage
): CarvableImage() {

    override val width: Int = originalImage.height

    override val height: Int = originalImage.width

    override val isMasked: Boolean = originalImage.isMasked

    override fun getMaskPixel(i: Int, j: Int): Int? {
        return originalImage.getMaskPixel(j, i)
    }

    override fun getPixelAt(i: Int, j: Int): Int {
        return originalImage.getPixelAt(j, i)
    }
}