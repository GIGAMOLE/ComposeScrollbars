@file:Suppress("unused")
@file:OptIn(ExperimentalComposeUiApi::class)

package com.gigamole.composescrollbars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsGravity
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layercontenttype.layercontentstyletype.ScrollbarsLayerContentStyleType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerGravity
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType
import com.gigamole.composescrollbars.config.sizetype.ScrollbarsSizeType
import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityType
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType

/**
 * The scrollbars main [Composable].
 *
 * The Scrollbars container organised in the following way:
 * - The global container with a max size, a [handleTouchDown] if supported, and a correct [alignment] of the layers container.
 * - The layers container which handles [visibility], [size], [intrinsicSide] and paddings.
 * - The layers container contains two layers content holders: background layer and knob layer, and their behavior is different by [ScrollbarsLayersType].
 *
 * In a [ScrollbarsLayersType.Wrap] the background layer is wrapped around the knob layer and both of them are centered.
 *
 * In a [ScrollbarsLayersType.Split] the background layer and knob layer are configured individually.
 *
 * @param state The required [ScrollbarsState].
 * @param modifier The custom [Modifier].
 * @see ScrollbarsLayerContent
 * @see ScrollbarsConfig
 * @see ScrollbarsScrollType
 * @author GIGAMOLE
 */
@Composable
fun Scrollbars(
    state: ScrollbarsState,
    modifier: Modifier = Modifier
) {
    state.HandleState()

    val orientation = state.config.orientation
    val gravity = state.config.gravity
    val layersType = state.config.layersType
    val startWeight = state.startKnobFraction
    val endWeight = 1.0F - state.endKnobFraction
    val knobWeight = state.endKnobFraction - state.startKnobFraction

    Box(
        modifier = modifier
            .fillMaxSize()
            .handleTouchDown(state = state),
        contentAlignment = alignment(
            orientation = orientation,
            gravity = gravity
        )
    ) {
        Box(
            modifier = Modifier
                .visibility(state = state)
                .size(
                    orientation = orientation,
                    sizeType = state.config.sizeType
                )
                .intrinsicSide(orientation = orientation)
                .padding(paddingValues = state.config.paddingValues)
        ) {
            @Composable
            fun BackgroundLayerContainer(
                modifier: Modifier = Modifier
            ) {
                Box(
                    modifier = modifier.fillMaxSide(orientation = orientation)
                ) {
                    ScrollbarsLayerContent(
                        state = state,
                        layerContentType = state.config.backgroundLayerContentType
                    )
                }
            }

            @Composable
            fun KnobLayerContainer(
                modifier: Modifier = Modifier,
                knobLayerContent: @Composable BoxScope.() -> Unit = {
                    ScrollbarsLayerContent(
                        state = state,
                        layerContentType = state.config.knobLayerContentType
                    )
                }
            ) {
                when (orientation) {
                    ScrollbarsOrientation.Vertical -> {
                        Column(
                            modifier = modifier.fillMaxHeight()
                        ) {
                            Spacer(
                                modifier = Modifier.nullableWeight(
                                    columnScope = this,
                                    weight = startWeight
                                )
                            )
                            Box(
                                modifier = Modifier.nullableWeight(
                                    columnScope = this,
                                    weight = knobWeight
                                ),
                                content = knobLayerContent
                            )
                            Spacer(
                                modifier = Modifier.nullableWeight(
                                    columnScope = this,
                                    weight = endWeight
                                )
                            )
                        }
                    }
                    ScrollbarsOrientation.Horizontal -> {
                        Row(
                            modifier = modifier.fillMaxWidth()
                        ) {
                            Spacer(
                                modifier = Modifier.nullableWeight(
                                    rowScope = this,
                                    weight = startWeight
                                )
                            )
                            Box(
                                modifier = Modifier.nullableWeight(
                                    rowScope = this,
                                    weight = knobWeight
                                ),
                                content = knobLayerContent
                            )
                            Spacer(
                                modifier = Modifier.nullableWeight(
                                    rowScope = this,
                                    weight = endWeight
                                )
                            )
                        }
                    }
                }
            }

            BackgroundLayerContainer(
                modifier = when (layersType) {
                    is ScrollbarsLayersType.Split -> {
                        Modifier
                            .align(
                                alignment = layerAlignment(
                                    orientation = orientation,
                                    layerGravity = layersType.backgroundLayerConfig.layerGravity
                                )
                            )
                            .padding(paddingValues = layersType.backgroundLayerConfig.paddingValues)
                            .thickness(
                                orientation = orientation,
                                thicknessType = layersType.backgroundLayerConfig.thicknessType
                            )
                    }
                    is ScrollbarsLayersType.Wrap -> {
                        Modifier
                            .align(alignment = Alignment.Center)
                            .fillMaxOppositeSide(orientation = orientation)
                    }
                }
            )
            KnobLayerContainer(
                modifier = when (layersType) {
                    is ScrollbarsLayersType.Split -> {
                        Modifier
                            .align(
                                alignment = layerAlignment(
                                    orientation = orientation,
                                    layerGravity = layersType.knobLayerConfig.layerGravity
                                )
                            )
                            .padding(paddingValues = layersType.knobLayerConfig.paddingValues)
                            .thickness(
                                orientation = orientation,
                                thicknessType = layersType.knobLayerConfig.thicknessType
                            )
                    }
                    is ScrollbarsLayersType.Wrap -> {
                        Modifier
                            .align(alignment = Alignment.Center)
                            .padding(paddingValues = layersType.paddingValues)
                            .thickness(
                                orientation = orientation,
                                thicknessType = layersType.thicknessType
                            )
                    }
                }
                    .drawWithContent {
                        state.handleTargetKnobFraction(this)

                        drawContent()
                    }
            )
        }
    }
}

