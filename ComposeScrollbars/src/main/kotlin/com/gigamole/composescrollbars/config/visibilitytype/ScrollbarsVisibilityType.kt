package com.gigamole.composescrollbars.config.visibilitytype

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.lerp
import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsGravity
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType


/**
 * The scrollbars visibility configuration for [ScrollbarsConfig].
 *
 * @see ScrollbarsScrollType
 * @author GIGAMOLE
 */
sealed interface ScrollbarsVisibilityType {

    /** The static scrollbars visibility. Always visible. */
    data object Static : ScrollbarsVisibilityType

    /** The dynamic scrollbars visibility. Includes in/out visibility animation, fading and other. */
    sealed class Dynamic : ScrollbarsVisibilityType {

        /**
         * In animation spec. Null disables the animation.
         *
         * @see HandleFraction
         */
        abstract val inAnimationSpec: AnimationSpec<Float>?

        /**
         * Out animation spec. Null disables the animation.
         *
         * @see HandleFraction
         */
        abstract val outAnimationSpec: AnimationSpec<Float>?

        /**
         * Indicates whether the dynamic visibility is animated with fade.
         *
         * @see fadeFraction
         */
        abstract val isFaded: Boolean

        /**
         * Indicates whether scrollbars are visible when the scroll is not possible (short content).
         *
         * @see ScrollbarsScrollType.isScrollPossible
         */
        abstract val isVisibleWhenScrollNotPossible: Boolean

        /**
         * Indicates whether scrollbars are visible when any press/touch down event occurred.
         *
         * @see cancelAwaitTouchDown
         * @see handleAwaitTouchDownRelease
         */
        abstract val isVisibleOnTouchDown: Boolean

        /**
         * Indicates whether scrollbars are statically visible only when the scroll is possible.
         *
         * @see ScrollbarsScrollType.isScrollPossible
         */
        abstract val isStaticWhenScrollPossible: Boolean

        private var fractionState by mutableFloatStateOf(0.0F)
        private var isAwaitTouchDownState by mutableStateOf(false)
        private var isAwaitTouchDownAnimationFinishedState by mutableStateOf(false)
        private var isHighlightingState by mutableStateOf(false)

        /**
         * Raw visibility fraction. Range from 0.0F to 1.0F.
         *
         * @see HandleFraction
         */
        val fraction: Float
            get() = fractionState

        /**
         * Fade fraction. Range from 0.0F to 1.0F.
         *
         * @see isFaded
         */
        val fadeFraction: Float
            get() = if (isFaded) {
                fraction
            } else {
                1.0F
            }

        /**
         * Cancels the pending auto-hide touch/press down sequence.
         *
         * @see isVisibleOnTouchDown
         */
        internal fun cancelAwaitTouchDown() {
            if (inAnimationSpec == null || outAnimationSpec == null) {
                return
            }

            isAwaitTouchDownState = false
        }

        /**
         * Handles the pending auto-hide touch/press down sequence.
         *
         * @see isVisibleOnTouchDown
         */
        internal fun handleAwaitTouchDownRelease() {
            if (inAnimationSpec == null || outAnimationSpec == null) {
                return
            }

            if (isAwaitTouchDownAnimationFinishedState.not()) {
                isAwaitTouchDownState = true
            }
        }

        /**
         * Handles the visibility [fraction] by calculating all provided fields.
         *
         * @param state The current [ScrollbarsState].
         * @see ScrollbarsScrollType
         */
        @Composable
        internal fun HandleFraction(state: ScrollbarsState) {
            val localInAnimationSpec = inAnimationSpec
            val localOutAnimationSpec = outAnimationSpec
            val isVisible = with(state.scrollType) {
                val isVisibleWhenScrollNotPossibleFlag = if (isVisibleWhenScrollNotPossible) {
                    true
                } else {
                    isScrollPossible
                }
                val isVisibleOnTouchDownFlag = if (isVisibleOnTouchDown) {
                    state.isTouchDown || isAwaitTouchDownState
                } else {
                    false
                }
                val isStaticWhenScrollPossibleFlag = if (isStaticWhenScrollPossible) {
                    isScrollPossible
                } else {
                    isScrollInProgress
                }

                (isStaticWhenScrollPossibleFlag || isVisibleOnTouchDownFlag || isHighlightingState) && isVisibleWhenScrollNotPossibleFlag
            }
            val targetValue = if (isVisible) {
                1.0F
            } else {
                0.0F
            }

            fractionState = if (localInAnimationSpec != null && localOutAnimationSpec != null) {
                animateFloatAsState(
                    targetValue = targetValue,
                    animationSpec = if (isVisible) {
                        localInAnimationSpec
                    } else {
                        localOutAnimationSpec
                    },
                    finishedListener = { value ->
                        isAwaitTouchDownAnimationFinishedState = value == 1.0F

                        if (isAwaitTouchDownState) {
                            isAwaitTouchDownState = false
                        }

                        if (isHighlightingState) {
                            if (value == 1.0F) {
                                isHighlightingState = false
                            }
                        }
                    },
                    label = "VisibilityFractionState"
                ).value.also {
                    isAwaitTouchDownAnimationFinishedState = it == 1.0F
                }
            } else {
                targetValue
            }
        }

        /**
         * Highlights (shows then hides) the scrollbars, so the [fraction] goes from 0.0F to 1.0F and then back to 0.0F.
         *
         * @see HandleFraction
         */
        fun highlight() {
            isHighlightingState = true

            if (fractionState == 1.0F) {
                isHighlightingState = false
            }
        }

