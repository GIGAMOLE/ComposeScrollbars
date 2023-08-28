@file:Suppress("unused")

package com.gigamole.composescrollbars.config.layercontenttype

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layercontenttype.layercontentstyletype.ScrollbarsLayerContentStyleType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType

/**
 * The scrollbars layer content configuration (background or knob) for [ScrollbarsConfig].
 *
 * @see ScrollbarsLayersType
 * @author GIGAMOLE
 */
sealed interface ScrollbarsLayerContentType {

    /** The empty (not visible) layer content. */
    data object None : ScrollbarsLayerContentType

    /**
     * The custom layer content.
     *
     * When the thickness type is [ScrollbarsThicknessType.Wrap], then the custom content thickness size (width or height, depends on [ScrollbarsOrientation]) should be
     * handled manually.
     *
     * @property content The block with the provided current [ScrollbarsState] that provides a custom [Composable] within a [BoxScope].
     */
    data class Custom(
        val content: @Composable BoxScope.(state: ScrollbarsState) -> Unit
    ) : ScrollbarsLayerContentType

    /** The default layer content. */
    sealed interface Default : ScrollbarsLayerContentType {

        /** The layer content [Shape]. */
        val shape: Shape

        /** The layer content [Brush]. */
        val brush: Brush

        /** The [ScrollbarsLayerContentStyleType]. */
        val styleType: ScrollbarsLayerContentStyleType

        /**
         * The original raw layer content type.
         *
         * @property shape The layer content [Shape].
         * @property brush The layer content [Brush].
         * @property styleType The [ScrollbarsLayerContentStyleType].
         */
        data class Original(
            override val shape: Shape = ScrollbarsLayerContentTypeDefaults.Default.Shape,
            override val brush: Brush = ScrollbarsLayerContentTypeDefaults.Default.Brush,
            override val styleType: ScrollbarsLayerContentStyleType = ScrollbarsLayerContentTypeDefaults.Default.StyleType
        ) : Default

        /**
         * The colored layer content type.
         *
         * @property shape The layer content [Shape].
         * @property brush The layer content [Brush].
         * @property styleType The [ScrollbarsLayerContentStyleType].
         */
        sealed interface Colored : Default {

            /** The idle layer content [Color]. */
            val idleColor: Color

            /**
             * The idle colored layer content type.
             *
             * @property idleColor The idle layer content [Color].
             * @property shape The layer content [Shape].
             * @property styleType The [ScrollbarsLayerContentStyleType].
             */
            data class Idle(
                override val idleColor: Color = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleColor,
                override val shape: Shape = ScrollbarsLayerContentTypeDefaults.Default.Shape,
                override val styleType: ScrollbarsLayerContentStyleType = ScrollbarsLayerContentTypeDefaults.Default.StyleType
            ) : Colored {

                /** The idle [SolidColor] brush with [idleColor]. */
                override val brush: Brush
                    get() = SolidColor(value = idleColor)
            }

            /**
             * The idle colored layer content type.
             *
             * @property idleColor The idle layer content [Color].
             * @property shape The layer content [Shape].
             * @property styleType The [ScrollbarsLayerContentStyleType].
             * @property activeColor The active layer content [Color].
             * @property inAnimationSpec The [activeColor] layer content in [AnimationSpec]. Null disables the animation.
             * @property outAnimationSpec The [activeColor] layer content out [AnimationSpec]. Null disables the animation.
             */
            data class IdleActive(
                override val idleColor: Color = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleColor,
                override val shape: Shape = ScrollbarsLayerContentTypeDefaults.Default.Shape,
                override val styleType: ScrollbarsLayerContentStyleType = ScrollbarsLayerContentTypeDefaults.Default.StyleType,
                val activeColor: Color = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.ActiveColor,
                val inAnimationSpec: AnimationSpec<Color>? = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.InAnimationSpec,
                val outAnimationSpec: AnimationSpec<Color>? = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.OutAnimationSpec
            ) : Colored {

                /** The [SolidColor] brush with current [idleActiveColor]. */
                override val brush: Brush
                    get() = SolidColor(value = idleActiveColorState)

                private var idleActiveColorState by mutableStateOf(idleColor)

                /**
                 * The current color (between [idleColor] and [activeColor]).
                 *
                 * @see brush
                 */
                val idleActiveColor: Color
                    get() = idleActiveColorState

                /**
                 * Handles the current [idleActiveColor].
                 *
                 * @param state The current [ScrollbarsState].
                 */
                @Composable
                internal fun HandleIdleActiveColor(state: ScrollbarsState) {
                    val isScrollInProgress = state.scrollType.isScrollInProgress
                    val idleActiveColor = if (isScrollInProgress) {
                        activeColor
                    } else {
                        idleColor
                    }

                    idleActiveColorState = if (inAnimationSpec == null || outAnimationSpec == null) {
                        idleActiveColor
                    } else {
                        animateColorAsState(
                            targetValue = idleActiveColor,
                            animationSpec = if (isScrollInProgress) {
                                inAnimationSpec
                            } else {
                                outAnimationSpec
                            },
                            label = "IdleActiveColor"
                        ).value
                    }
                }
            }
        }
    }
}