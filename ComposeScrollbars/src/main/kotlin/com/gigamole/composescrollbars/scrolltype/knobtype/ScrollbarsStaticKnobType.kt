package com.gigamole.composescrollbars.scrolltype.knobtype

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.unit.Dp
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType

/**
 * The scrollbars static knob configuration for [ScrollbarsScrollType].
 *
 * @see ScrollbarsDynamicKnobType
 * @see ScrollbarsKnobType
 * @author GIGAMOLE
 */
sealed interface ScrollbarsStaticKnobType : ScrollbarsKnobType {

    /** The knob [AnimationSpec]. Null disables the animation. */
    override val animationSpec: AnimationSpec<Float>?

    /**
     * The scrollbars static knob with an automatic size.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     */
    data class Auto(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec
    ) : ScrollbarsStaticKnobType

    /**
     * The scrollbars static knob with a fraction size.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     * @property fraction The knob fraction size.
     */
    data class Fraction(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec,
        val fraction: Float = ScrollbarsKnobTypeDefaults.Fraction.Fraction
    ) : ScrollbarsStaticKnobType

    /**
     * The scrollbars static knob with an exact size.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     * @property size The knob exact size.
     */
    data class Exact(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec,
        val size: Dp = ScrollbarsKnobTypeDefaults.Exact.Size
    ) : ScrollbarsStaticKnobType
}