        /**
         * The dynamic visibility with only fade.
         *
         * @property inAnimationSpec The in [AnimationSpec]. Null disables the animation.
         * @property outAnimationSpec The out [AnimationSpec]. Null disables the animation.
         * @property isVisibleWhenScrollNotPossible Indicates whether scrollbars are visible when the scroll is not possible (short content).
         * @property isVisibleOnTouchDown Indicates whether scrollbars are visible when any press/touch down event occurred.
         * @property isStaticWhenScrollPossible Indicates whether scrollbars are statically visible only when the scroll is possible.
         */
        data class Fade(
            override val inAnimationSpec: AnimationSpec<Float>? = ScrollbarsVisibilityTypeDefaults.Dynamic.InAnimationSpec,
            override val outAnimationSpec: AnimationSpec<Float>? = ScrollbarsVisibilityTypeDefaults.Dynamic.OutAnimationSpec,
            override val isVisibleWhenScrollNotPossible: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleWhenScrollNotPossible,
            override val isVisibleOnTouchDown: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleOnTouchDown,
            override val isStaticWhenScrollPossible: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsStaticWhenScrollPossible
        ) : Dynamic() {

            override val isFaded: Boolean
                get() = true
        }

        /**
         * The dynamic visibility with the slide, and optional fade.
         *
         * @property inAnimationSpec The in [AnimationSpec]. Null disables the animation.
         * @property outAnimationSpec The out [AnimationSpec]. Null disables the animation.
         * @property isFaded Indicates whether the dynamic visibility is animated with fade.
         * @property isVisibleWhenScrollNotPossible Indicates whether scrollbars are visible when the scroll is not possible (short content).
         * @property isVisibleOnTouchDown Indicates whether scrollbars are visible when any press/touch down event occurred.
         * @property isStaticWhenScrollPossible Indicates whether scrollbars are statically visible only when the scroll is possible.
         * @see ScrollbarsLayersType
         */
        data class Slide(
            override val inAnimationSpec: AnimationSpec<Float>? = ScrollbarsVisibilityTypeDefaults.Dynamic.InAnimationSpec,
            override val outAnimationSpec: AnimationSpec<Float>? = ScrollbarsVisibilityTypeDefaults.Dynamic.OutAnimationSpec,
            override val isFaded: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsFaded,
            override val isVisibleWhenScrollNotPossible: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleWhenScrollNotPossible,
            override val isVisibleOnTouchDown: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleOnTouchDown,
            override val isStaticWhenScrollPossible: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsStaticWhenScrollPossible
        ) : Dynamic() {

            private var targetSlideOffsetState by mutableStateOf(IntOffset.Zero)

            /** The current slide offset. Range (+/- and X/Y) from captured scrollbars layers container size to 0.0F. */
            val slideOffset by derivedStateOf {
                targetSlideOffsetState.times(1.0F - fraction)
            }

            /**
             * Handles target slide offset by capturing the current scrollbars layers container [size] and mapping it to a correct side (left, top, right or bottom,
             * depends on [ScrollbarsOrientation] and [ScrollbarsGravity]).
             *
             * @param state The current [ScrollbarsState].
             * @param size The captured scrollbars layers container [size].
             */
            internal fun handleTargetSlideOffset(
                state: ScrollbarsState,
                size: IntSize
            ) {
                with(state) {
                    targetSlideOffsetState = when (config.orientation) {
                        ScrollbarsOrientation.Vertical -> {
                            when (config.gravity) {
                                ScrollbarsGravity.Start -> {
                                    IntOffset(
                                        x = -size.width,
                                        y = 0
                                    )
                                }
                                ScrollbarsGravity.End -> {
                                    IntOffset(
                                        x = size.width,
                                        y = 0
                                    )
                                }
                            }
                        }
                        ScrollbarsOrientation.Horizontal -> {
                            when (config.gravity) {
                                ScrollbarsGravity.Start -> {
                                    IntOffset(
                                        x = 0,
                                        y = -size.height
                                    )
                                }
                                ScrollbarsGravity.End -> {
                                    IntOffset(
                                        x = 0,
                                        y = size.height
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * The dynamic visibility with the scale, and optional fade.
         *
         * @property inAnimationSpec The in [AnimationSpec]. Null disables the animation.
         * @property outAnimationSpec The out [AnimationSpec]. Null disables the animation.
         * @property isFaded Indicates whether the dynamic visibility is animated with fade.
         * @property isVisibleWhenScrollNotPossible Indicates whether scrollbars are visible when the scroll is not possible (short content).
         * @property isVisibleOnTouchDown Indicates whether scrollbars are visible when any press/touch down event occurred.
         * @property isStaticWhenScrollPossible Indicates whether scrollbars are statically visible only when the scroll is possible.
         * @property startScale The start (from) scale. The stop scale is at 1.0F.
         */
        data class Scale(
            override val inAnimationSpec: AnimationSpec<Float>? = ScrollbarsVisibilityTypeDefaults.Dynamic.InAnimationSpec,
            override val outAnimationSpec: AnimationSpec<Float>? = ScrollbarsVisibilityTypeDefaults.Dynamic.OutAnimationSpec,
            override val isFaded: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsFaded,
            override val isVisibleWhenScrollNotPossible: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleWhenScrollNotPossible,
            override val isVisibleOnTouchDown: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleOnTouchDown,
            override val isStaticWhenScrollPossible: Boolean = ScrollbarsVisibilityTypeDefaults.Dynamic.IsStaticWhenScrollPossible,
            val startScale: Float = ScrollbarsVisibilityTypeDefaults.Dynamic.Scale.StartScale
        ) : Dynamic() {

            /** The current scale. Range from [startScale] to 1.0F. */
            val scaleValue by derivedStateOf {
                lerp(
                    start = startScale,
                    stop = 1.0F,
                    fraction = fraction
                )
            }
        }
    }
}
