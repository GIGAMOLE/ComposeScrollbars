@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.gigamole.composescrollbars.sample

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.gigamole.composescrollbars.Scrollbars
import com.gigamole.composescrollbars.common.subLerp
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsConfigDefaults
import com.gigamole.composescrollbars.config.ScrollbarsGravity
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentTypeDefaults
import com.gigamole.composescrollbars.config.layercontenttype.layercontentstyletype.ScrollbarsLayerContentStyleType
import com.gigamole.composescrollbars.config.layercontenttype.layercontentstyletype.ScrollbarsLayerContentStyleTypeDefaults
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersTypeDefaults
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerConfig
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerConfigDefaults
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerGravity
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessTypeDefaults
import com.gigamole.composescrollbars.config.sizetype.ScrollbarsSizeType
import com.gigamole.composescrollbars.config.sizetype.ScrollbarsSizeTypeDefaults
import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityType
import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityTypeDefaults
import com.gigamole.composescrollbars.rememberScrollbarsState
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsDynamicKnobType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsKnobTypeDefaults
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsStaticKnobType
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import kotlinx.coroutines.launch
import kotlin.random.Random

private val sampleBgColors = listOf(
    Color(0xFF00A6FB),
    Color(0xFF0582CA),
    Color(0xFF006494),
    Color(0xFF003554),
    Color(0xFF051923),
)

