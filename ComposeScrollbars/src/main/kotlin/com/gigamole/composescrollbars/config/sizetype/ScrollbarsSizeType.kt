package com.gigamole.composescrollbars.config.sizetype

import androidx.compose.ui.unit.Dp
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.sizetype.ScrollbarsSizeType.Full

/**
 * The scrollbars layers container size configuration for [ScrollbarsConfig]. The scrollbars layers container is centered, when the size is not [Full].
 *
 * @see ScrollbarsOrientation
 * @author GIGAMOLE
 */
sealed interface ScrollbarsSizeType {

    /** The scrollbars layers container size, which occupies all available size (width or height, depends on [ScrollbarsOrientation]). */
    data object Full : ScrollbarsSizeType

    /**
     * The scrollbars layers container size, which occupies available size by [fraction] (width or height, depends on [ScrollbarsOrientation]).
     *
     * @property fraction The layers container size fraction.
     */
    data class Fraction(
        val fraction: Float = ScrollbarsSizeTypeDefaults.Fraction.Fraction
    ) : ScrollbarsSizeType

    /**
     * The scrollbars layers container size, which occupies available size by the exact [size] (width or height, depends on [ScrollbarsOrientation]).
     *
     * @property size The layers container exact size.
     */
    data class Exact(
        val size: Dp = ScrollbarsSizeTypeDefaults.Exact.Size
    ) : ScrollbarsSizeType
}