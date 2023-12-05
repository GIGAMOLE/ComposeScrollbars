@file:Suppress("unused")

package com.gigamole.composescrollbars

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventType
import com.gigamole.composescrollbars.common.subLerp
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityType
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsDynamicKnobType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsStaticKnobType
import kotlin.math.ceil
import kotlin.math.max

/**
 * Remembers the [ScrollbarsState] by a [ScrollbarsScrollType.Scroll].
 *
 * @param config The [ScrollbarsConfig].
 * @param scrollType The [ScrollbarsScrollType.Scroll].
 * @return The [ScrollbarsState].
 * @author GIGAMOLE
 */
@Composable
fun rememberScrollbarsState(
    config: ScrollbarsConfig,
    scrollType: ScrollbarsScrollType.Scroll
): ScrollbarsState = rememberScrollbarsState(
    config = config,
    scrollType = scrollType as ScrollbarsScrollType
)

/**
 * Remembers the [ScrollbarsState] by a [ScrollbarsScrollType.Lazy.List].
 *
 * @param config The [ScrollbarsConfig].
 * @param scrollType The [ScrollbarsScrollType.Lazy.List].
 * @return The [ScrollbarsState].
 * @author GIGAMOLE
 */
@Composable
fun rememberScrollbarsState(
    config: ScrollbarsConfig,
    scrollType: ScrollbarsScrollType.Lazy.List
): ScrollbarsState = rememberScrollbarsState(
    config = config,
    scrollType = scrollType as ScrollbarsScrollType
)

/**
 * Remembers the [ScrollbarsState] by a [ScrollbarsScrollType.Lazy.Grid].
 *
 * @param config The [ScrollbarsConfig].
 * @param scrollType The [ScrollbarsScrollType.Lazy.Grid].
 * @return The [ScrollbarsState].
 * @author GIGAMOLE
 */
@Composable
fun rememberScrollbarsState(
    config: ScrollbarsConfig,
    scrollType: ScrollbarsScrollType.Lazy.Grid
): ScrollbarsState = rememberScrollbarsState(
    config = config,
    scrollType = scrollType as ScrollbarsScrollType
)

/**
 * Remembers the [ScrollbarsState] by a [ScrollbarsScrollType.Lazy.StaggeredGrid].
 *
 * @param config The [ScrollbarsConfig].
 * @param scrollType The [ScrollbarsScrollType.Lazy.StaggeredGrid].
 * @return The [ScrollbarsState].
 * @author GIGAMOLE
 */
@Composable
fun rememberScrollbarsState(
    config: ScrollbarsConfig,
    scrollType: ScrollbarsScrollType.Lazy.StaggeredGrid
): ScrollbarsState = rememberScrollbarsState(
    config = config,
    scrollType = scrollType as ScrollbarsScrollType
)

/**
 * Remembers the [ScrollbarsState] by a generic [ScrollbarsScrollType].
 *
 * @param config The [ScrollbarsConfig].
 * @param scrollType The [ScrollbarsScrollType].
 * @return The [ScrollbarsState].
 * @author GIGAMOLE
 */
@Composable
fun rememberScrollbarsState(
    config: ScrollbarsConfig,
    scrollType: ScrollbarsScrollType
): ScrollbarsState = remember(
    config,
    scrollType
) {
    ScrollbarsState(
        config = config,
        scrollType = scrollType
    )
}

/**
 * The scrollbars state for a [Scrollbars] [Composable].
 *
 * @property config The appearance [ScrollbarsConfig].
 * @property scrollType The [ScrollbarsScrollType] configuration.
 * @author GIGAMOLE
 */