@Composable
fun MainScreenContent() {
    val rawScrollState = rememberScrollState()
    val rawLazyListState = rememberLazyListState()
    val rawLazyGridState = rememberLazyGridState()
    val rawLazyStaggeredGridState = rememberLazyStaggeredGridState()
    val configScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Scroll type.
    var scrollbarsSampleScrollType by remember { mutableStateOf(SampleScrollType.Scroll) }

    // Items setup.
    var itemsCount by remember { mutableIntStateOf(10) }
    var gridItemsCount by remember { mutableIntStateOf(20) }
    var itemAdditionalSize by remember { mutableStateOf(0.dp) }
    var contentSpacing by remember { mutableStateOf(20.dp) }
    var itemsSpacing by remember { mutableStateOf(0.dp) }
    var itemsRandomHeight by remember { mutableStateOf(false) }
    var isIdleColorPickerVisible by remember { mutableStateOf(false) }
    var isActiveColorPickerVisible by remember { mutableStateOf(false) }
    var randomSeed by remember { mutableIntStateOf(0) }
    var scrollbarsSampleGridSpanCount by remember { mutableStateOf(SampleGridSpanCount.Span_2) }

    // Scrollbars config setup.
    var scrollbarsOrientation by remember { mutableStateOf(ScrollbarsOrientation.Vertical) }
    var scrollbarsSampleExampleType by remember { mutableStateOf(SampleScrollbarsExampleType.Default) }
    var scrollbarsGravity by remember { mutableStateOf(ScrollbarsConfigDefaults.Gravity) }
    var scrollbarsHorizontalPaddingValue by remember { mutableStateOf(ScrollbarsConfigDefaults.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)) }
    var scrollbarsVerticalPaddingValue by remember { mutableStateOf(ScrollbarsConfigDefaults.PaddingValues.calculateTopPadding()) }
    var scrollbarsSampleSizeType by remember { mutableStateOf(SampleSizeType.Full) }
    var scrollbarsSizeFraction by remember { mutableFloatStateOf(ScrollbarsSizeTypeDefaults.Fraction.Fraction) }
    var scrollbarsSizeExact by remember { mutableStateOf(ScrollbarsSizeTypeDefaults.Exact.Size) }
    var scrollbarsSampleLayersType by remember { mutableStateOf(SampleLayersType.Wrap) }
    var scrollbarsWrapThickness by remember { mutableStateOf(ScrollbarsThicknessTypeDefaults.Exact.Thickness) }
    var scrollbarsWrapHorizontalPaddingValue by remember { mutableStateOf(ScrollbarsLayersTypeDefaults.Wrap.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)) }
    var scrollbarsWrapVerticalPaddingValue by remember { mutableStateOf(ScrollbarsLayersTypeDefaults.Wrap.PaddingValues.calculateTopPadding()) }
    var scrollbarsSplitConfigureLayerType by remember { mutableStateOf(SampleConfigureLayerType.Knob) }
    var scrollbarsBackgroundLayerGravity by remember { mutableStateOf(ScrollbarsLayerConfigDefaults.Gravity) }
    var scrollbarsBackgroundLayerThickness by remember { mutableStateOf(ScrollbarsThicknessTypeDefaults.Exact.Thickness) }
    var scrollbarsBackgroundLayerHorizontalPaddingValue by remember { mutableStateOf(ScrollbarsLayerConfigDefaults.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)) }
    var scrollbarsBackgroundLayerVerticalPaddingValue by remember { mutableStateOf(ScrollbarsLayerConfigDefaults.PaddingValues.calculateTopPadding()) }
    var scrollbarsKnobLayerGravity by remember { mutableStateOf(ScrollbarsLayerConfigDefaults.Gravity) }
    var scrollbarsKnobLayerThickness by remember { mutableStateOf(ScrollbarsThicknessTypeDefaults.Exact.Thickness) }
    var scrollbarsKnobLayerHorizontalPaddingValue by remember { mutableStateOf(ScrollbarsLayerConfigDefaults.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)) }
    var scrollbarsKnobLayerVerticalPaddingValue by remember { mutableStateOf(ScrollbarsLayerConfigDefaults.PaddingValues.calculateTopPadding()) }

    // Scrollbars background layer setup.
    var scrollbarsContentConfigureLayerType by remember { mutableStateOf(SampleConfigureLayerType.Knob) }
    var scrollbarsSampleBackgroundLayerContentType by remember { mutableStateOf(SampleLayerContentType.None) }
    var scrollbarsSampleBackgroundLayerContentShape by remember { mutableStateOf(SampleDefaultLayerContentShape.Rounded) }
    var scrollbarsSampleBackgroundLayerContentStyle by remember { mutableStateOf(SampleDefaultLayerContentStyle.Background) }
    var scrollbarsBackgroundLayerContentStyleBorderWidth by remember { mutableStateOf(ScrollbarsLayerContentStyleTypeDefaults.Border.Width) }
    var scrollbarsBackgroundLayerContentIdleColor by remember { mutableStateOf(ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleColor) }
    var scrollbarsBackgroundLayerContentActiveColor by remember { mutableStateOf(ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.ActiveColor) }
    var scrollbarsSampleBackgroundIdleActiveAnimationSpec by remember { mutableStateOf(SampleIdleActiveAnimationSpec.Default) }

    // Scrollbars knob layer setup.
    var scrollbarsSampleKnobLayerContentType by remember { mutableStateOf(SampleLayerContentType.IdleActiveColored) }
    var scrollbarsSampleKnobLayerContentShape by remember { mutableStateOf(SampleDefaultLayerContentShape.Rounded) }
    var scrollbarsSampleKnobLayerContentStyle by remember { mutableStateOf(SampleDefaultLayerContentStyle.Background) }
    var scrollbarsKnobLayerContentStyleBorderWidth by remember { mutableStateOf(ScrollbarsLayerContentStyleTypeDefaults.Border.Width) }
    var scrollbarsKnobLayerContentIdleColor by remember { mutableStateOf(ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleColor) }
    var scrollbarsKnobLayerContentActiveColor by remember { mutableStateOf(ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.ActiveColor) }
    var scrollbarsSampleKnobIdleActiveAnimationSpec by remember { mutableStateOf(SampleIdleActiveAnimationSpec.Default) }

    // Scrollbars visibility type setup.
    var scrollbarsSampleVisibilityType by remember { mutableStateOf(SampleVisibilityType.Fade) }
    var scrollbarsDynamicVisibilityAnimationSpec by remember { mutableStateOf(SampleDynamicVisibilityAnimationSpec.Default) }
    var scrollbarsDynamicVisibilityIsFaded by remember { mutableStateOf(ScrollbarsVisibilityTypeDefaults.Dynamic.IsFaded) }
    var scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible by remember { mutableStateOf(ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleWhenScrollNotPossible) }
    var scrollbarsDynamicVisibilityIsVisibleOnTouchDown by remember { mutableStateOf(ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleOnTouchDown) }
    var scrollbarsDynamicVisibilityIsStaticWhenScrollPossible by remember { mutableStateOf(ScrollbarsVisibilityTypeDefaults.Dynamic.IsStaticWhenScrollPossible) }
    var scrollbarsScaleVisibilityStartScale by remember { mutableFloatStateOf(ScrollbarsVisibilityTypeDefaults.Dynamic.Scale.StartScale) }

    // Scrollbars knob type setup.
    var scrollbarsSampleStaticKnobType by remember { mutableStateOf(SampleStaticKnobType.Auto) }
    var scrollbarsSampleDynamicKnobType by remember { mutableStateOf(SampleDynamicKnobType.Auto) }
    var scrollbarsSampleKnobTypeAnimationSpec by remember { mutableStateOf(SampleKnobTypeAnimationSpec.Spring) }
    var scrollbarsFractionKnobSizeFraction by remember { mutableFloatStateOf(ScrollbarsKnobTypeDefaults.Fraction.Fraction) }
    var scrollbarsExactKnobSize by remember { mutableStateOf(ScrollbarsKnobTypeDefaults.Exact.Size) }
    var scrollbarsDynamicKnobIsSubLerp by remember { mutableStateOf(ScrollbarsKnobTypeDefaults.Worm.IsSubLerp) }

    val scrollbarsGridSpanCount by remember(scrollbarsSampleGridSpanCount) {
        derivedStateOf {
            scrollbarsSampleGridSpanCount.spanCount
        }
    }
    val scrollbarsPaddingValues by remember(
        scrollbarsHorizontalPaddingValue,
        scrollbarsVerticalPaddingValue
    ) {
        derivedStateOf {
            PaddingValues(
                horizontal = scrollbarsHorizontalPaddingValue,
                vertical = scrollbarsVerticalPaddingValue
            )
        }
    }
    val scrollbarsSizeType by remember(
        scrollbarsSampleSizeType,
        scrollbarsSizeFraction,
        scrollbarsSizeExact
    ) {
        derivedStateOf {
            when (scrollbarsSampleSizeType) {
                SampleSizeType.Full -> {
                    ScrollbarsSizeType.Full
                }
                SampleSizeType.Fraction -> {
                    ScrollbarsSizeType.Fraction(fraction = scrollbarsSizeFraction)
                }
                SampleSizeType.Exact -> {
                    ScrollbarsSizeType.Exact(size = scrollbarsSizeExact)
                }
            }
        }
    }
    val scrollbarsWrapPaddingValues by remember(
        scrollbarsWrapHorizontalPaddingValue,
        scrollbarsWrapVerticalPaddingValue,
    ) {
        derivedStateOf {
            PaddingValues(
                horizontal = scrollbarsWrapHorizontalPaddingValue,
                vertical = scrollbarsWrapVerticalPaddingValue
            )
        }
    }
    val scrollbarsBackgroundLayerPaddingValues by remember(
        scrollbarsBackgroundLayerHorizontalPaddingValue,
        scrollbarsBackgroundLayerVerticalPaddingValue,
    ) {
        derivedStateOf {
            PaddingValues(
                horizontal = scrollbarsBackgroundLayerHorizontalPaddingValue,
                vertical = scrollbarsBackgroundLayerVerticalPaddingValue
            )
        }
    }
    val scrollbarsKnobLayerPaddingValues by remember(
        scrollbarsKnobLayerHorizontalPaddingValue,
        scrollbarsKnobLayerVerticalPaddingValue,
    ) {
        derivedStateOf {
            PaddingValues(
                horizontal = scrollbarsKnobLayerHorizontalPaddingValue,
                vertical = scrollbarsKnobLayerVerticalPaddingValue
            )
        }
    }
    val scrollbarsLayersType by remember(
        scrollbarsSampleLayersType,
        scrollbarsWrapThickness,
        scrollbarsWrapPaddingValues,
        scrollbarsBackgroundLayerGravity,
        scrollbarsBackgroundLayerThickness,
        scrollbarsBackgroundLayerPaddingValues,
        scrollbarsKnobLayerGravity,
        scrollbarsKnobLayerThickness,
        scrollbarsKnobLayerPaddingValues
    ) {
        derivedStateOf {
            when (scrollbarsSampleLayersType) {
                SampleLayersType.Wrap -> {
                    ScrollbarsLayersType.Wrap(
                        thicknessType = ScrollbarsThicknessType.Exact(thickness = scrollbarsWrapThickness),
                        paddingValues = scrollbarsWrapPaddingValues
                    )
                }
                SampleLayersType.Split -> {
                    ScrollbarsLayersType.Split(
                        backgroundLayerConfig = ScrollbarsLayerConfig(
                            thicknessType = ScrollbarsThicknessType.Exact(thickness = scrollbarsBackgroundLayerThickness),
                            layerGravity = scrollbarsBackgroundLayerGravity,
                            paddingValues = scrollbarsBackgroundLayerPaddingValues
                        ),
                        knobLayerConfig = ScrollbarsLayerConfig(
                            thicknessType = ScrollbarsThicknessType.Exact(thickness = scrollbarsKnobLayerThickness),
                            layerGravity = scrollbarsKnobLayerGravity,
                            paddingValues = scrollbarsKnobLayerPaddingValues
                        )
                    )
                }
            }
        }
    }
    val scrollbarsBackgroundLayerContentShape by remember(scrollbarsSampleBackgroundLayerContentShape) {
        derivedStateOf {
            when (scrollbarsSampleBackgroundLayerContentShape) {
                SampleDefaultLayerContentShape.Rect -> {
                    RectangleShape
                }
                SampleDefaultLayerContentShape.Rounded -> {
                    RoundedCornerShape(percent = 100)
                }
                SampleDefaultLayerContentShape.Diamond -> {
                    CutCornerShape(percent = 100)
                }
            }
        }
    }
    val scrollbarsBackgroundLayerContentStyle by remember(
        scrollbarsSampleBackgroundLayerContentStyle,
        scrollbarsBackgroundLayerContentStyleBorderWidth
    ) {
        derivedStateOf {
            when (scrollbarsSampleBackgroundLayerContentStyle) {
                SampleDefaultLayerContentStyle.Background -> {
                    ScrollbarsLayerContentStyleType.Background
                }
                SampleDefaultLayerContentStyle.Border -> {
                    ScrollbarsLayerContentStyleType.Border(width = scrollbarsBackgroundLayerContentStyleBorderWidth)
                }
            }
        }
    }
    val scrollbarsBackgroundIdleActiveInAnimationSpec: AnimationSpec<Color>? by remember(scrollbarsSampleBackgroundIdleActiveAnimationSpec) {
        derivedStateOf {
            when (scrollbarsSampleBackgroundIdleActiveAnimationSpec) {
                SampleIdleActiveAnimationSpec.None -> {
                    null
                }
                SampleIdleActiveAnimationSpec.Spring -> {
                    spring()
                }
                SampleIdleActiveAnimationSpec.Default -> {
                    ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.InAnimationSpec
                }
            }
        }
    }
    val scrollbarsBackgroundIdleActiveOutAnimationSpec: AnimationSpec<Color>? by remember(scrollbarsSampleBackgroundIdleActiveAnimationSpec) {
        derivedStateOf {
            when (scrollbarsSampleBackgroundIdleActiveAnimationSpec) {
                SampleIdleActiveAnimationSpec.None -> {
                    null
                }
                SampleIdleActiveAnimationSpec.Spring -> {
                    spring()
                }
                SampleIdleActiveAnimationSpec.Default -> {
                    ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.OutAnimationSpec
                }
            }
        }
    }
    val scrollbarsBackgroundLayerContentType by remember(
        scrollbarsSampleBackgroundLayerContentType,
        scrollbarsBackgroundLayerContentShape,
        scrollbarsBackgroundLayerContentStyle,
        scrollbarsBackgroundLayerContentIdleColor,
        scrollbarsBackgroundLayerContentActiveColor,
        scrollbarsBackgroundIdleActiveInAnimationSpec,
        scrollbarsBackgroundIdleActiveOutAnimationSpec
    ) {
        derivedStateOf {
            when (scrollbarsSampleBackgroundLayerContentType) {
                SampleLayerContentType.None -> {
                    ScrollbarsLayerContentType.None
                }
                SampleLayerContentType.IdleColored -> {
                    ScrollbarsLayerContentType.Default.Colored.Idle(
                        idleColor = scrollbarsBackgroundLayerContentIdleColor,
                        shape = scrollbarsBackgroundLayerContentShape,
                        styleType = scrollbarsBackgroundLayerContentStyle
                    )
                }
                SampleLayerContentType.IdleActiveColored -> {
                    ScrollbarsLayerContentType.Default.Colored.IdleActive(
                        idleColor = scrollbarsBackgroundLayerContentIdleColor,
                        shape = scrollbarsBackgroundLayerContentShape,
                        styleType = scrollbarsBackgroundLayerContentStyle,
                        activeColor = scrollbarsBackgroundLayerContentActiveColor,
                        inAnimationSpec = scrollbarsBackgroundIdleActiveInAnimationSpec,
                        outAnimationSpec = scrollbarsBackgroundIdleActiveOutAnimationSpec
                    )
                }
            }
        }
    }
    val scrollbarsKnobLayerContentShape by remember(scrollbarsSampleKnobLayerContentShape) {
        derivedStateOf {
            when (scrollbarsSampleKnobLayerContentShape) {
                SampleDefaultLayerContentShape.Rect -> {
                    RectangleShape
                }
                SampleDefaultLayerContentShape.Rounded -> {
                    RoundedCornerShape(percent = 100)
                }
                SampleDefaultLayerContentShape.Diamond -> {
                    CutCornerShape(percent = 100)
                }
            }
        }
    }
    val scrollbarsKnobLayerContentStyle by remember(
        scrollbarsSampleKnobLayerContentStyle,
        scrollbarsKnobLayerContentStyleBorderWidth
    ) {
        derivedStateOf {
            when (scrollbarsSampleKnobLayerContentStyle) {
                SampleDefaultLayerContentStyle.Background -> {
                    ScrollbarsLayerContentStyleType.Background
                }
                SampleDefaultLayerContentStyle.Border -> {
                    ScrollbarsLayerContentStyleType.Border(width = scrollbarsKnobLayerContentStyleBorderWidth)
                }
            }
        }
    }
    val scrollbarsKnobIdleActiveInAnimationSpec: AnimationSpec<Color>? by remember(scrollbarsSampleKnobIdleActiveAnimationSpec) {
        derivedStateOf {
            when (scrollbarsSampleKnobIdleActiveAnimationSpec) {
                SampleIdleActiveAnimationSpec.None -> {
                    null
                }
                SampleIdleActiveAnimationSpec.Spring -> {
                    spring()
                }
                SampleIdleActiveAnimationSpec.Default -> {
                    ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.InAnimationSpec
                }
            }
        }
    }
    val scrollbarsKnobIdleActiveOutAnimationSpec: AnimationSpec<Color>? by remember(scrollbarsSampleKnobIdleActiveAnimationSpec) {
        derivedStateOf {
            when (scrollbarsSampleKnobIdleActiveAnimationSpec) {
                SampleIdleActiveAnimationSpec.None -> {
                    null
                }
                SampleIdleActiveAnimationSpec.Spring -> {
                    spring()
                }
                SampleIdleActiveAnimationSpec.Default -> {
                    ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.OutAnimationSpec
                }
            }
        }
    }
    val scrollbarsKnobLayerContentType by remember(
        scrollbarsSampleKnobLayerContentType,
        scrollbarsKnobLayerContentShape,
        scrollbarsKnobLayerContentStyle,
        scrollbarsKnobLayerContentIdleColor,
        scrollbarsKnobLayerContentActiveColor,
        scrollbarsKnobIdleActiveInAnimationSpec,
        scrollbarsKnobIdleActiveOutAnimationSpec
    ) {
        derivedStateOf {
            when (scrollbarsSampleKnobLayerContentType) {
                SampleLayerContentType.None -> {
                    ScrollbarsLayerContentType.None
                }
                SampleLayerContentType.IdleColored -> {
                    ScrollbarsLayerContentType.Default.Colored.Idle(
                        idleColor = scrollbarsKnobLayerContentIdleColor,
                        shape = scrollbarsKnobLayerContentShape,
                        styleType = scrollbarsKnobLayerContentStyle
                    )
                }
                SampleLayerContentType.IdleActiveColored -> {
                    ScrollbarsLayerContentType.Default.Colored.IdleActive(
                        idleColor = scrollbarsKnobLayerContentIdleColor,
                        shape = scrollbarsKnobLayerContentShape,
                        styleType = scrollbarsKnobLayerContentStyle,
                        activeColor = scrollbarsKnobLayerContentActiveColor,
                        inAnimationSpec = scrollbarsKnobIdleActiveInAnimationSpec,
                        outAnimationSpec = scrollbarsKnobIdleActiveOutAnimationSpec
                    )
                }
            }
        }
    }
    val scrollbarsInDynamicVisibilityAnimationSpec: AnimationSpec<Float>? by remember(scrollbarsDynamicVisibilityAnimationSpec) {
        derivedStateOf {
            when (scrollbarsDynamicVisibilityAnimationSpec) {
                SampleDynamicVisibilityAnimationSpec.None -> {
                    null
                }
                SampleDynamicVisibilityAnimationSpec.Spring -> {
                    spring()
                }
                SampleDynamicVisibilityAnimationSpec.Default -> {
                    ScrollbarsVisibilityTypeDefaults.Dynamic.InAnimationSpec
                }
            }
        }
    }
    val scrollbarsOutDynamicVisibilityAnimationSpec: AnimationSpec<Float>? by remember(scrollbarsDynamicVisibilityAnimationSpec) {
        derivedStateOf {
            when (scrollbarsDynamicVisibilityAnimationSpec) {
                SampleDynamicVisibilityAnimationSpec.None -> {
                    null
                }
                SampleDynamicVisibilityAnimationSpec.Spring -> {
                    spring()
                }
                SampleDynamicVisibilityAnimationSpec.Default -> {
                    ScrollbarsVisibilityTypeDefaults.Dynamic.OutAnimationSpec
                }
            }
        }
    }
    val scrollbarsVisibilityType by remember(
        scrollbarsSampleVisibilityType,
        scrollbarsDynamicVisibilityIsFaded,
        scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible,
        scrollbarsDynamicVisibilityIsVisibleOnTouchDown,
        scrollbarsDynamicVisibilityIsStaticWhenScrollPossible,
        scrollbarsInDynamicVisibilityAnimationSpec,
        scrollbarsOutDynamicVisibilityAnimationSpec,
        scrollbarsScaleVisibilityStartScale
    ) {
        derivedStateOf {
            when (scrollbarsSampleVisibilityType) {
                SampleVisibilityType.Static -> {
                    ScrollbarsVisibilityType.Static
                }
                SampleVisibilityType.Fade -> {
                    ScrollbarsVisibilityType.Dynamic.Fade(
                        inAnimationSpec = scrollbarsInDynamicVisibilityAnimationSpec,
                        outAnimationSpec = scrollbarsOutDynamicVisibilityAnimationSpec,
                        isVisibleWhenScrollNotPossible = scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible,
                        isVisibleOnTouchDown = scrollbarsDynamicVisibilityIsVisibleOnTouchDown,
                        isStaticWhenScrollPossible = scrollbarsDynamicVisibilityIsStaticWhenScrollPossible
                    )
                }
                SampleVisibilityType.Slide -> {
                    ScrollbarsVisibilityType.Dynamic.Slide(
                        inAnimationSpec = scrollbarsInDynamicVisibilityAnimationSpec,
                        outAnimationSpec = scrollbarsOutDynamicVisibilityAnimationSpec,
                        isFaded = scrollbarsDynamicVisibilityIsFaded,
                        isVisibleWhenScrollNotPossible = scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible,
                        isVisibleOnTouchDown = scrollbarsDynamicVisibilityIsVisibleOnTouchDown,
                        isStaticWhenScrollPossible = scrollbarsDynamicVisibilityIsStaticWhenScrollPossible
                    )
                }
                SampleVisibilityType.Scale -> {
                    ScrollbarsVisibilityType.Dynamic.Scale(
                        inAnimationSpec = scrollbarsInDynamicVisibilityAnimationSpec,
                        outAnimationSpec = scrollbarsOutDynamicVisibilityAnimationSpec,
                        isFaded = scrollbarsDynamicVisibilityIsFaded,
                        isVisibleWhenScrollNotPossible = scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible,
                        isVisibleOnTouchDown = scrollbarsDynamicVisibilityIsVisibleOnTouchDown,
                        isStaticWhenScrollPossible = scrollbarsDynamicVisibilityIsStaticWhenScrollPossible,
                        startScale = scrollbarsScaleVisibilityStartScale
                    )
                }
            }
        }
    }

    val scrollbarsConfig by remember(
        scrollbarsOrientation,
        scrollbarsGravity,
        scrollbarsPaddingValues,
        scrollbarsSizeType,
        scrollbarsLayersType,
        scrollbarsBackgroundLayerContentType,
        scrollbarsKnobLayerContentType,
        scrollbarsVisibilityType,
        scrollbarsSampleExampleType,
    ) {
        derivedStateOf {
            when (scrollbarsSampleExampleType) {
                SampleScrollbarsExampleType.Default -> {
                    ScrollbarsConfig(
                        orientation = scrollbarsOrientation,
                        gravity = scrollbarsGravity,
                        paddingValues = scrollbarsPaddingValues,
                        sizeType = scrollbarsSizeType,
                        layersType = scrollbarsLayersType,
                        backgroundLayerContentType = scrollbarsBackgroundLayerContentType,
                        knobLayerContentType = scrollbarsKnobLayerContentType,
                        visibilityType = scrollbarsVisibilityType
                    )
                }
                SampleScrollbarsExampleType.Custom -> {
                    // Provides a custom scrollbars config.
                    ScrollbarsConfig(
                        orientation = ScrollbarsOrientation.Vertical,
                        gravity = ScrollbarsGravity.End,
                        paddingValues = PaddingValues(),
                        sizeType = ScrollbarsSizeType.Full,
                        layersType = ScrollbarsLayersType.Split(
                            backgroundLayerConfig = ScrollbarsLayerConfig(
                                thicknessType = ScrollbarsThicknessType.Exact(thickness = 32.dp),
                                layerGravity = ScrollbarsLayerGravity.End,
                                paddingValues = PaddingValues(vertical = 16.dp)
                            ),
                            knobLayerConfig = ScrollbarsLayerConfig(
                                thicknessType = ScrollbarsThicknessType.Wrap,
                                layerGravity = ScrollbarsLayerGravity.End,
                                paddingValues = PaddingValues(end = 16.dp)
                            )
                        ),
                        backgroundLayerContentType = ScrollbarsLayerContentType.Custom { state ->
                            // Custom interpolated dashes background.
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                repeat(10) {
                                    val markFraction = when (it) {
                                        0 -> {
                                            1.0F - state.scrollFraction.subLerp(
                                                start = 0.0F,
                                                stop = 0.1F
                                            )
                                        }
                                        9 -> {
                                            state.scrollFraction.subLerp(
                                                start = 0.9F,
                                                stop = 1.0F
                                            )
                                        }
                                        else -> {
                                            val centerFraction = state.scrollFraction * 0.9F
                                            val markFractionCurrent = it.toFloat() / 10.0F
                                            val markFractionPrev = markFractionCurrent - 0.1F
                                            val markFractionNext = markFractionCurrent + 0.1F
                                            val prevNextFraction = centerFraction.subLerp(
                                                start = markFractionPrev,
                                                stop = markFractionNext
                                            )

                                            when {
                                                centerFraction in markFractionPrev..markFractionCurrent && prevNextFraction <= 0.5F -> {
                                                    prevNextFraction * 2.0F
                                                }
                                                centerFraction in markFractionCurrent..markFractionNext && prevNextFraction >= 0.5F -> {
                                                    1.0F - ((prevNextFraction - 0.5F) * 2.0F)
                                                }
                                                else -> {
                                                    0.0F
                                                }
                                            }
                                        }
                                    }

                                    val markColor = lerp(
                                        start = Color.LightGray,
                                        stop = Color.Black,
                                        fraction = markFraction
                                    )
                                    val markWidth = lerp(
                                        start = 12.dp,
                                        stop = 28.dp,
                                        fraction = markFraction
                                    )
                                    val markHeight = lerp(
                                        start = 2.dp,
                                        stop = 6.dp,
                                        fraction = markFraction
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(width = markWidth)
                                            .height(height = markHeight)
                                            .background(color = markColor)
                                    )
                                }
                            }
                        },
                        knobLayerContentType = ScrollbarsLayerContentType.Custom { state ->
                            // Custom percentage wrap content text knob.
                            val percentage = state.scrollFraction * 100.0F
                            val percentageText = "${String.format("%.02f", percentage)} %"

                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .align(alignment = Alignment.CenterEnd)
                                    .background(color = Color.Black)
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = percentageText,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        visibilityType = scrollbarsVisibilityType
                    )
                }
            }
        }
    }

    val scrollbarsSampleLazyType by remember(
        itemsRandomHeight,
        scrollbarsSampleScrollType
    ) {
        derivedStateOf {
            if (itemsRandomHeight || scrollbarsSampleScrollType == SampleScrollType.LazyStgrGrid) {
                SampleLazyType.Dynamic
            } else {
                SampleLazyType.Static
            }
        }
    }
    val scrollbarsKnobTypeAnimationSpec: AnimationSpec<Float>? by remember(scrollbarsSampleKnobTypeAnimationSpec) {
        derivedStateOf {
            when (scrollbarsSampleKnobTypeAnimationSpec) {
                SampleKnobTypeAnimationSpec.None -> {
                    null
                }
                SampleKnobTypeAnimationSpec.Spring -> {
                    spring()
                }
                SampleKnobTypeAnimationSpec.Tween_400 -> {
                    tween(durationMillis = 400)
                }
            }
        }
    }
    val scrollbarsStaticKnobType by remember(
        scrollbarsSampleStaticKnobType,
        scrollbarsKnobTypeAnimationSpec,
        scrollbarsFractionKnobSizeFraction,
        scrollbarsExactKnobSize,
        scrollbarsSampleExampleType,
    ) {
        derivedStateOf {
            when (scrollbarsSampleExampleType) {
                SampleScrollbarsExampleType.Default -> {
                    when (scrollbarsSampleStaticKnobType) {
                        SampleStaticKnobType.Auto -> {
                            ScrollbarsStaticKnobType.Auto(animationSpec = scrollbarsKnobTypeAnimationSpec)
                        }
                        SampleStaticKnobType.Fraction -> {
                            ScrollbarsStaticKnobType.Fraction(
                                fraction = scrollbarsFractionKnobSizeFraction,
                                animationSpec = scrollbarsKnobTypeAnimationSpec
                            )
                        }
                        SampleStaticKnobType.Exact -> {
                            ScrollbarsStaticKnobType.Exact(
                                size = scrollbarsExactKnobSize,
                                animationSpec = scrollbarsKnobTypeAnimationSpec
                            )
                        }
                    }
                }
                SampleScrollbarsExampleType.Custom -> {
                    ScrollbarsStaticKnobType.Exact(
                        size = 38.dp,
                        animationSpec = scrollbarsKnobTypeAnimationSpec
                    )
                }
            }
        }
    }
    val scrollbarsDynamicKnobType by remember(
        scrollbarsSampleDynamicKnobType,
        scrollbarsKnobTypeAnimationSpec,
        scrollbarsFractionKnobSizeFraction,
        scrollbarsExactKnobSize,
        scrollbarsDynamicKnobIsSubLerp,
        scrollbarsSampleExampleType
    ) {
        derivedStateOf {
            when (scrollbarsSampleExampleType) {
                SampleScrollbarsExampleType.Default -> {
                    when (scrollbarsSampleDynamicKnobType) {
                        SampleDynamicKnobType.Auto -> {
                            ScrollbarsDynamicKnobType.Auto(animationSpec = scrollbarsKnobTypeAnimationSpec)
                        }
                        SampleDynamicKnobType.Fraction -> {
                            ScrollbarsDynamicKnobType.Fraction(
                                fraction = scrollbarsFractionKnobSizeFraction,
                                animationSpec = scrollbarsKnobTypeAnimationSpec
                            )
                        }
                        SampleDynamicKnobType.Exact -> {
                            ScrollbarsDynamicKnobType.Exact(
                                size = scrollbarsExactKnobSize,
                                animationSpec = scrollbarsKnobTypeAnimationSpec
                            )
                        }
                        SampleDynamicKnobType.Worm -> {
                            ScrollbarsDynamicKnobType.Worm(
                                animationSpec = scrollbarsKnobTypeAnimationSpec,
                                isSubLerp = scrollbarsDynamicKnobIsSubLerp
                            )
                        }
                    }
                }
                SampleScrollbarsExampleType.Custom -> {
                    ScrollbarsDynamicKnobType.Exact(
                        size = 38.dp,
                        animationSpec = scrollbarsKnobTypeAnimationSpec
                    )
                }
            }
        }
    }
    val scrollbarsScrollType by remember(
        scrollbarsSampleScrollType,
        scrollbarsStaticKnobType,
        scrollbarsDynamicKnobType,
        scrollbarsSampleLazyType,
        scrollbarsGridSpanCount
    ) {
        derivedStateOf {
            when (scrollbarsSampleScrollType) {
                SampleScrollType.Scroll -> {
                    ScrollbarsScrollType.Scroll(
                        knobType = scrollbarsStaticKnobType,
                        state = rawScrollState
                    )
                }
                SampleScrollType.LazyList -> {
                    when (scrollbarsSampleLazyType) {
                        SampleLazyType.Static -> {
                            ScrollbarsScrollType.Lazy.List.Static(
                                knobType = scrollbarsStaticKnobType,
                                state = rawLazyListState
                            )
                        }
                        SampleLazyType.Dynamic -> {
                            ScrollbarsScrollType.Lazy.List.Dynamic(
                                knobType = scrollbarsDynamicKnobType,
                                state = rawLazyListState
                            )
                        }
                    }
                }
                SampleScrollType.LazyGrid -> {
                    when (scrollbarsSampleLazyType) {
                        SampleLazyType.Static -> {
                            ScrollbarsScrollType.Lazy.Grid.Static(
                                knobType = scrollbarsStaticKnobType,
                                state = rawLazyGridState,
                                spanCount = scrollbarsGridSpanCount
                            )
                        }
                        SampleLazyType.Dynamic -> {
                            ScrollbarsScrollType.Lazy.Grid.Dynamic(
                                knobType = scrollbarsDynamicKnobType,
                                state = rawLazyGridState,
                                spanCount = scrollbarsGridSpanCount
                            )
                        }
                    }
                }
                SampleScrollType.LazyStgrGrid -> {
                    ScrollbarsScrollType.Lazy.StaggeredGrid(
                        knobType = scrollbarsDynamicKnobType,
                        state = rawLazyStaggeredGridState,
                        spanCount = scrollbarsGridSpanCount
                    )
                }
            }
        }
    }

    val scrollbarsState = rememberScrollbarsState(
        config = scrollbarsConfig,
        scrollType = scrollbarsScrollType
    )

    fun resetScrolls() {
        coroutineScope.launch {
            rawScrollState.scrollTo(0)
        }
        coroutineScope.launch {
            rawLazyListState.scrollToItem(0)
        }
        coroutineScope.launch {
            rawLazyGridState.scrollToItem(0)
        }
        coroutineScope.launch {
            rawLazyStaggeredGridState.scrollToItem(0)
        }
        coroutineScope.launch {
            configScrollState.scrollTo(0)
        }
    }

    fun reset() {
        scrollbarsSampleScrollType = SampleScrollType.Scroll

        itemsCount = 10
        gridItemsCount = 20
        itemAdditionalSize = 0.dp
        contentSpacing = 20.dp
        itemsSpacing = 0.dp
        itemsRandomHeight = false
        isIdleColorPickerVisible = false
        isActiveColorPickerVisible = false

        scrollbarsSampleGridSpanCount = SampleGridSpanCount.Span_2
        scrollbarsOrientation = ScrollbarsOrientation.Vertical
        scrollbarsSampleExampleType = SampleScrollbarsExampleType.Default
        scrollbarsGravity = ScrollbarsConfigDefaults.Gravity
        scrollbarsHorizontalPaddingValue = ScrollbarsConfigDefaults.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        scrollbarsVerticalPaddingValue = ScrollbarsConfigDefaults.PaddingValues.calculateTopPadding()
        scrollbarsSampleSizeType = SampleSizeType.Full
        scrollbarsSizeFraction = ScrollbarsSizeTypeDefaults.Fraction.Fraction
        scrollbarsSizeExact = ScrollbarsSizeTypeDefaults.Exact.Size
        scrollbarsSampleLayersType = SampleLayersType.Wrap
        scrollbarsWrapThickness = ScrollbarsThicknessTypeDefaults.Exact.Thickness
        scrollbarsWrapHorizontalPaddingValue = ScrollbarsLayersTypeDefaults.Wrap.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        scrollbarsWrapVerticalPaddingValue = ScrollbarsLayersTypeDefaults.Wrap.PaddingValues.calculateTopPadding()
        scrollbarsSplitConfigureLayerType = SampleConfigureLayerType.Knob
        scrollbarsBackgroundLayerGravity = ScrollbarsLayerConfigDefaults.Gravity
        scrollbarsBackgroundLayerThickness = ScrollbarsThicknessTypeDefaults.Exact.Thickness
        scrollbarsBackgroundLayerHorizontalPaddingValue = ScrollbarsLayerConfigDefaults.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        scrollbarsBackgroundLayerVerticalPaddingValue = ScrollbarsLayerConfigDefaults.PaddingValues.calculateTopPadding()
        scrollbarsKnobLayerGravity = ScrollbarsLayerConfigDefaults.Gravity
        scrollbarsKnobLayerThickness = ScrollbarsThicknessTypeDefaults.Exact.Thickness
        scrollbarsKnobLayerHorizontalPaddingValue = ScrollbarsLayerConfigDefaults.PaddingValues.calculateStartPadding(LayoutDirection.Ltr)
        scrollbarsKnobLayerVerticalPaddingValue = ScrollbarsLayerConfigDefaults.PaddingValues.calculateTopPadding()

        scrollbarsContentConfigureLayerType = SampleConfigureLayerType.Knob
        scrollbarsSampleBackgroundLayerContentType = SampleLayerContentType.None
        scrollbarsSampleBackgroundLayerContentShape = SampleDefaultLayerContentShape.Rounded
        scrollbarsSampleBackgroundLayerContentStyle = SampleDefaultLayerContentStyle.Background
        scrollbarsBackgroundLayerContentStyleBorderWidth = ScrollbarsLayerContentStyleTypeDefaults.Border.Width
        scrollbarsBackgroundLayerContentIdleColor = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleColor
        scrollbarsBackgroundLayerContentActiveColor = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.ActiveColor
        scrollbarsSampleBackgroundIdleActiveAnimationSpec = SampleIdleActiveAnimationSpec.Default

        scrollbarsSampleKnobLayerContentType = SampleLayerContentType.IdleActiveColored
        scrollbarsSampleKnobLayerContentShape = SampleDefaultLayerContentShape.Rounded
        scrollbarsSampleKnobLayerContentStyle = SampleDefaultLayerContentStyle.Background
        scrollbarsKnobLayerContentStyleBorderWidth = ScrollbarsLayerContentStyleTypeDefaults.Border.Width
        scrollbarsKnobLayerContentIdleColor = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleColor
        scrollbarsKnobLayerContentActiveColor = ScrollbarsLayerContentTypeDefaults.Default.Colored.IdleActive.ActiveColor
        scrollbarsSampleKnobIdleActiveAnimationSpec = SampleIdleActiveAnimationSpec.Default

        scrollbarsSampleVisibilityType = SampleVisibilityType.Fade
        scrollbarsDynamicVisibilityAnimationSpec = SampleDynamicVisibilityAnimationSpec.Default
        scrollbarsDynamicVisibilityIsFaded = ScrollbarsVisibilityTypeDefaults.Dynamic.IsFaded
        scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleWhenScrollNotPossible
        scrollbarsDynamicVisibilityIsVisibleOnTouchDown = ScrollbarsVisibilityTypeDefaults.Dynamic.IsVisibleOnTouchDown
        scrollbarsDynamicVisibilityIsStaticWhenScrollPossible = ScrollbarsVisibilityTypeDefaults.Dynamic.IsStaticWhenScrollPossible
        scrollbarsScaleVisibilityStartScale = ScrollbarsVisibilityTypeDefaults.Dynamic.Scale.StartScale

        scrollbarsSampleStaticKnobType = SampleStaticKnobType.Auto
        scrollbarsSampleDynamicKnobType = SampleDynamicKnobType.Auto
        scrollbarsSampleKnobTypeAnimationSpec = SampleKnobTypeAnimationSpec.Spring
        scrollbarsFractionKnobSizeFraction = ScrollbarsKnobTypeDefaults.Fraction.Fraction
        scrollbarsExactKnobSize = ScrollbarsKnobTypeDefaults.Exact.Size
        scrollbarsDynamicKnobIsSubLerp = ScrollbarsKnobTypeDefaults.Worm.IsSubLerp

        coroutineScope.launch {
            resetScrolls()
        }
    }

    LaunchedEffect(scrollbarsSampleScrollType) {
        resetScrolls()
    }
    LaunchedEffect(scrollbarsSampleExampleType) {
        if (scrollbarsSampleExampleType == SampleScrollbarsExampleType.Custom) {
            scrollbarsOrientation = ScrollbarsOrientation.Vertical
        }
    }

    @Composable
    fun SampleItem(
        index: Int,
        orientation: ScrollbarsOrientation
    ) {
        val rawItemSize = if (itemsRandomHeight) {
            val random = Random(index + randomSeed)

            if (random.nextFloat() >= 0.1F) {
                65.dp + 200.dp * random.nextFloat()
            } else {
                400.dp
            }
        } else {
            65.dp
        }
        val itemSize = rawItemSize + itemAdditionalSize

        Box(
            modifier = Modifier
                .background(color = sampleBgColors[index % sampleBgColors.size])
                .then(
                    when (orientation) {
                        ScrollbarsOrientation.Vertical -> {
                            Modifier
                                .fillMaxWidth()
                                .requiredSizeIn(minHeight = 10.dp)
                                .height(height = itemSize)
                                .padding(horizontal = 20.dp)
                        }
                        ScrollbarsOrientation.Horizontal -> {
                            Modifier
                                .fillMaxHeight()
                                .requiredSizeIn(minWidth = 10.dp)
                                .width(width = itemSize)
                                .padding(vertical = 20.dp)
                        }
                    }
                ),
            contentAlignment = when (orientation) {
                ScrollbarsOrientation.Vertical -> {
                    Alignment.CenterStart
                }
                ScrollbarsOrientation.Horizontal -> {
                    Alignment.Center
                }
            }

        ) {
            val text = (index + 1).toString()

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Crossfade(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 0.95F)
                .clipToBounds(),
            targetState = scrollbarsSampleScrollType to scrollbarsOrientation,
            label = "ScrollbarsCrossfade"
        ) { (scrollbarsSampleContentTypeState, scrollbarsOrientationState) ->
            when (scrollbarsSampleContentTypeState) {
                SampleScrollType.Scroll -> {
                    when (scrollbarsOrientationState) {
                        ScrollbarsOrientation.Vertical -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(state = rawScrollState)
                                        .padding(
                                            horizontal = 20.dp,
                                            vertical = contentSpacing
                                        ),
                                    verticalArrangement = Arrangement.spacedBy(space = itemsSpacing)
                                ) {
                                    repeat(itemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                        ScrollbarsOrientation.Horizontal -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .horizontalScroll(state = rawScrollState)
                                        .padding(
                                            vertical = 20.dp,
                                            horizontal = contentSpacing
                                        ),
                                    horizontalArrangement = Arrangement.spacedBy(space = itemsSpacing)
                                ) {
                                    repeat(itemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                    }
                }
                SampleScrollType.LazyList -> {
                    when (scrollbarsOrientationState) {
                        ScrollbarsOrientation.Vertical -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    state = rawLazyListState,
                                    verticalArrangement = Arrangement.spacedBy(space = itemsSpacing),
                                    contentPadding = PaddingValues(
                                        horizontal = 20.dp,
                                        vertical = contentSpacing
                                    )
                                ) {
                                    items(itemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                        ScrollbarsOrientation.Horizontal -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyRow(
                                    modifier = Modifier.fillMaxSize(),
                                    state = rawLazyListState,
                                    horizontalArrangement = Arrangement.spacedBy(space = itemsSpacing),
                                    contentPadding = PaddingValues(
                                        horizontal = contentSpacing,
                                        vertical = 20.dp
                                    )
                                ) {
                                    items(itemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                    }
                }
                SampleScrollType.LazyGrid -> {
                    when (scrollbarsOrientationState) {
                        ScrollbarsOrientation.Vertical -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    state = rawLazyGridState,
                                    verticalArrangement = Arrangement.spacedBy(space = itemsSpacing),
                                    contentPadding = PaddingValues(
                                        horizontal = 20.dp,
                                        vertical = contentSpacing
                                    ),
                                    columns = GridCells.Fixed(count = scrollbarsGridSpanCount)
                                ) {
                                    items(gridItemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                        ScrollbarsOrientation.Horizontal -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyHorizontalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    state = rawLazyGridState,
                                    horizontalArrangement = Arrangement.spacedBy(space = itemsSpacing),
                                    contentPadding = PaddingValues(
                                        horizontal = contentSpacing,
                                        vertical = 20.dp
                                    ),
                                    rows = GridCells.Fixed(count = scrollbarsGridSpanCount)
                                ) {
                                    items(gridItemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                    }
                }
                SampleScrollType.LazyStgrGrid -> {
                    when (scrollbarsOrientationState) {
                        ScrollbarsOrientation.Vertical -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyVerticalStaggeredGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    state = rawLazyStaggeredGridState,
                                    verticalItemSpacing = itemsSpacing,
                                    contentPadding = PaddingValues(
                                        horizontal = 20.dp,
                                        vertical = contentSpacing
                                    ),
                                    columns = StaggeredGridCells.Fixed(count = scrollbarsGridSpanCount)
                                ) {
                                    items(gridItemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                        ScrollbarsOrientation.Horizontal -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyHorizontalStaggeredGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    state = rawLazyStaggeredGridState,
                                    horizontalItemSpacing = itemsSpacing,
                                    contentPadding = PaddingValues(
                                        horizontal = contentSpacing,
                                        vertical = 20.dp
                                    ),
                                    rows = StaggeredGridCells.Fixed(count = scrollbarsGridSpanCount)
                                ) {
                                    items(gridItemsCount) {
                                        SampleItem(
                                            index = it,
                                            orientation = scrollbarsOrientationState
                                        )
                                    }
                                }
                                Scrollbars(state = scrollbarsState)
                            }
                        }
                    }
                }
            }
        }
        Divider()
        TabRow(
            selectedTabIndex = scrollbarsSampleScrollType.ordinal,
            modifier = Modifier.fillMaxWidth(),
            tabs = {
                SampleScrollType.values().forEach { enumValue ->
                    Tab(
                        selected = enumValue == scrollbarsSampleScrollType,
                        text = {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = enumValue.name.uppercase(),
                                maxLines = 1
                            )
                        },
                        onClick = {
                            scrollbarsSampleScrollType = enumValue
                        },
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = configScrollState)
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            if (scrollbarsSampleScrollType.isLazyGridOrStgrGrid) {
                Text(
                    text = "SPAN COUNT:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = scrollbarsSampleGridSpanCount.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        SampleGridSpanCount.values().forEach { enumValue ->
                            Tab(
                                selected = enumValue == scrollbarsSampleGridSpanCount,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    scrollbarsSampleGridSpanCount = enumValue
                                },
                            )
                        }
                    }
                )
            }

            val endValueRange = if (scrollbarsSampleScrollType.isLazyGridOrStgrGrid) {
                40
            } else {
                20
            }

            Text(
                modifier = Modifier.padding(
                    top = if (scrollbarsSampleScrollType.isLazyGridOrStgrGrid) {
                        20.dp
                    } else {
                        0.dp
                    }
                ),
                text = "ITEMS COUNT:",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = if (scrollbarsSampleScrollType.isLazyGridOrStgrGrid) {
                    gridItemsCount.toFloat()
                } else {
                    itemsCount.toFloat()
                },
                onValueChange = {
                    if (scrollbarsSampleScrollType.isLazyGridOrStgrGrid) {
                        gridItemsCount = it.toInt()
                    } else {
                        itemsCount = it.toInt()
                    }
                },
                valueRange = 0.0F..endValueRange.toFloat(),
                steps = endValueRange
            )

            Text(
                text = "ITEM ADDITIONAL SIZE:",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = itemAdditionalSize.value,
                onValueChange = {
                    itemAdditionalSize = it.dp
                },
                valueRange = -40.0F..40.0F,
                steps = 50
            )

            Text(
                text = "CONTENT SPACING:",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = contentSpacing.value,
                onValueChange = {
                    contentSpacing = it.dp
                },
                valueRange = 0F..25.0F,
                steps = 25
            )

            Text(
                text = "ITEMS SPACING:",
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = itemsSpacing.value,
                onValueChange = {
                    itemsSpacing = it.dp
                },
                valueRange = 0F..25.0F,
                steps = 25
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(weight = 1.0F)
                        .basicMarquee(),
                    text = "ITEMS RANDOM HEIGHT:",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
                Switch(
                    checked = itemsRandomHeight,
                    onCheckedChange = {
                        itemsRandomHeight = it
                        if (itemsRandomHeight) {
                            randomSeed = Random.nextInt()
                        }
                    }
                )
            }

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "SAMPLE EXAMPLE TYPE:",
                style = MaterialTheme.typography.labelLarge
            )
            TabRow(
                selectedTabIndex = scrollbarsSampleExampleType.ordinal,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    SampleScrollbarsExampleType.values().forEach { enumValue ->
                        Tab(
                            selected = enumValue == scrollbarsSampleExampleType,
                            text = {
                                Text(
                                    modifier = Modifier.basicMarquee(),
                                    text = enumValue.name.uppercase(),
                                    maxLines = 1
                                )
                            },
                            onClick = {
                                scrollbarsSampleExampleType = enumValue
                            },
                        )
                    }
                }
            )

            var isBorder = false
            var isIdleActive = false

            if (scrollbarsSampleExampleType == SampleScrollbarsExampleType.Default) {
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "ORIENTATION:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = scrollbarsOrientation.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        ScrollbarsOrientation.values().forEach { enumValue ->
                            Tab(
                                selected = enumValue == scrollbarsOrientation,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    scrollbarsOrientation = enumValue
                                },
                            )
                        }
                    }
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "GRAVITY:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = scrollbarsGravity.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        ScrollbarsGravity.values().forEach { enumValue ->
                            Tab(
                                selected = enumValue == scrollbarsGravity,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    scrollbarsGravity = enumValue
                                },
                            )
                        }
                    }
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "HORIZONTAL PADDING VALUE:",
                    style = MaterialTheme.typography.labelLarge
                )
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = scrollbarsHorizontalPaddingValue.value,
                    onValueChange = {
                        scrollbarsHorizontalPaddingValue = it.dp
                    },
                    valueRange = 0.0F..20.0F,
                    steps = 20
                )

                Text(
                    text = "VERTICAL PADDING VALUE:",
                    style = MaterialTheme.typography.labelLarge
                )
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = scrollbarsVerticalPaddingValue.value,
                    onValueChange = {
                        scrollbarsVerticalPaddingValue = it.dp
                    },
                    valueRange = 0.0F..20.0F,
                    steps = 20
                )

                Text(
                    text = "SIZE TYPE:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = scrollbarsSampleSizeType.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        SampleSizeType.values().forEach { enumValue ->
                            Tab(
                                selected = enumValue == scrollbarsSampleSizeType,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    scrollbarsSampleSizeType = enumValue
                                },
                            )
                        }
                    }
                )

                if (scrollbarsSampleSizeType == SampleSizeType.Fraction) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "SIZE FRACTION:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = scrollbarsSizeFraction,
                        onValueChange = {
                            scrollbarsSizeFraction = it
                        },
                        valueRange = 0.25F..1.0F,
                        steps = 7
                    )
                } else if (scrollbarsSampleSizeType == SampleSizeType.Exact) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "SIZE EXACT:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = scrollbarsSizeExact.value,
                        onValueChange = {
                            scrollbarsSizeExact = it.dp
                        },
                        valueRange = 160F..400.0F,
                        steps = 7
                    )
                }

                Text(
                    modifier = Modifier.padding(
                        top = when (scrollbarsSampleSizeType) {
                            SampleSizeType.Full -> 20.dp
                            else -> 12.dp
                        }
                    ),
                    text = "LAYERS TYPE:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = scrollbarsSampleLayersType.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        SampleLayersType.values().forEach { enumValue ->
                            Tab(
                                selected = enumValue == scrollbarsSampleLayersType,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    scrollbarsSampleLayersType = enumValue
                                },
                            )
                        }
                    }
                )

                when (scrollbarsSampleLayersType) {
                    SampleLayersType.Wrap -> {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "WRAP THICKNESS:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = scrollbarsWrapThickness.value,
                            onValueChange = {
                                scrollbarsWrapThickness = it.dp
                            },
                            valueRange = 1.0F..20.0F,
                            steps = 19
                        )

                        Text(
                            text = "WRAP HORIZONTAL PADDING VALUE:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = scrollbarsWrapHorizontalPaddingValue.value,
                            onValueChange = {
                                scrollbarsWrapHorizontalPaddingValue = it.dp
                            },
                            valueRange = 0.0F..20.0F,
                            steps = 20
                        )

                        Text(
                            text = "WRAP VERTICAL PADDING VALUE:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = scrollbarsWrapVerticalPaddingValue.value,
                            onValueChange = {
                                scrollbarsWrapVerticalPaddingValue = it.dp
                            },
                            valueRange = 0.0F..20.0F,
                            steps = 20
                        )
                    }
                    SampleLayersType.Split -> {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "SPLIT LAYER CONFIGURE:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        TabRow(
                            selectedTabIndex = scrollbarsSplitConfigureLayerType.ordinal,
                            modifier = Modifier.fillMaxWidth(),
                            tabs = {
                                SampleConfigureLayerType.values().forEach { enumValue ->
                                    Tab(
                                        selected = enumValue == scrollbarsSplitConfigureLayerType,
                                        text = {
                                            Text(
                                                modifier = Modifier.basicMarquee(),
                                                text = enumValue.name.uppercase(),
                                                maxLines = 1
                                            )
                                        },
                                        onClick = {
                                            scrollbarsSplitConfigureLayerType = enumValue
                                        },
                                    )
                                }
                            }
                        )

                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "SPLIT GRAVITY:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        TabRow(
                            selectedTabIndex = when (scrollbarsSplitConfigureLayerType) {
                                SampleConfigureLayerType.Background -> {
                                    scrollbarsBackgroundLayerGravity.ordinal
                                }
                                SampleConfigureLayerType.Knob -> {
                                    scrollbarsKnobLayerGravity.ordinal
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            tabs = {
                                ScrollbarsLayerGravity.values().forEach { enumValue ->
                                    Tab(
                                        selected = when (scrollbarsSplitConfigureLayerType) {
                                            SampleConfigureLayerType.Background -> {
                                                enumValue == scrollbarsBackgroundLayerGravity
                                            }
                                            SampleConfigureLayerType.Knob -> {
                                                enumValue == scrollbarsKnobLayerGravity
                                            }
                                        },
                                        text = {
                                            Text(
                                                modifier = Modifier.basicMarquee(),
                                                text = enumValue.name.uppercase(),
                                                maxLines = 1
                                            )
                                        },
                                        onClick = {
                                            when (scrollbarsSplitConfigureLayerType) {
                                                SampleConfigureLayerType.Background -> {
                                                    scrollbarsBackgroundLayerGravity = enumValue
                                                }
                                                SampleConfigureLayerType.Knob -> {
                                                    scrollbarsKnobLayerGravity = enumValue
                                                }
                                            }
                                        },
                                    )
                                }
                            }
                        )

                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "SPLIT THICKNESS:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = when (scrollbarsSplitConfigureLayerType) {
                                SampleConfigureLayerType.Background -> {
                                    scrollbarsBackgroundLayerThickness.value
                                }
                                SampleConfigureLayerType.Knob -> {
                                    scrollbarsKnobLayerThickness.value
                                }
                            },
                            onValueChange = {
                                when (scrollbarsSplitConfigureLayerType) {
                                    SampleConfigureLayerType.Background -> {
                                        scrollbarsBackgroundLayerThickness = it.dp
                                    }
                                    SampleConfigureLayerType.Knob -> {
                                        scrollbarsKnobLayerThickness = it.dp
                                    }
                                }
                            },
                            valueRange = 1.0F..20.0F,
                            steps = 19
                        )

                        Text(
                            text = "SPLIT HORIZONTAL PADDING VALUE:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = when (scrollbarsSplitConfigureLayerType) {
                                SampleConfigureLayerType.Background -> {
                                    scrollbarsBackgroundLayerHorizontalPaddingValue.value
                                }
                                SampleConfigureLayerType.Knob -> {
                                    scrollbarsKnobLayerHorizontalPaddingValue.value
                                }
                            },
                            onValueChange = {
                                when (scrollbarsSplitConfigureLayerType) {
                                    SampleConfigureLayerType.Background -> {
                                        scrollbarsBackgroundLayerHorizontalPaddingValue = it.dp
                                    }
                                    SampleConfigureLayerType.Knob -> {
                                        scrollbarsKnobLayerHorizontalPaddingValue = it.dp
                                    }
                                }
                            },
                            valueRange = 0.0F..20.0F,
                            steps = 20
                        )

                        Text(
                            text = "SPLIT VERTICAL PADDING VALUE:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = when (scrollbarsSplitConfigureLayerType) {
                                SampleConfigureLayerType.Background -> {
                                    scrollbarsBackgroundLayerVerticalPaddingValue.value
                                }
                                SampleConfigureLayerType.Knob -> {
                                    scrollbarsKnobLayerVerticalPaddingValue.value
                                }
                            },
                            onValueChange = {
                                when (scrollbarsSplitConfigureLayerType) {
                                    SampleConfigureLayerType.Background -> {
                                        scrollbarsBackgroundLayerVerticalPaddingValue = it.dp
                                    }
                                    SampleConfigureLayerType.Knob -> {
                                        scrollbarsKnobLayerVerticalPaddingValue = it.dp
                                    }
                                }
                            },
                            valueRange = 0.0F..20.0F,
                            steps = 20
                        )
                    }
                }

                Text(
                    text = "LAYER CONTENT CONFIGURE:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = scrollbarsContentConfigureLayerType.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        SampleConfigureLayerType.values().forEach { enumValue ->
                            Tab(
                                selected = enumValue == scrollbarsContentConfigureLayerType,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    scrollbarsContentConfigureLayerType = enumValue
                                },
                            )
                        }
                    }
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "LAYER CONTENT TYPE:",
                    style = MaterialTheme.typography.labelLarge
                )
                TabRow(
                    selectedTabIndex = when (scrollbarsContentConfigureLayerType) {
                        SampleConfigureLayerType.Background -> {
                            scrollbarsSampleBackgroundLayerContentType.ordinal
                        }
                        SampleConfigureLayerType.Knob -> {
                            scrollbarsSampleKnobLayerContentType.ordinal
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    tabs = {
                        SampleLayerContentType.values().forEach { enumValue ->
                            Tab(
                                selected = when (scrollbarsContentConfigureLayerType) {
                                    SampleConfigureLayerType.Background -> {
                                        scrollbarsSampleBackgroundLayerContentType == enumValue
                                    }
                                    SampleConfigureLayerType.Knob -> {
                                        scrollbarsSampleKnobLayerContentType == enumValue
                                    }
                                },
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = enumValue.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = {
                                    when (scrollbarsContentConfigureLayerType) {
                                        SampleConfigureLayerType.Background -> {
                                            scrollbarsSampleBackgroundLayerContentType = enumValue
                                        }
                                        SampleConfigureLayerType.Knob -> {
                                            scrollbarsSampleKnobLayerContentType = enumValue
                                        }
                                    }
                                },
                            )
                        }
                    }
                )

                if ((scrollbarsContentConfigureLayerType == SampleConfigureLayerType.Background &&
                            scrollbarsSampleBackgroundLayerContentType.isDefault) ||
                    (scrollbarsContentConfigureLayerType == SampleConfigureLayerType.Knob &&
                            scrollbarsSampleKnobLayerContentType.isDefault)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "IDLE COLOR:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(intrinsicSize = IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(space = 20.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(weight = 1.0F),
                            onClick = {
                                isActiveColorPickerVisible = false
                                isIdleColorPickerVisible = true
                            }
                        ) {
                            Text(text = "PICK COLOR")
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(ratio = 1.0F)
                                .background(
                                    color = when (scrollbarsContentConfigureLayerType) {
                                        SampleConfigureLayerType.Background -> {
                                            scrollbarsBackgroundLayerContentIdleColor
                                        }
                                        SampleConfigureLayerType.Knob -> {
                                            scrollbarsKnobLayerContentIdleColor
                                        }
                                    },
                                    shape = RoundedCornerShape(size = 4.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(size = 4.dp)
                                )
                                .clickable {
                                    isActiveColorPickerVisible = false
                                    isIdleColorPickerVisible = true
                                }
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "SHAPE:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    TabRow(
                        selectedTabIndex = when (scrollbarsContentConfigureLayerType) {
                            SampleConfigureLayerType.Background -> {
                                scrollbarsSampleBackgroundLayerContentShape.ordinal
                            }
                            SampleConfigureLayerType.Knob -> {
                                scrollbarsSampleKnobLayerContentShape.ordinal
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        tabs = {
                            SampleDefaultLayerContentShape.values().forEach { enumValue ->
                                Tab(
                                    selected = when (scrollbarsContentConfigureLayerType) {
                                        SampleConfigureLayerType.Background -> {
                                            scrollbarsSampleBackgroundLayerContentShape == enumValue
                                        }
                                        SampleConfigureLayerType.Knob -> {
                                            scrollbarsSampleKnobLayerContentShape == enumValue
                                        }
                                    },
                                    text = {
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = enumValue.name.uppercase(),
                                            maxLines = 1
                                        )
                                    },
                                    onClick = {
                                        when (scrollbarsContentConfigureLayerType) {
                                            SampleConfigureLayerType.Background -> {
                                                scrollbarsSampleBackgroundLayerContentShape = enumValue
                                            }
                                            SampleConfigureLayerType.Knob -> {
                                                scrollbarsSampleKnobLayerContentShape = enumValue
                                            }
                                        }
                                    },
                                )
                            }
                        }
                    )
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "STYLE:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    TabRow(
                        selectedTabIndex = when (scrollbarsContentConfigureLayerType) {
                            SampleConfigureLayerType.Background -> {
                                scrollbarsSampleBackgroundLayerContentStyle.ordinal
                            }
                            SampleConfigureLayerType.Knob -> {
                                scrollbarsSampleKnobLayerContentStyle.ordinal
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        tabs = {
                            SampleDefaultLayerContentStyle.values().forEach { enumValue ->
                                Tab(
                                    selected = when (scrollbarsContentConfigureLayerType) {
                                        SampleConfigureLayerType.Background -> {
                                            scrollbarsSampleBackgroundLayerContentStyle == enumValue
                                        }
                                        SampleConfigureLayerType.Knob -> {
                                            scrollbarsSampleKnobLayerContentStyle == enumValue
                                        }
                                    },
                                    text = {
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = enumValue.name.uppercase(),
                                            maxLines = 1
                                        )
                                    },
                                    onClick = {
                                        when (scrollbarsContentConfigureLayerType) {
                                            SampleConfigureLayerType.Background -> {
                                                scrollbarsSampleBackgroundLayerContentStyle = enumValue
                                            }
                                            SampleConfigureLayerType.Knob -> {
                                                scrollbarsSampleKnobLayerContentStyle = enumValue
                                            }
                                        }
                                    },
                                )
                            }
                        }
                    )

                    if ((scrollbarsContentConfigureLayerType == SampleConfigureLayerType.Background &&
                                scrollbarsSampleBackgroundLayerContentStyle == SampleDefaultLayerContentStyle.Border) ||
                        (scrollbarsContentConfigureLayerType == SampleConfigureLayerType.Knob &&
                                scrollbarsSampleKnobLayerContentStyle == SampleDefaultLayerContentStyle.Border)
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 20.dp),
                            text = "BORDER WIDTH:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = when (scrollbarsContentConfigureLayerType) {
                                SampleConfigureLayerType.Background -> {
                                    scrollbarsBackgroundLayerContentStyleBorderWidth.value
                                }
                                SampleConfigureLayerType.Knob -> {
                                    scrollbarsKnobLayerContentStyleBorderWidth.value
                                }
                            },
                            onValueChange = {
                                when (scrollbarsContentConfigureLayerType) {
                                    SampleConfigureLayerType.Background -> {
                                        scrollbarsBackgroundLayerContentStyleBorderWidth = it.dp
                                    }
                                    SampleConfigureLayerType.Knob -> {
                                        scrollbarsKnobLayerContentStyleBorderWidth = it.dp
                                    }
                                }
                            },
                            valueRange = 0.5F..3.0F,
                            steps = 7
                        )

                        isBorder = true
                    }

                    if ((scrollbarsContentConfigureLayerType == SampleConfigureLayerType.Background &&
                                scrollbarsSampleBackgroundLayerContentType == SampleLayerContentType.IdleActiveColored) ||
                        (scrollbarsContentConfigureLayerType == SampleConfigureLayerType.Knob &&
                                scrollbarsSampleKnobLayerContentType == SampleLayerContentType.IdleActiveColored)
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                top = if (isBorder) {
                                    12.dp
                                } else {
                                    20.dp
                                }
                            ),
                            text = "IDLE ACTIVE COLOR:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(intrinsicSize = IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(space = 20.dp)
                        ) {
                            Button(
                                modifier = Modifier.weight(weight = 1.0F),
                                onClick = {
                                    isIdleColorPickerVisible = false
                                    isActiveColorPickerVisible = true
                                }
                            ) {
                                Text(text = "PICK COLOR")
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(ratio = 1.0F)
                                    .background(
                                        color = when (scrollbarsContentConfigureLayerType) {
                                            SampleConfigureLayerType.Background -> {
                                                scrollbarsBackgroundLayerContentActiveColor
                                            }
                                            SampleConfigureLayerType.Knob -> {
                                                scrollbarsKnobLayerContentActiveColor
                                            }
                                        },
                                        shape = RoundedCornerShape(size = 4.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(size = 4.dp)
                                    )
                                    .clickable {
                                        isIdleColorPickerVisible = false
                                        isActiveColorPickerVisible = true
                                    }
                            )
                        }

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "ACTIVE COLOR ANIMATION SPEC:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        TabRow(
                            selectedTabIndex = when (scrollbarsContentConfigureLayerType) {
                                SampleConfigureLayerType.Background -> {
                                    scrollbarsSampleBackgroundIdleActiveAnimationSpec.ordinal
                                }
                                SampleConfigureLayerType.Knob -> {
                                    scrollbarsSampleKnobIdleActiveAnimationSpec.ordinal
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            tabs = {
                                SampleIdleActiveAnimationSpec.values().forEach { enumValue ->
                                    Tab(
                                        selected = when (scrollbarsContentConfigureLayerType) {
                                            SampleConfigureLayerType.Background -> {
                                                scrollbarsSampleBackgroundIdleActiveAnimationSpec == enumValue
                                            }
                                            SampleConfigureLayerType.Knob -> {
                                                scrollbarsSampleKnobIdleActiveAnimationSpec == enumValue
                                            }
                                        },
                                        text = {
                                            Text(
                                                modifier = Modifier.basicMarquee(),
                                                text = enumValue.name.uppercase(),
                                                maxLines = 1
                                            )
                                        },
                                        onClick = {
                                            when (scrollbarsContentConfigureLayerType) {
                                                SampleConfigureLayerType.Background -> {
                                                    scrollbarsSampleBackgroundIdleActiveAnimationSpec = enumValue
                                                }
                                                SampleConfigureLayerType.Knob -> {
                                                    scrollbarsSampleKnobIdleActiveAnimationSpec = enumValue
                                                }
                                            }
                                        },
                                    )
                                }
                            }
                        )

                        isIdleActive = true
                    }
                }
            }

            Text(
                modifier = Modifier.padding(
                    top = if (isBorder && !isIdleActive) {
                        12.dp
                    } else {
                        20.dp
                    }
                ),
                text = "VISIBILITY TYPE:",
                style = MaterialTheme.typography.labelLarge
            )
            TabRow(
                selectedTabIndex = scrollbarsSampleVisibilityType.ordinal,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    SampleVisibilityType.values().forEach { enumValue ->
                        Tab(
                            selected = enumValue == scrollbarsSampleVisibilityType,
                            text = {
                                Text(
                                    modifier = Modifier.basicMarquee(),
                                    text = enumValue.name.uppercase(),
                                    maxLines = 1
                                )
                            },
                            onClick = {
                                scrollbarsSampleVisibilityType = enumValue
                            },
                        )
                    }
                }
            )

            when (scrollbarsSampleVisibilityType) {
                SampleVisibilityType.Static -> {
                    /* Do nothing. */
                }
                SampleVisibilityType.Fade,
                SampleVisibilityType.Slide,
                SampleVisibilityType.Scale -> {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "DYNAMIC VISIBILITY ANIMATION SPEC:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    TabRow(
                        selectedTabIndex = scrollbarsDynamicVisibilityAnimationSpec.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                        tabs = {
                            SampleDynamicVisibilityAnimationSpec.values().forEach { enumValue ->
                                Tab(
                                    selected = enumValue == scrollbarsDynamicVisibilityAnimationSpec,
                                    text = {
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = enumValue.name.uppercase(),
                                            maxLines = 1
                                        )
                                    },
                                    onClick = {
                                        scrollbarsDynamicVisibilityAnimationSpec = enumValue
                                    },
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(height = 12.dp))

                    if (scrollbarsSampleVisibilityType != SampleVisibilityType.Fade) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(weight = 1.0F),
                                text = "IS FADED:",
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1
                            )
                            Switch(
                                checked = scrollbarsDynamicVisibilityIsFaded,
                                onCheckedChange = {
                                    scrollbarsDynamicVisibilityIsFaded = it
                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(weight = 1.0F)
                                .basicMarquee(),
                            text = "IS VISIBLE WHEN SCROLL NOT POSSIBLE:",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
                        Switch(
                            checked = scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible,
                            onCheckedChange = {
                                scrollbarsDynamicVisibilityIsVisibleWhenScrollNotPossible = it
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(weight = 1.0F)
                                .basicMarquee(),
                            text = "IS VISIBLE ON TOUCH DOWN:",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
                        Switch(
                            checked = scrollbarsDynamicVisibilityIsVisibleOnTouchDown,
                            onCheckedChange = {
                                scrollbarsDynamicVisibilityIsVisibleOnTouchDown = it
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(weight = 1.0F)
                                .basicMarquee(),
                            text = "IS STATIC WHEN SCROLL POSSIBLE:",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
                        Switch(
                            checked = scrollbarsDynamicVisibilityIsStaticWhenScrollPossible,
                            onCheckedChange = {
                                scrollbarsDynamicVisibilityIsStaticWhenScrollPossible = it
                            }
                        )
                    }

                    if (scrollbarsSampleVisibilityType == SampleVisibilityType.Scale) {
                        Text(
                            modifier = Modifier.padding(top = 12.dp),
                            text = "START SCALE:",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Slider(
                            modifier = Modifier.fillMaxWidth(),
                            value = scrollbarsScaleVisibilityStartScale,
                            onValueChange = {
                                scrollbarsScaleVisibilityStartScale = it
                            },
                            valueRange = 0.5F..1.5F,
                            steps = 9
                        )
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        onClick = {
                            scrollbarsState.highlight()
                        }
                    ) {
                        Text(text = "HIGHLIGHT")
                    }
                }
            }

            var knobAnimationSpecTopPadding = 20.dp

            if (scrollbarsSampleExampleType == SampleScrollbarsExampleType.Default) {
                if (scrollbarsSampleScrollType.isLazyListOrGrid) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "LAZY ITEMS TYPE:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    TabRow(
                        selectedTabIndex = 0,
                        modifier = Modifier.fillMaxWidth(),
                        tabs = {
                            Tab(
                                selected = true,
                                text = {
                                    Text(
                                        modifier = Modifier.basicMarquee(),
                                        text = scrollbarsSampleLazyType.name.uppercase(),
                                        maxLines = 1
                                    )
                                },
                                onClick = { /* Do nothing. */ },
                            )
                        }
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "KNOB TYPE:",
                    style = MaterialTheme.typography.labelLarge
                )
                if (
                    (scrollbarsSampleScrollType == SampleScrollType.Scroll) ||
                    (scrollbarsSampleScrollType.isLazyListOrGrid && scrollbarsSampleLazyType == SampleLazyType.Static)
                ) {
                    TabRow(
                        selectedTabIndex = scrollbarsSampleStaticKnobType.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                        tabs = {
                            SampleStaticKnobType.values().forEach { enumValue ->
                                Tab(
                                    selected = enumValue == scrollbarsSampleStaticKnobType,
                                    text = {
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = enumValue.name.uppercase(),
                                            maxLines = 1
                                        )
                                    },
                                    onClick = {
                                        scrollbarsSampleStaticKnobType = enumValue
                                    },
                                )
                            }
                        }
                    )
                } else {
                    TabRow(
                        selectedTabIndex = scrollbarsSampleDynamicKnobType.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                        tabs = {
                            SampleDynamicKnobType.values().forEach { enumValue ->
                                Tab(
                                    selected = enumValue == scrollbarsSampleDynamicKnobType,
                                    text = {
                                        Text(
                                            modifier = Modifier.basicMarquee(),
                                            text = enumValue.name.uppercase(),
                                            maxLines = 1
                                        )
                                    },
                                    onClick = {
                                        scrollbarsSampleDynamicKnobType = enumValue
                                    },
                                )
                            }
                        }
                    )
                }

                if ((scrollbarsSampleScrollType == SampleScrollType.Scroll &&
                            scrollbarsSampleStaticKnobType == SampleStaticKnobType.Fraction) ||
                    (scrollbarsSampleScrollType.isLazy &&
                            scrollbarsSampleLazyType == SampleLazyType.Static &&
                            scrollbarsSampleStaticKnobType == SampleStaticKnobType.Fraction) ||
                    (scrollbarsSampleScrollType.isLazy &&
                            scrollbarsSampleLazyType == SampleLazyType.Dynamic &&
                            scrollbarsSampleDynamicKnobType == SampleDynamicKnobType.Fraction)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "PERCENTAGE KNOB SIZE FRACTION:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = scrollbarsFractionKnobSizeFraction,
                        onValueChange = {
                            scrollbarsFractionKnobSizeFraction = it
                        },
                        valueRange = 0.25F..0.75F,
                        steps = 10
                    )

                    knobAnimationSpecTopPadding = 12.dp
                }

                if ((scrollbarsSampleScrollType == SampleScrollType.Scroll &&
                            scrollbarsSampleStaticKnobType == SampleStaticKnobType.Exact) ||
                    (scrollbarsSampleScrollType.isLazy &&
                            scrollbarsSampleLazyType == SampleLazyType.Static &&
                            scrollbarsSampleStaticKnobType == SampleStaticKnobType.Exact) ||
                    (scrollbarsSampleScrollType.isLazy &&
                            scrollbarsSampleLazyType == SampleLazyType.Dynamic &&
                            scrollbarsSampleDynamicKnobType == SampleDynamicKnobType.Exact)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        text = "FIXED KNOB SIZE:",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        modifier = Modifier.fillMaxWidth(),
                        value = scrollbarsExactKnobSize.value,
                        onValueChange = {
                            scrollbarsExactKnobSize = it.dp
                        },
                        valueRange = 8.0F..200.0F,
                        steps = 20
                    )

                    knobAnimationSpecTopPadding = 12.dp
                }

                if (scrollbarsSampleScrollType.isLazy &&
                    scrollbarsSampleLazyType == SampleLazyType.Dynamic &&
                    scrollbarsSampleDynamicKnobType == SampleDynamicKnobType.Worm
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(weight = 1.0F)
                                .basicMarquee(),
                            text = "WORM IS SUB LERP:",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
                        Switch(
                            checked = scrollbarsDynamicKnobIsSubLerp,
                            onCheckedChange = {
                                scrollbarsDynamicKnobIsSubLerp = it
                            }
                        )
                    }

                    knobAnimationSpecTopPadding = 12.dp
                }
            }

            Text(
                modifier = Modifier.padding(top = knobAnimationSpecTopPadding),
                text = "KNOB ANIMATION SPEC:",
                style = MaterialTheme.typography.labelLarge
            )
            TabRow(
                selectedTabIndex = scrollbarsSampleKnobTypeAnimationSpec.ordinal,
                modifier = Modifier.fillMaxWidth(),
                tabs = {
                    SampleKnobTypeAnimationSpec.values().forEach { enumValue ->
                        Tab(
                            selected = enumValue == scrollbarsSampleKnobTypeAnimationSpec,
                            text = {
                                Text(
                                    modifier = Modifier.basicMarquee(),
                                    text = enumValue.name.uppercase(),
                                    maxLines = 1
                                )
                            },
                            onClick = {
                                scrollbarsSampleKnobTypeAnimationSpec = enumValue
                            },
                        )
                    }
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                onClick = {
                    reset()
                }
            ) {
                Text(text = "RESET")
            }
        }
    }

    if (isIdleColorPickerVisible || isActiveColorPickerVisible) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = {
                isIdleColorPickerVisible = false
                isActiveColorPickerVisible = false
            },
        ) {
            ClassicColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 300.dp)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                color = HsvColor.from(
                    color = if (isActiveColorPickerVisible) {
                        when (scrollbarsContentConfigureLayerType) {
                            SampleConfigureLayerType.Background -> {
                                scrollbarsBackgroundLayerContentActiveColor
                            }
                            SampleConfigureLayerType.Knob -> {
                                scrollbarsKnobLayerContentActiveColor
                            }
                        }
                    } else {
                        when (scrollbarsContentConfigureLayerType) {
                            SampleConfigureLayerType.Background -> {
                                scrollbarsBackgroundLayerContentIdleColor
                            }
                            SampleConfigureLayerType.Knob -> {
                                scrollbarsKnobLayerContentIdleColor
                            }
                        }
                    }
                )
            ) {
                val targetColor = it.toColor()

                if (isActiveColorPickerVisible) {
                    when (scrollbarsContentConfigureLayerType) {
                        SampleConfigureLayerType.Background -> {
                            scrollbarsBackgroundLayerContentActiveColor = targetColor
                        }
                        SampleConfigureLayerType.Knob -> {
                            scrollbarsKnobLayerContentActiveColor = targetColor
                        }
                    }
                } else {
                    when (scrollbarsContentConfigureLayerType) {
                        SampleConfigureLayerType.Background -> {
                            scrollbarsBackgroundLayerContentIdleColor = targetColor
                        }
                        SampleConfigureLayerType.Knob -> {
                            scrollbarsKnobLayerContentIdleColor = targetColor
                        }
                    }
                }
            }
        }
    }
}