/**
 * The scrollbars layer content [Composable] provider within a [BoxScope], which handles [ScrollbarsLayerContentType].
 *
 * @param state The current [ScrollbarsState].
 * @param layerContentType The [ScrollbarsLayerContentType].
 * @author GIGAMOLE
 */
@Composable
private fun BoxScope.ScrollbarsLayerContent(
    state: ScrollbarsState,
    layerContentType: ScrollbarsLayerContentType
) {
    when (layerContentType) {
        is ScrollbarsLayerContentType.Default -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultLayerContentType(defaultLayerContentType = layerContentType)
            )
        }
        is ScrollbarsLayerContentType.Custom -> {
            layerContentType.content(this, state)
        }
        ScrollbarsLayerContentType.None -> {
            /* Do nothing. */
        }
    }
}

/**
 * The [Modifier] which handles the [ScrollbarsVisibilityType.Dynamic.isVisibleOnTouchDown] indicator.
 *
 * @param state The current [ScrollbarsState].
 * @return The [Modifier] chain with a [handleTouchDown] in it.
 * @author GIGAMOLE
 */
private fun Modifier.handleTouchDown(
    state: ScrollbarsState
): Modifier = then(
    with(state) {
        if (config.visibilityType is ScrollbarsVisibilityType.Dynamic &&
            config.visibilityType.isVisibleOnTouchDown
        ) {
            Modifier
                .pointerInteropFilter(
                    onTouchEvent = {
                        state.handleTouchDown(motionEvent = it)

                        false
                    }
                )
                .pointerInput(this) {
                    awaitEachGesture {
                        state.handleTouchDownRelease(awaitPointerEventScope = this)

                        waitForUpOrCancellation()
                    }
                }
        } else {
            Modifier
        }
    }
)

/**
 * The [Modifier] which handles a [ScrollbarsSizeType] by [ScrollbarsOrientation].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @param sizeType The [ScrollbarsSizeType].
 * @return The [Modifier] chain with a [size] in it.
 * @author GIGAMOLE
 */
private fun Modifier.size(
    orientation: ScrollbarsOrientation,
    sizeType: ScrollbarsSizeType
): Modifier = then(
    when (sizeType) {
        is ScrollbarsSizeType.Exact -> {
            Modifier.exactSize(
                orientation = orientation,
                size = sizeType.size
            )
        }
        is ScrollbarsSizeType.Fraction -> {
            Modifier.fillMaxSide(
                orientation = orientation,
                fraction = sizeType.fraction
            )
        }
        ScrollbarsSizeType.Full -> {
            Modifier.fillMaxSide(
                orientation = orientation,
                fraction = 1.0F
            )
        }
    }
)

/**
 * The [Modifier] which handles an exact size by [ScrollbarsOrientation].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @param size The exact size.
 * @return The [Modifier] chain with an [exactSize] in it.
 * @author GIGAMOLE
 */
