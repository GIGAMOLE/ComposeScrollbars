package com.gigamole.composescrollbars.scrolltype.knobtype

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsKnobTypeDefaults.AnimationSpec

/**
 * The default values for [ScrollbarsKnobType].
 *
 * @see ScrollbarsStaticKnobType
 * @see ScrollbarsDynamicKnobType
 * @see ScrollbarsScrollType
 * @author GIGAMOLE
 */
object ScrollbarsKnobTypeDefaults {

    /** The default knob [AnimationSpec]. */
    val AnimationSpec: AnimationSpec<Float>
        get() = spring()

    /** The default values for static and dynamic fraction [ScrollbarsKnobType]. */
    object Fraction {

        /** The default fraction knob size. */
        const val Fraction: Float = 0.5F
    }

    /** The default values for static and dynamic exact [ScrollbarsKnobType]. */
    object Exact {

        /** The default exact knob size. */
        val Size: Dp = 120.dp
    }

    /** The default values for [ScrollbarsDynamicKnobType.Worm]. */
    object Worm {

        /** The default is knob size sub interpolated between items indicator. */
        const val IsSubLerp: Boolean = true
    }
}