class ScrollbarsState(
    val config: ScrollbarsConfig,
    val scrollType: ScrollbarsScrollType
) {
    private var targetStartKnobFractionState by mutableFloatStateOf(0.0F)
    private var targetEndKnobFractionState by mutableFloatStateOf(0.0F)
    private var targetScrollFractionState by mutableFloatStateOf(0.0F)

    private var startKnobFractionState by mutableFloatStateOf(0.0F)
    private var endKnobFractionState by mutableFloatStateOf(0.0F)
    private var scrollFractionState by mutableFloatStateOf(0.0F)

    private var isTouchDownState by mutableStateOf(false)

    /** The start knob fraction (left or top, depends on [ScrollbarsOrientation]). Range from 0.0F to 1.0F. */
    val startKnobFraction: Float
        get() = startKnobFractionState

    /** The end knob fraction (right or bottom, depends on [ScrollbarsOrientation]). Range from 0.0F to 1.0F. */
    val endKnobFraction: Float
        get() = endKnobFractionState

    /** The overall scroll fraction. Range from 0.0F to 1.0F. */
    val scrollFraction: Float
        get() = scrollFractionState

    /**
     * Indicates whether there is any touch/press down events in progress.
     *
     * @see ScrollbarsVisibilityType.Dynamic.isVisibleOnTouchDown
     */
    val isTouchDown: Boolean
        get() = isTouchDownState

    /**
     * A quick shortcut function to highlight the [ScrollbarsVisibilityType.Dynamic].
     *
     * @see ScrollbarsVisibilityType.Dynamic.highlight
     */
    fun highlight() {
        with(config) {
            if (visibilityType is ScrollbarsVisibilityType.Dynamic) {
                visibilityType.highlight()
            }
        }
    }

    /**
     * Handles the core [ScrollbarsState] params handles.
     *
     * @see ScrollbarsVisibilityType.Dynamic.HandleFraction
     * @see ScrollbarsLayerContentType.Default.Colored.IdleActive.HandleIdleActiveColor
     * @see HandleKnobFraction
     */
    @Composable
    internal fun HandleState() {
        with(config) {
            if (visibilityType is ScrollbarsVisibilityType.Dynamic) {
                visibilityType.HandleFraction(state = this@ScrollbarsState)
            }
            if (backgroundLayerContentType is ScrollbarsLayerContentType.Default.Colored.IdleActive) {
                backgroundLayerContentType.HandleIdleActiveColor(state = this@ScrollbarsState)
            }
            if (knobLayerContentType is ScrollbarsLayerContentType.Default.Colored.IdleActive) {
                knobLayerContentType.HandleIdleActiveColor(state = this@ScrollbarsState)
            }
        }

        HandleKnobFraction()
    }

    /**
     * Handles the knob target start/end fractions and scroll fraction with animation or not.
     *
     * @see startKnobFractionState
     * @see endKnobFractionState
     * @see scrollFractionState
     */
    @Composable
    private fun HandleKnobFraction() {
        val animationSpec = scrollType.knobType.animationSpec

        if (animationSpec == null) {
            startKnobFractionState = targetStartKnobFractionState
            endKnobFractionState = targetEndKnobFractionState
            scrollFractionState = targetScrollFractionState
        } else {
            startKnobFractionState = animateFloatAsState(
                targetValue = targetStartKnobFractionState,
                animationSpec = animationSpec,
                label = "StartKnobFractionState"
            ).value
            endKnobFractionState = animateFloatAsState(
                targetValue = targetEndKnobFractionState,
                animationSpec = animationSpec,
                label = "EndKnobFractionState"
            ).value
            scrollFractionState = animateFloatAsState(
                targetValue = targetScrollFractionState,
                animationSpec = animationSpec,
                label = "ScrollFractionState"
            ).value
        }
    }

    /**
     * Handles the origin touch/press scrollbars down event if needed.
     *
     * @param motionEvent The raw [MotionEvent].
     * @see isTouchDown
     * @see ScrollbarsVisibilityType.Dynamic.cancelAwaitTouchDown
     */
    internal fun handleTouchDown(motionEvent: MotionEvent) {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            isTouchDownState = true

            with(config) {
                if (visibilityType is ScrollbarsVisibilityType.Dynamic) {
                    visibilityType.cancelAwaitTouchDown()
                }
            }
        }
    }

    /**
     * Handles the in progress touch/press scrollbars down event when released.
     *
     * @param awaitPointerEventScope The raw [AwaitPointerEventScope].
     * @see isTouchDown
     * @see ScrollbarsVisibilityType.Dynamic.handleAwaitTouchDownRelease
     */
    internal fun handleTouchDownRelease(awaitPointerEventScope: AwaitPointerEventScope) {
        with(awaitPointerEventScope) {
            if (isTouchDownState) {
                if (currentEvent.type == PointerEventType.Release) {
                    isTouchDownState = false

                    with(config) {
                        if (visibilityType is ScrollbarsVisibilityType.Dynamic) {
                            visibilityType.handleAwaitTouchDownRelease()
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles the knob start/end fraction and scroll fraction targets destinations.
     *
     * @param drawScope The current scrollbars [DrawScope].
     */
    internal fun handleTargetKnobFraction(drawScope: DrawScope) {
        with(drawScope) {
            // Get the viewport size for a correct orientation side.
            val viewportSize = when (config.orientation) {
                ScrollbarsOrientation.Vertical -> {
                    size.height
                }
                ScrollbarsOrientation.Horizontal -> {
                    size.width
                }
            }

            val targetStartKnobFraction: Float
            val targetEndKnobFraction: Float
            val targetScrollFraction: Float

            when (scrollType) {
                is ScrollbarsScrollType.Scroll -> {
                    val knobType = scrollType.knobType
                    val scrollState = scrollType.state

                    if (scrollState.maxValue <= 0 || scrollType.isScrollPossible.not()) {
                        when (knobType) {
                            is ScrollbarsStaticKnobType.Auto -> {
                                targetStartKnobFraction = 0.0F
                                targetEndKnobFraction = 1.0F
                            }
                            is ScrollbarsStaticKnobType.Exact -> {
                                val knobExactSize = knobType.size.toPx()

                                targetStartKnobFraction = 0.0F
                                targetEndKnobFraction = knobExactSize / viewportSize
                            }
                            is ScrollbarsStaticKnobType.Fraction -> {
                                val knobFractionSize = viewportSize * knobType.fraction

                                targetStartKnobFraction = 0.0F
                                targetEndKnobFraction = knobFractionSize / viewportSize
                            }
                        }

                        targetScrollFraction = 0.0F
                    } else {
                        val scrollValue = scrollState.value.toFloat()
                        val scrollMaxValue = scrollState.maxValue.toFloat()
                        val scrollFraction = (scrollValue / scrollMaxValue).coerceIn(0.0F, 1.0F)

                        when (knobType) {
                            is ScrollbarsStaticKnobType.Auto -> {
                                val totalSize = viewportSize + scrollMaxValue

                                targetStartKnobFraction = scrollValue / totalSize
                                targetEndKnobFraction = targetStartKnobFraction + (viewportSize / totalSize)
                            }
                            is ScrollbarsStaticKnobType.Exact -> {
                                val knobExactSize = knobType.size.toPx()
                                val totalSize = viewportSize - knobExactSize

                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                targetEndKnobFraction = targetStartKnobFraction + (knobExactSize / viewportSize)
                            }
                            is ScrollbarsStaticKnobType.Fraction -> {
                                val knobFractionSize = viewportSize * knobType.fraction
                                val totalSize = viewportSize - knobFractionSize

                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                targetEndKnobFraction = targetStartKnobFraction + (knobFractionSize / viewportSize)
                            }
                        }

                        targetScrollFraction = scrollFraction
                    }
                }
                is ScrollbarsScrollType.Lazy -> {
                    when (scrollType) {
                        is ScrollbarsScrollType.Lazy.List -> {
                            val lazyListState = scrollType.state
                            val layoutInfo = lazyListState.layoutInfo
                            val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()

                            when (scrollType) {
                                is ScrollbarsScrollType.Lazy.List.Dynamic -> {
                                    val knobType = scrollType.knobType
                                    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

                                    if (firstVisibleItem == null || lastVisibleItem == null || layoutInfo.totalItemsCount == 0 || scrollType.isScrollPossible.not()) {
                                        when (knobType) {
                                            is ScrollbarsDynamicKnobType.Auto,
                                            is ScrollbarsDynamicKnobType.Worm -> {
                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = 1.0F
                                            }
                                            is ScrollbarsDynamicKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobExactSize / viewportSize
                                            }
                                            is ScrollbarsDynamicKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobFractionSize / viewportSize
                                            }
                                        }

                                        targetScrollFraction = 0.0F
                                    } else {
                                        val itemSize = viewportSize / layoutInfo.totalItemsCount
                                        val itemSizeFraction = itemSize / viewportSize

                                        val firstItemOffsetFix = if (firstVisibleItem.index == 0) {
                                            0.0F
                                        } else {
                                            layoutInfo.beforeContentPadding.toFloat()
                                        }
                                        val firstItemSizeFix = if (firstVisibleItem.index == 0) {
                                            layoutInfo.beforeContentPadding.toFloat()
                                        } else {
                                            0.0F
                                        }
                                        val lastItemSizeFix = if (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) {
                                            layoutInfo.afterContentPadding.toFloat()
                                        } else {
                                            layoutInfo.mainAxisItemSpacing.toFloat()
                                        }

                                        val startSubFraction = ((firstVisibleItem.offset.toFloat() + firstItemOffsetFix) /
                                                (firstVisibleItem.size.toFloat() + layoutInfo.mainAxisItemSpacing + firstItemSizeFix)).coerceIn(-1.0F, 0.0F) *
                                                itemSizeFraction
                                        val endSubFraction = ((lastVisibleItem.offset.toFloat() - layoutInfo.viewportEndOffset.toFloat()) /
                                                (lastVisibleItem.size.toFloat() + lastItemSizeFix)).coerceIn(-1.0F, 0.0F) * itemSizeFraction

                                        val rawStartFraction = ((firstVisibleItem.index * itemSize) / viewportSize - startSubFraction).coerceIn(0.0F, 1.0F)
                                        val rawEndFraction = ((lastVisibleItem.index * itemSize) / viewportSize - endSubFraction).coerceIn(0.0F, 1.0F)

                                        val startScrollFraction = if (rawEndFraction == 1.0F) {
                                            0.5F
                                        } else {
                                            rawStartFraction.subLerp(0.0F, 1.0F - rawEndFraction) * 0.5F
                                        }.coerceIn(0.0F, 1.0F)
                                        val endScrollFraction = if (rawStartFraction == 0.0F) {
                                            0.0F
                                        } else {
                                            rawEndFraction.subLerp(1.0F - rawStartFraction, 1.0F) * 0.5F
                                        }.coerceIn(0.0F, 1.0F)
                                        val scrollFraction = (startScrollFraction + endScrollFraction).coerceIn(0.0F, 1.0F)

                                        when (knobType) {
                                            is ScrollbarsDynamicKnobType.Auto -> {
                                                val knobAutoSize = viewportSize * (itemSizeFraction * 3.5F).coerceIn(0.05F, 0.95F)
                                                val totalSize = viewportSize - knobAutoSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobAutoSize / viewportSize)
                                            }
                                            is ScrollbarsDynamicKnobType.Worm -> {
                                                if (knobType.isSubLerp) {
                                                    targetStartKnobFraction = rawStartFraction
                                                    targetEndKnobFraction = rawEndFraction
                                                } else {
                                                    targetStartKnobFraction = (firstVisibleItem.index * itemSize) / viewportSize
                                                    targetEndKnobFraction = ((lastVisibleItem.index + 1) * itemSize) / viewportSize
                                                }
                                            }
                                            is ScrollbarsDynamicKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()
                                                val totalSize = viewportSize - knobExactSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobExactSize / viewportSize)
                                            }
                                            is ScrollbarsDynamicKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction
                                                val totalSize = viewportSize - knobFractionSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobFractionSize / viewportSize)
                                            }
                                        }

                                        targetScrollFraction = scrollFraction
                                    }
                                }
                                is ScrollbarsScrollType.Lazy.List.Static -> {
                                    val knobType = scrollType.knobType

                                    if (firstVisibleItem == null || layoutInfo.totalItemsCount == 0 || scrollType.isScrollPossible.not()) {
                                        when (knobType) {
                                            is ScrollbarsStaticKnobType.Auto -> {
                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = 1.0F
                                            }
                                            is ScrollbarsStaticKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobExactSize / viewportSize
                                            }
                                            is ScrollbarsStaticKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobFractionSize / viewportSize
                                            }
                                        }

                                        targetScrollFraction = 0.0F
                                    } else {
                                        val itemSize = firstVisibleItem.size.toFloat()
                                        val rawItemsSize = itemSize * layoutInfo.totalItemsCount
                                        val itemsSize = rawItemsSize + layoutInfo.beforeContentPadding + layoutInfo.afterContentPadding +
                                                (layoutInfo.mainAxisItemSpacing * (layoutInfo.totalItemsCount - 1))
                                        val scrollValue = lazyListState.firstVisibleItemIndex * (itemSize + layoutInfo.mainAxisItemSpacing) +
                                                lazyListState.firstVisibleItemScrollOffset
                                        val scrollMaxValue = itemsSize - (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset)
                                        val scrollFraction = (scrollValue / scrollMaxValue).coerceIn(0.0F, 1.0F)

                                        when (knobType) {
                                            is ScrollbarsStaticKnobType.Auto -> {
                                                val totalSize = viewportSize + scrollMaxValue

                                                targetStartKnobFraction = scrollValue / totalSize
                                                targetEndKnobFraction = targetStartKnobFraction + (viewportSize / totalSize)
                                            }
                                            is ScrollbarsStaticKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()
                                                val totalSize = viewportSize - knobExactSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobExactSize / viewportSize)
                                            }
                                            is ScrollbarsStaticKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction
                                                val totalSize = viewportSize - knobFractionSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobFractionSize / viewportSize)
                                            }
                                        }

                                        targetScrollFraction = scrollFraction
                                    }
                                }
                            }
                        }
                        is ScrollbarsScrollType.Lazy.Grid -> {
                            val lazyGridState = scrollType.state
                            val layoutInfo = lazyGridState.layoutInfo
                            val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()

                            when (scrollType) {
                                is ScrollbarsScrollType.Lazy.Grid.Dynamic -> {
                                    val knobType = scrollType.knobType
                                    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

                                    if (firstVisibleItem == null || lastVisibleItem == null || layoutInfo.totalItemsCount == 0 || scrollType.isScrollPossible.not()) {
                                        when (knobType) {
                                            is ScrollbarsDynamicKnobType.Auto,
                                            is ScrollbarsDynamicKnobType.Worm -> {
                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = 1.0F
                                            }
                                            is ScrollbarsDynamicKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobExactSize / viewportSize
                                            }
                                            is ScrollbarsDynamicKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobFractionSize / viewportSize
                                            }
                                        }

                                        targetScrollFraction = 0.0F
                                    } else {
                                        val entriesCount = ceil(layoutInfo.totalItemsCount.toFloat() / scrollType.spanCount.toFloat()).toInt()
                                        val entrySize = viewportSize / entriesCount.toFloat()
                                        val entrySizeFraction = entrySize / viewportSize

                                        val firstVisibleEntryIndex: Int
                                        val lastVisibleEntryIndex: Int

                                        when (layoutInfo.orientation) {
                                            Orientation.Vertical -> {
                                                firstVisibleEntryIndex = firstVisibleItem.row
                                                lastVisibleEntryIndex = lastVisibleItem.row
                                            }
                                            Orientation.Horizontal -> {
                                                firstVisibleEntryIndex = firstVisibleItem.column
                                                lastVisibleEntryIndex = lastVisibleItem.column
                                            }
                                        }

                                        fun LazyGridItemInfo.entryOffset(): Float {
                                            return when (layoutInfo.orientation) {
                                                Orientation.Vertical -> {
                                                    offset.y
                                                }
                                                Orientation.Horizontal -> {
                                                    offset.x
                                                }
                                            }.toFloat()
                                        }

                                        fun entrySize(isFirst: Boolean): Float {
                                            return if (isFirst) {
                                                layoutInfo.visibleItemsInfo.take(scrollType.spanCount)
                                            } else {
                                                layoutInfo.visibleItemsInfo.takeLastWhile {
                                                    when (layoutInfo.orientation) {
                                                        Orientation.Vertical -> {
                                                            it.row == lastVisibleEntryIndex
                                                        }
                                                        Orientation.Horizontal -> {
                                                            it.column == lastVisibleEntryIndex
                                                        }
                                                    }
                                                }
                                            }.maxOf {
                                                when (layoutInfo.orientation) {
                                                    Orientation.Vertical -> {
                                                        it.size.height
                                                    }
                                                    Orientation.Horizontal -> {
                                                        it.size.width
                                                    }
                                                }
                                            }.toFloat()
                                        }

                                        val firstVisibleEntryOffset = firstVisibleItem.entryOffset()
                                        val firstVisibleEntrySize = entrySize(isFirst = true)
                                        val lastVisibleEntryOffset = lastVisibleItem.entryOffset()
                                        val lastVisibleEntrySize = entrySize(isFirst = false)

                                        val firstEntryOffsetFix = if (firstVisibleEntryIndex == 0) {
                                            0.0F
                                        } else {
                                            layoutInfo.beforeContentPadding.toFloat()
                                        }
                                        val firstEntrySizeFix = if (firstVisibleEntryIndex == 0) {
                                            layoutInfo.beforeContentPadding.toFloat()
                                        } else {
                                            0.0F
                                        }
                                        val lastEntrySizeFix = if (lastVisibleEntryIndex == entriesCount - 1) {
                                            layoutInfo.afterContentPadding.toFloat()
                                        } else {
                                            layoutInfo.mainAxisItemSpacing.toFloat()
                                        }

                                        val startSubFraction = ((firstVisibleEntryOffset + firstEntryOffsetFix) /
                                                (firstVisibleEntrySize + layoutInfo.mainAxisItemSpacing + firstEntrySizeFix)).coerceIn(-1.0F, 0.0F) * entrySizeFraction
                                        val endSubFraction = ((lastVisibleEntryOffset - layoutInfo.viewportEndOffset.toFloat()) /
                                                (lastVisibleEntrySize + lastEntrySizeFix)).coerceIn(-1.0F, 0.0F) * entrySizeFraction

                                        val rawStartFraction = ((firstVisibleEntryIndex * entrySize) / viewportSize - startSubFraction).coerceIn(0.0F, 1.0F)
                                        val rawEndFraction = ((lastVisibleEntryIndex * entrySize) / viewportSize - endSubFraction).coerceIn(0.0F, 1.0F)

                                        val startScrollFraction = if (rawEndFraction == 1.0F) {
                                            0.5F
                                        } else {
                                            rawStartFraction.subLerp(0.0F, 1.0F - rawEndFraction) * 0.5F
                                        }.coerceIn(0.0F, 1.0F)
                                        val endScrollFraction = if (rawStartFraction == 0.0F) {
                                            0.0F
                                        } else {
                                            rawEndFraction.subLerp(1.0F - rawStartFraction, 1.0F) * 0.5F
                                        }.coerceIn(0.0F, 1.0F)
                                        val scrollFraction = (startScrollFraction + endScrollFraction).coerceIn(0.0F, 1.0F)

                                        when (knobType) {
                                            is ScrollbarsDynamicKnobType.Auto -> {
                                                val knobAutoSize = viewportSize * (entrySizeFraction * 3.5F).coerceIn(0.05F, 0.95F)
                                                val totalSize = viewportSize - knobAutoSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobAutoSize / viewportSize)
                                            }
                                            is ScrollbarsDynamicKnobType.Worm -> {
                                                if (knobType.isSubLerp) {
                                                    targetStartKnobFraction = rawStartFraction
                                                    targetEndKnobFraction = rawEndFraction
                                                } else {
                                                    targetStartKnobFraction = (firstVisibleEntryIndex * entrySize) / viewportSize
                                                    targetEndKnobFraction = ((lastVisibleEntryIndex + 1) * entrySize) / viewportSize
                                                }
                                            }
                                            is ScrollbarsDynamicKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()
                                                val totalSize = viewportSize - knobExactSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobExactSize / viewportSize)
                                            }
                                            is ScrollbarsDynamicKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction
                                                val totalSize = viewportSize - knobFractionSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobFractionSize / viewportSize)
                                            }
                                        }

                                        targetScrollFraction = scrollFraction
                                    }
                                }
                                is ScrollbarsScrollType.Lazy.Grid.Static -> {
                                    val knobType = scrollType.knobType

                                    if (firstVisibleItem == null || layoutInfo.totalItemsCount == 0 || scrollType.isScrollPossible.not()) {
                                        when (knobType) {
                                            is ScrollbarsStaticKnobType.Auto -> {
                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = 1.0F
                                            }
                                            is ScrollbarsStaticKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobExactSize / viewportSize
                                            }
                                            is ScrollbarsStaticKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction

                                                targetStartKnobFraction = 0.0F
                                                targetEndKnobFraction = knobFractionSize / viewportSize
                                            }
                                        }

                                        targetScrollFraction = 0.0F
                                    } else {
                                        val entrySize = when (layoutInfo.orientation) {
                                            Orientation.Vertical -> {
                                                firstVisibleItem.size.height.toFloat()
                                            }
                                            Orientation.Horizontal -> {
                                                firstVisibleItem.size.width.toFloat()
                                            }
                                        }

                                        val entriesCount = ceil(layoutInfo.totalItemsCount.toFloat() / scrollType.spanCount.toFloat())
                                        val rawEntriesSize = entrySize * entriesCount
                                        val entriesSize = rawEntriesSize + layoutInfo.beforeContentPadding + layoutInfo.afterContentPadding +
                                                (layoutInfo.mainAxisItemSpacing * (entriesCount - 1))
                                        val firstVisibleEntryIndex = lazyGridState.firstVisibleItemIndex / scrollType.spanCount
                                        val scrollValue = firstVisibleEntryIndex * (entrySize + layoutInfo.mainAxisItemSpacing) +
                                                lazyGridState.firstVisibleItemScrollOffset
                                        val scrollMaxValue = entriesSize - (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset)
                                        val scrollFraction = (scrollValue / scrollMaxValue).coerceIn(0.0F, 1.0F)

                                        when (knobType) {
                                            is ScrollbarsStaticKnobType.Auto -> {
                                                val totalSize = viewportSize + scrollMaxValue

                                                targetStartKnobFraction = scrollValue / totalSize
                                                targetEndKnobFraction = targetStartKnobFraction + (viewportSize / totalSize)
                                            }
                                            is ScrollbarsStaticKnobType.Exact -> {
                                                val knobExactSize = knobType.size.toPx()
                                                val totalSize = viewportSize - knobExactSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobExactSize / viewportSize)
                                            }
                                            is ScrollbarsStaticKnobType.Fraction -> {
                                                val knobFractionSize = viewportSize * knobType.fraction
                                                val totalSize = viewportSize - knobFractionSize

                                                targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                                targetEndKnobFraction = targetStartKnobFraction + (knobFractionSize / viewportSize)
                                            }
                                        }

                                        targetScrollFraction = scrollFraction
                                    }
                                }
                            }
                        }
                        is ScrollbarsScrollType.Lazy.StaggeredGrid -> {
                            val lazyGridState = scrollType.state
                            val knobType = scrollType.knobType
                            val layoutInfo = lazyGridState.layoutInfo

                            fun LazyStaggeredGridItemInfo.itemOffset(): Float {
                                return when (layoutInfo.orientation) {
                                    Orientation.Vertical -> {
                                        offset.y
                                    }
                                    Orientation.Horizontal -> {
                                        offset.x
                                    }
                                }.toFloat() - if (index == 0) {
                                    0.0F
                                } else {
                                    layoutInfo.mainAxisItemSpacing.toFloat()
                                }
                            }

                            fun LazyStaggeredGridItemInfo.lastItemOffset(): Float {
                                return when (layoutInfo.orientation) {
                                    Orientation.Vertical -> {
                                        offset.y
                                    }
                                    Orientation.Horizontal -> {
                                        offset.x
                                    }
                                }.toFloat()
                            }

                            fun LazyStaggeredGridItemInfo.itemSize(): Float {
                                return when (layoutInfo.orientation) {
                                    Orientation.Vertical -> {
                                        size.height
                                    }
                                    Orientation.Horizontal -> {
                                        size.width
                                    }
                                }.toFloat() - if (index == 0) {
                                    layoutInfo.mainAxisItemSpacing.toFloat()
                                } else {
                                    0.0F
                                }
                            }

                            fun LazyStaggeredGridItemInfo.lastItemSize(): Float {
                                return itemSize()
                            }

                            fun LazyStaggeredGridItemInfo.lastItemEndOffset(): Float {
                                return lastItemOffset() + lastItemSize()
                            }

                            val firstVisibleItem = layoutInfo.visibleItemsInfo.filter {
                                it.itemOffset() <= 0.0F
                            }.maxByOrNull {
                                it.itemOffset()
                            }
                            val lastVisibleItem = layoutInfo.visibleItemsInfo.filter {
                                it.lastItemEndOffset() >= layoutInfo.viewportEndOffset - layoutInfo.afterContentPadding &&
                                        it.lastItemOffset() <= layoutInfo.viewportEndOffset - layoutInfo.afterContentPadding
                            }.minByOrNull {
                                it.index
                            } ?: firstVisibleItem

                            if (firstVisibleItem == null || lastVisibleItem == null || layoutInfo.totalItemsCount == 0 || scrollType.isScrollPossible.not()) {
                                when (knobType) {
                                    is ScrollbarsDynamicKnobType.Auto,
                                    is ScrollbarsDynamicKnobType.Worm -> {
                                        targetStartKnobFraction = 0.0F
                                        targetEndKnobFraction = 1.0F
                                    }
                                    is ScrollbarsDynamicKnobType.Exact -> {
                                        val knobExactSize = knobType.size.toPx()

                                        targetStartKnobFraction = 0.0F
                                        targetEndKnobFraction = knobExactSize / viewportSize
                                    }
                                    is ScrollbarsDynamicKnobType.Fraction -> {
                                        val knobFractionSize = viewportSize * knobType.fraction

                                        targetStartKnobFraction = 0.0F
                                        targetEndKnobFraction = knobFractionSize / viewportSize
                                    }
                                }

                                targetScrollFraction = 0.0F
                            } else {
                                val itemsCount = layoutInfo.totalItemsCount
                                val itemSize = viewportSize / itemsCount.toFloat()
                                val itemSizeFraction = itemSize / viewportSize

                                val firstVisibleItemOffset = firstVisibleItem.itemOffset()
                                val firstVisibleItemSize = firstVisibleItem.itemSize()
                                val lastVisibleItemOffset = lastVisibleItem.lastItemOffset()
                                val lastVisibleItemEndOffset = lastVisibleItem.lastItemEndOffset() + layoutInfo.afterContentPadding
                                val lastVisibleItemSize = lastVisibleItem.lastItemSize()

                                val nextFirstVisibleItem = layoutInfo.visibleItemsInfo.minus(firstVisibleItem).find {
                                    val offset = it.itemOffset()

                                    offset > 0.0F && offset <= firstVisibleItemSize
                                } ?: firstVisibleItem
                                val nextFirstVisibleItemOffset = nextFirstVisibleItem.itemOffset()

                                val firstVisibleItemAlignedSize = if (nextFirstVisibleItemOffset == firstVisibleItemOffset) {
                                    firstVisibleItemSize
                                } else {
                                    nextFirstVisibleItemOffset - firstVisibleItemOffset
                                }
                                val firstVisibleItemAlignedIndex = if (nextFirstVisibleItem.index == firstVisibleItem.index) {
                                    1
                                } else {
                                    nextFirstVisibleItem.index - firstVisibleItem.index
                                }

                                val preLastVisibleItem = layoutInfo.visibleItemsInfo.minus(lastVisibleItem).filter {
                                    val endOffset = it.lastItemEndOffset()

                                    endOffset <= layoutInfo.viewportEndOffset &&
                                            endOffset >= lastVisibleItemOffset &&
                                            it.index < lastVisibleItem.index
                                }.maxByOrNull {
                                    it.lastItemEndOffset()
                                } ?: lastVisibleItem
                                val preLastVisibleItemOffset = preLastVisibleItem.lastItemOffset()
                                val preLastVisibleItemEndOffset = preLastVisibleItem.lastItemEndOffset() + layoutInfo.afterContentPadding

                                val lastVisibleItemAlignedSize = if (preLastVisibleItemOffset == lastVisibleItemOffset) {
                                    lastVisibleItemSize
                                } else {
                                    lastVisibleItemEndOffset - preLastVisibleItemEndOffset
                                }
                                val lastVisibleItemAlignedIndex = if (preLastVisibleItem.index == lastVisibleItem.index) {
                                    1
                                } else {
                                    lastVisibleItem.index - preLastVisibleItem.index
                                }
                                val lastVisibleItemAlignedOffset = lastVisibleItemEndOffset - lastVisibleItemAlignedSize

                                val firstVisibleItemSizeFraction = if (firstVisibleItem.index == 0 && nextFirstVisibleItem.index != 0) {
                                    itemSizeFraction * nextFirstVisibleItem.index
                                } else {
                                    itemSizeFraction * firstVisibleItemAlignedIndex
                                }

                                var lastVisibleItemSpacing = 0.0F

                                val realLastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                                val realLastEndSubFraction = if (realLastVisibleItem == null) {
                                    0.0F
                                } else {
                                    val realLastVisibleItemEndOffset = realLastVisibleItem.lastItemEndOffset() + layoutInfo.afterContentPadding

                                    if (realLastVisibleItem.index == lastVisibleItem.index && lastVisibleItem.index != 0) {
                                        lastVisibleItemSpacing = layoutInfo.mainAxisItemSpacing.toFloat()
                                    }

                                    if (
                                        realLastVisibleItem.index == layoutInfo.totalItemsCount - 1 &&
                                        lastVisibleItem.index != realLastVisibleItem.index
                                    ) {
                                        val realLastVisibleItemLeftoverOffset: Float
                                        val realLastVisibleItemLeftoverSize: Float

                                        if (lastVisibleItemEndOffset == realLastVisibleItemEndOffset) {
                                            realLastVisibleItemLeftoverOffset = realLastVisibleItem.lastItemOffset()
                                            realLastVisibleItemLeftoverSize = realLastVisibleItem.lastItemSize() - layoutInfo.mainAxisItemSpacing.toFloat()
                                        } else {
                                            val maxLastVisibleItem = layoutInfo.visibleItemsInfo.maxByOrNull { it.lastItemEndOffset() + layoutInfo.afterContentPadding }

                                            if (maxLastVisibleItem == lastVisibleItem && lastVisibleItemEndOffset >= realLastVisibleItemEndOffset) {
                                                realLastVisibleItemLeftoverOffset = realLastVisibleItemEndOffset - layoutInfo.mainAxisItemSpacing.toFloat()
                                                realLastVisibleItemLeftoverSize = lastVisibleItemEndOffset - realLastVisibleItemEndOffset
                                            } else if (
                                                maxLastVisibleItem == realLastVisibleItem &&
                                                realLastVisibleItemEndOffset >= lastVisibleItemEndOffset &&
                                                realLastVisibleItemEndOffset - lastVisibleItemEndOffset <= layoutInfo.mainAxisItemSpacing.toFloat()
                                            ) {
                                                realLastVisibleItemLeftoverOffset = lastVisibleItemEndOffset - layoutInfo.mainAxisItemSpacing.toFloat()
                                                realLastVisibleItemLeftoverSize = realLastVisibleItemEndOffset - lastVisibleItemEndOffset
                                            } else if (
                                                maxLastVisibleItem != null &&
                                                maxLastVisibleItem != lastVisibleItem &&
                                                maxLastVisibleItem != realLastVisibleItem
                                            ) {
                                                val maxLastVisibleItemEndOffset = maxLastVisibleItem.lastItemEndOffset() + layoutInfo.afterContentPadding
                                                val preMaxVisibleItemEndOffset = max(lastVisibleItemEndOffset, realLastVisibleItemEndOffset)

                                                if (maxLastVisibleItemEndOffset - preMaxVisibleItemEndOffset <= layoutInfo.mainAxisItemSpacing.toFloat()) {
                                                    realLastVisibleItemLeftoverOffset = maxLastVisibleItemEndOffset - layoutInfo.mainAxisItemSpacing.toFloat()
                                                    realLastVisibleItemLeftoverSize = maxLastVisibleItemEndOffset - preMaxVisibleItemEndOffset
                                                } else {
                                                    realLastVisibleItemLeftoverOffset = Float.NaN
                                                    realLastVisibleItemLeftoverSize = Float.NaN
                                                }
                                            } else {
                                                realLastVisibleItemLeftoverOffset = Float.NaN
                                                realLastVisibleItemLeftoverSize = Float.NaN
                                            }
                                        }

                                        if (realLastVisibleItemLeftoverOffset.isNaN() || realLastVisibleItemLeftoverSize.isNaN()) {
                                            0.0F
                                        } else {
                                            if (lastVisibleItem.index != 0) {
                                                lastVisibleItemSpacing = layoutInfo.mainAxisItemSpacing.toFloat()
                                            }

                                            val realLastVisibleEntrySizeFraction = itemSizeFraction * (realLastVisibleItem.index - lastVisibleItem.index)

                                            ((realLastVisibleItemLeftoverOffset - layoutInfo.viewportEndOffset.toFloat()) /
                                                    realLastVisibleItemLeftoverSize).coerceIn(-1.0F, 0.0F) * realLastVisibleEntrySizeFraction
                                        }
                                    } else {
                                        0.0F
                                    }
                                }

                                val lastVisibleItemSizeFraction = itemSizeFraction * lastVisibleItemAlignedIndex
                                val lastVisibleItemIndex = if (lastVisibleItem.index == layoutInfo.totalItemsCount - 1) {
                                    layoutInfo.totalItemsCount - lastVisibleItemAlignedIndex
                                } else {
                                    lastVisibleItem.index + 1 - lastVisibleItemAlignedIndex
                                }

                                val startSubFraction = (firstVisibleItemOffset / firstVisibleItemAlignedSize).coerceIn(-1.0F, 0.0F) * firstVisibleItemSizeFraction
                                val endSubFraction = ((lastVisibleItemAlignedOffset - layoutInfo.viewportEndOffset.toFloat())
                                        / (lastVisibleItemAlignedSize - lastVisibleItemSpacing)).coerceIn(-1.0F, 0.0F) * lastVisibleItemSizeFraction

                                val rawStartFraction = ((firstVisibleItem.index * itemSize) / viewportSize - startSubFraction).coerceIn(0.0F, 1.0F)
                                val rawEndFraction = ((lastVisibleItemIndex * itemSize) / viewportSize - (endSubFraction + realLastEndSubFraction)).coerceIn(0.0F, 1.0F)

                                val startScrollFraction = if (rawEndFraction == 1.0F) {
                                    0.5F
                                } else {
                                    rawStartFraction.subLerp(0.0F, 1.0F - rawEndFraction) * 0.5F
                                }.coerceIn(0.0F, 1.0F)
                                val endScrollFraction = if (rawStartFraction == 0.0F) {
                                    0.0F
                                } else {
                                    rawEndFraction.subLerp(1.0F - rawStartFraction, 1.0F) * 0.5F
                                }.coerceIn(0.0F, 1.0F)
                                val scrollFraction = (startScrollFraction + endScrollFraction).coerceIn(0.0F, 1.0F)

                                when (knobType) {
                                    is ScrollbarsDynamicKnobType.Auto -> {
                                        val knobAutoSize = viewportSize * (itemSizeFraction * 3.5F * scrollType.spanCount.toFloat()).coerceIn(0.05F, 0.95F)
                                        val totalSize = viewportSize - knobAutoSize

                                        targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                        targetEndKnobFraction = targetStartKnobFraction + (knobAutoSize / viewportSize)
                                    }
                                    is ScrollbarsDynamicKnobType.Worm -> {
                                        if (knobType.isSubLerp) {
                                            targetStartKnobFraction = rawStartFraction
                                            targetEndKnobFraction = rawEndFraction
                                        } else {
                                            val firstWormItemIndex = layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0
                                            val lastWormItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: (layoutInfo.totalItemsCount - 1)) + 1

                                            targetStartKnobFraction = (firstWormItemIndex * itemSize) / viewportSize
                                            targetEndKnobFraction = (lastWormItemIndex * itemSize) / viewportSize
                                        }
                                    }
                                    is ScrollbarsDynamicKnobType.Exact -> {
                                        val knobExactSize = knobType.size.toPx()
                                        val totalSize = viewportSize - knobExactSize

                                        targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                        targetEndKnobFraction = targetStartKnobFraction + (knobExactSize / viewportSize)
                                    }
                                    is ScrollbarsDynamicKnobType.Fraction -> {
                                        val knobFractionSize = viewportSize * knobType.fraction
                                        val totalSize = viewportSize - knobFractionSize

                                        targetStartKnobFraction = scrollFraction * totalSize / viewportSize
                                        targetEndKnobFraction = targetStartKnobFraction + (knobFractionSize / viewportSize)
                                    }
                                }

                                targetScrollFraction = scrollFraction
                            }
                        }
                    }
                }
            }

            val layoutTargetStartKnobFraction = targetStartKnobFraction.coerceIn(0.0F, 1.0F)
            val layoutTargetEndKnobFraction = targetEndKnobFraction.coerceIn(0.0F, 1.0F)
            val layoutTargetScrollFraction = targetScrollFraction.coerceIn(0.0F, 1.0F)

            if (config.isReverseLayout) {
                targetStartKnobFractionState = 1.0F - layoutTargetEndKnobFraction
                targetEndKnobFractionState = 1.0F - layoutTargetStartKnobFraction
                targetScrollFractionState = 1.0F - layoutTargetScrollFraction
            } else {
                targetStartKnobFractionState = layoutTargetStartKnobFraction
                targetEndKnobFractionState = layoutTargetEndKnobFraction
                targetScrollFractionState = layoutTargetScrollFraction
            }
        }
    }
}