private fun Modifier.exactSize(
    orientation: ScrollbarsOrientation,
    size: Dp
): Modifier = then(
    when (orientation) {
        ScrollbarsOrientation.Vertical -> {
            Modifier.height(height = size)
        }
        ScrollbarsOrientation.Horizontal -> {
            Modifier.width(width = size)
        }
    }
)

/**
 * The [Modifier] which handles a filling of max size by [ScrollbarsOrientation] and optional weight [fraction].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @param fraction The optional weight fraction.
 * @return The [Modifier] chain with a [fillMaxSide] in it.
 * @author GIGAMOLE
 */
private fun Modifier.fillMaxSide(
    orientation: ScrollbarsOrientation,
    fraction: Float = 1.0F
): Modifier = then(
    when (orientation) {
        ScrollbarsOrientation.Vertical -> {
            Modifier.fillMaxHeight(fraction = fraction)
        }
        ScrollbarsOrientation.Horizontal -> {
            Modifier.fillMaxWidth(fraction = fraction)
        }
    }
)

/**
 * The [Modifier] which handles a filling of max opposite size (basically, wrap thickness) by [ScrollbarsOrientation].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @return The [Modifier] chain with a [fillMaxOppositeSide] in it.
 * @author GIGAMOLE
 */
private fun Modifier.fillMaxOppositeSide(
    orientation: ScrollbarsOrientation
): Modifier = then(
    when (orientation) {
        ScrollbarsOrientation.Vertical -> {
            Modifier.fillMaxWidth()
        }
        ScrollbarsOrientation.Horizontal -> {
            Modifier.fillMaxHeight()
        }
    }
)

/**
 * The [Modifier] which handles an intrinsic size of a side by [ScrollbarsOrientation].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @return The [Modifier] chain with an [intrinsicSide] in it.
 * @author GIGAMOLE
 */
private fun Modifier.intrinsicSide(
    orientation: ScrollbarsOrientation
): Modifier = then(
    when (orientation) {
        ScrollbarsOrientation.Vertical -> {
            Modifier.width(intrinsicSize = IntrinsicSize.Max)
        }
        ScrollbarsOrientation.Horizontal -> {
            Modifier.height(intrinsicSize = IntrinsicSize.Max)
        }
    }
)

/**
 * The [Modifier] which handles a [ScrollbarsThicknessType] by [ScrollbarsOrientation].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @param thicknessType The [ScrollbarsThicknessType].
 * @return The [Modifier] chain with a [thickness] in it.
 * @author GIGAMOLE
 */
private fun Modifier.thickness(
    orientation: ScrollbarsOrientation,
    thicknessType: ScrollbarsThicknessType
): Modifier = then(
    when (orientation) {
        ScrollbarsOrientation.Vertical -> {
            when (thicknessType) {
                is ScrollbarsThicknessType.Exact -> {
                    Modifier.width(width = thicknessType.thickness)
                }
                ScrollbarsThicknessType.Wrap -> {
                    Modifier
                }
            }
        }
        ScrollbarsOrientation.Horizontal -> {
            when (thicknessType) {
                is ScrollbarsThicknessType.Exact -> {
                    Modifier.height(height = thicknessType.thickness)
                }
                ScrollbarsThicknessType.Wrap -> {
                    Modifier
                }
            }
        }
    }
)

/**
 * The [Modifier] which handles a zero value weight within a [RowScope].
 *
 * @param rowScope The [RowScope].
 * @param weight The weight value.
 * @return The [Modifier] chain with a [nullableWeight] in it.
 * @author GIGAMOLE
 */
private fun Modifier.nullableWeight(
    rowScope: RowScope,
    weight: Float
): Modifier = then(
    if (weight > 0.0F) {
        with(rowScope) {
            Modifier.weight(weight = weight)
        }
    } else {
        Modifier
    }
)

/**
 * The [Modifier] which handles a zero value weight within a [ColumnScope].
 *
 * @param columnScope The [ColumnScope].
 * @param weight The weight value.
 * @return The [Modifier] chain with a [nullableWeight] in it.
 * @author GIGAMOLE
 */
private fun Modifier.nullableWeight(
    columnScope: ColumnScope,
    weight: Float
): Modifier = then(
    if (weight > 0.0F) {
        with(columnScope) {
            Modifier.weight(weight = weight)
        }
    } else {
        Modifier
    }
)

