package com.gigamole.composescrollbars.config.sizetype

import androidx.compose.ui.unit.dp

import com.gigamole.composescrollbars.config.ScrollbarsConfig

/**
 * The default values for [ScrollbarsSizeType].
 *
 * @see ScrollbarsConfig
 * @author GIGAMOLE
 */
object ScrollbarsSizeTypeDefaults {

    /** The default values for [ScrollbarsSizeType.Fraction]. */
    object Fraction {

        /** The default [ScrollbarsSizeType.Fraction.fraction]. */
        const val Fraction = 0.5F
    }

    /** The default values for [ScrollbarsSizeType.Exact]. */
    object Exact {

        /** The default [ScrollbarsSizeType.Exact.size]. */
        val Size = 300.dp
    }
}
