package com.gigamole.composescrollbars.scrolltype.knobtype

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.ui.unit.Dp
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType

/**
 * The scrollbars dynamic knob configuration for [ScrollbarsScrollType].
 *
 * @see ScrollbarsStaticKnobType
 * @see ScrollbarsKnobType
 * @author GIGAMOLE
 */
sealed interface ScrollbarsDynamicKnobType : ScrollbarsKnobType {

    /** The knob [AnimationSpec]. Null disables the animation. */
    override val animationSpec: AnimationSpec<Float>?

    /**
     * The scrollbars dynamic knob with an automatic size.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     */
    data class Auto(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec
    ) : ScrollbarsDynamicKnobType

    /**
     * The scrollbars dynamic knob with a fraction size.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     * @property fraction The knob fraction size.
     */
    data class Fraction(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec,
        val fraction: Float = ScrollbarsKnobTypeDefaults.Fraction.Fraction
    ) : ScrollbarsDynamicKnobType

    /**
     * The scrollbars dynamic knob with an exact size.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     * @property size The knob exact size.
     */
    data class Exact(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec,
        val size: Dp = ScrollbarsKnobTypeDefaults.Exact.Size
    ) : ScrollbarsDynamicKnobType

    /**
     * The scrollbars dynamic knob with a size, which represents current visible items as a section or with sub-interpolation.
     *
     * @property animationSpec The knob [AnimationSpec]. Null disables the animation.
     * @property isSubLerp Indicates whether the knob size is sub interpolated between items.
     */
    data class Worm(
        override val animationSpec: AnimationSpec<Float>? = ScrollbarsKnobTypeDefaults.AnimationSpec,
        val isSubLerp: Boolean = ScrollbarsKnobTypeDefaults.Worm.IsSubLerp
    ) : ScrollbarsDynamicKnobType
}