enum class SampleSizeType {
    Full,
    Fraction,
    Exact
}

enum class SampleLayersType {
    Wrap,
    Split
}

enum class SampleConfigureLayerType {
    Background,
    Knob
}

enum class SampleScrollType {
    Scroll,
    LazyList,
    LazyGrid,
    LazyStgrGrid;

    val isLazy: Boolean
        get() = this == LazyList || this == LazyGrid || this == LazyStgrGrid

    val isLazyListOrGrid: Boolean
        get() = this == LazyList || this == LazyGrid

    val isLazyGridOrStgrGrid: Boolean
        get() = this == LazyGrid || this == LazyStgrGrid
}

enum class SampleStaticKnobType {
    Auto,
    Fraction,
    Exact
}

enum class SampleDynamicKnobType {
    Auto,
    Worm,
    Fraction,
    Exact
}

@Suppress("EnumEntryName")
enum class SampleKnobTypeAnimationSpec {
    None,
    Spring,
    Tween_400
}

enum class SampleLazyType {
    Static,
    Dynamic
}

enum class SampleScrollbarsExampleType {
    Default,
    Custom
}

@Suppress("EnumEntryName")
enum class SampleGridSpanCount(val spanCount: Int) {
    Span_1(1),
    Span_2(2),
    Span_3(3)
}

enum class SampleVisibilityType {
    Static,
    Fade,
    Slide,
    Scale
}

enum class SampleDynamicVisibilityAnimationSpec {
    None,
    Spring,
    Default
}

enum class SampleLayerContentType {
    None,
    IdleColored,
    IdleActiveColored;

    val isDefault: Boolean
        get() = this == IdleColored || this == IdleActiveColored
}

enum class SampleDefaultLayerContentShape {
    Rect,
    Rounded,
    Diamond
}

enum class SampleDefaultLayerContentStyle {
    Background,
    Border
}

enum class SampleIdleActiveAnimationSpec {
    None,
    Spring,
    Default
}