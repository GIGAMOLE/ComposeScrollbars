package com.gigamole.composescrollbars.common

import androidx.compose.animation.core.Easing
import com.gigamole.composescrollbars.ScrollbarsState

/**
 * Performs a linear interpolation (lerp) between two values ([start] and [stop]) using a sub-fraction of a given [Float].
 *
 * Optionally, an [Easing] function can be provided to modify the interpolation curve.
 *
 * @param start The starting value of the interpolation.
 * @param stop The ending value of the interpolation.
 * @param easing An optional [Easing] function to modify the interpolation curve.
 * @return The interpolated value based on the sub-fraction of the current [Float].
 * @see ScrollbarsState
 * @author GIGAMOLE
 */
fun Float.subLerp(start: Float, stop: Float, easing: Easing? = null): Float {
    return when {
        start == stop -> start
        this < start -> 0.0F
        this > stop -> 1.0F
        else -> {
            val subFraction = (this - start) / (stop - start)

            easing?.transform(subFraction) ?: subFraction
        }
    }
}
