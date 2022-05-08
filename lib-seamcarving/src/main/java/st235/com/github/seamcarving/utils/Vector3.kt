package st235.com.github.seamcarving.utils

import android.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

internal data class Vector3(
    val x: Int,
    val y: Int,
    val z: Int
) {

    fun add(that: Vector3): Vector3 {
        return Vector3(x + that.x, y + that.y, z + that.z)
    }

    fun subtract(that: Vector3): Vector3 {
        return Vector3(x - that.x, y - that.y, z - that.z)
    }
    
    fun abs(): Vector3 {
        return Vector3(Math.abs(x), Math.abs(y), Math.abs(z))
    }

    fun l1Norm(): Int {
        return Math.abs(x) + Math.abs(y) + Math.abs(z)
    }
    
    fun magnitude(): Double {
        return sqrt(x.toDouble().pow(2) + y.toDouble().pow(2) + z.toDouble().pow(2))
    }

    companion object {
        fun fromColor(color: Int): Vector3 {
            return Vector3(Color.red(color), Color.green(color), Color.blue(color))
        }
    }
}