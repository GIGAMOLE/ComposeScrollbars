package com.gigamole.composescrollbars.scrolltype.knobtype

import androidx.compose.animation.core.AnimationSpec
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType

/**
 * The scrollbars generic knob configuration for [ScrollbarsScrollType].
 *
 * @see ScrollbarsStaticKnobType
 * @see ScrollbarsDynamicKnobType
 */
interface ScrollbarsKnobType {

    /** The knob [AnimationSpec]. Null disables the animation. */
    val animationSpec: AnimationSpec<Float>?
}