/**
 * The [Modifier] which handles a [ScrollbarsVisibilityType].
 *
 * @param state The current [ScrollbarsState].
 * @return The [Modifier] chain with a [visibility] in it.
 * @author GIGAMOLE
 */
private fun Modifier.visibility(
    state: ScrollbarsState
): Modifier {
    val visibilityType = state.config.visibilityType

    return then(
        when (visibilityType) {
            is ScrollbarsVisibilityType.Dynamic.Fade -> {
                Modifier.alpha(alpha = visibilityType.fadeFraction)
            }
            is ScrollbarsVisibilityType.Dynamic.Scale -> {
                Modifier
                    .scale(scale = visibilityType.scaleValue)
                    .alpha(alpha = visibilityType.fadeFraction)
            }
            is ScrollbarsVisibilityType.Dynamic.Slide -> {
                Modifier
                    .onSizeChanged {
                        // Capture and handle the new size of the layers container.
                        visibilityType.handleTargetSlideOffset(
                            state = state,
                            size = it
                        )
                    }
                    .offset {
                        visibilityType.slideOffset
                    }
                    .alpha(alpha = visibilityType.fadeFraction)
            }
            is ScrollbarsVisibilityType.Static -> {
                Modifier
            }
        }
    )
}

/**
 * The [Modifier] which handles the appearance of [ScrollbarsLayerContentType.Default].
 *
 * @param defaultLayerContentType The [ScrollbarsLayerContentType.Default].
 * @return The [Modifier] chain with a [defaultLayerContentType] in it.
 * @author GIGAMOLE
 */
private fun Modifier.defaultLayerContentType(
    defaultLayerContentType: ScrollbarsLayerContentType.Default
): Modifier = then(
    when (val drawStyleType = defaultLayerContentType.styleType) {
        ScrollbarsLayerContentStyleType.Background -> {
            Modifier.background(
                shape = defaultLayerContentType.shape,
                brush = defaultLayerContentType.brush
            )
        }
        is ScrollbarsLayerContentStyleType.Border -> {
            Modifier.border(
                width = drawStyleType.width,
                shape = defaultLayerContentType.shape,
                brush = defaultLayerContentType.brush
            )
        }
    }
)

/**
 * The [Modifier] which handles a global container content alignment by [ScrollbarsOrientation] and [ScrollbarsGravity].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @param gravity The [ScrollbarsGravity].
 * @return The [Modifier] chain with an [alignment] in it.
 * @author GIGAMOLE
 */
@Composable
private fun alignment(
    orientation: ScrollbarsOrientation,
    gravity: ScrollbarsGravity
): Alignment = when (orientation) {
    ScrollbarsOrientation.Vertical -> {
        when (gravity) {
            ScrollbarsGravity.Start -> {
                Alignment.CenterStart
            }
            ScrollbarsGravity.End -> {
                Alignment.CenterEnd
            }
        }
    }
    ScrollbarsOrientation.Horizontal -> {
        when (gravity) {
            ScrollbarsGravity.Start -> {
                Alignment.TopCenter
            }
            ScrollbarsGravity.End -> {
                Alignment.BottomCenter
            }
        }
    }
}

/**
 * The [Modifier] which handles a scrollbars layer alignment by [ScrollbarsOrientation] and [ScrollbarsLayerGravity].
 *
 * @param orientation The [ScrollbarsOrientation].
 * @param layerGravity The [ScrollbarsLayerGravity].
 * @return The [Modifier] chain with an [layerAlignment] in it.
 * @author GIGAMOLE
 */
@Composable
private fun layerAlignment(
    orientation: ScrollbarsOrientation,
    layerGravity: ScrollbarsLayerGravity
): Alignment = when (orientation) {
    ScrollbarsOrientation.Vertical -> {
        when (layerGravity) {
            ScrollbarsLayerGravity.Start -> {
                Alignment.CenterStart
            }
            ScrollbarsLayerGravity.Center -> {
                Alignment.Center
            }
            ScrollbarsLayerGravity.End -> {
                Alignment.CenterEnd
            }
        }
    }
    ScrollbarsOrientation.Horizontal -> {
        when (layerGravity) {
            ScrollbarsLayerGravity.Start -> {
                Alignment.TopCenter
            }
            ScrollbarsLayerGravity.Center -> {
                Alignment.Center
            }
            ScrollbarsLayerGravity.End -> {
                Alignment.BottomCenter
            }
        }
    }
}
