package com.gigamole.composescrollbars.sample

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.gigamole.composescrollbars.Scrollbars
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.ScrollbarsGravity
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layercontenttype.layercontentstyletype.ScrollbarsLayerContentStyleType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerConfig
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerGravity
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType
import com.gigamole.composescrollbars.rememberScrollbarsState
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsStaticKnobType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt

private val sampleTitleColor = Color(0xFF007AC9)
private val sampleTitleBgColor = sampleTitleColor.copy(alpha = 0.05F)
private val sampleDescriptionColor = Color(0xFFE21776)
private val sampleDescriptionBgColor = sampleDescriptionColor.copy(alpha = 0.05F)
private val sampleStarColor = Color.White
private val sampleStarBgColor = Color(0xFFFFC107)

@Suppress("unused")
@Composable
fun MainScreenDemoContent() {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val verticalScrollAmount = with(LocalDensity.current) { 30.dp.toPx() }.roundToInt()
    val horizontalScrollAmount = with(LocalDensity.current) { 128.dp.toPx() }.roundToInt()
    var triggerId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(triggerId) {
        if (triggerId == null) {
            return@LaunchedEffect
        }

        coroutineScope.launch {
            coroutineScope.launch {
                verticalScrollState.animateScrollTo(
                    value = verticalScrollAmount,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
            coroutineScope.launch {
                delay(600)
                horizontalScrollState.animateScrollTo(
                    value = horizontalScrollAmount,
                    animationSpec = tween(durationMillis = 1300)
                )
            }
            coroutineScope.launch {
                delay(1300)
                verticalScrollState.animateScrollTo(
                    value = -verticalScrollAmount,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
            coroutineScope.launch {
                delay(2500)
                horizontalScrollState.animateScrollTo(
                    value = -horizontalScrollAmount,
                    animationSpec = tween(durationMillis = 600)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    triggerId = UUID
                        .randomUUID()
                        .toString()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.height(height = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 32.dp)
            ) {
                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            paddingValues = PaddingValues(),
                            layersType = ScrollbarsLayersType.Wrap(paddingValues = PaddingValues(all = 2.dp)),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Custom {
                                // Set shadowed background.
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(
                                            elevation = 4.dp,
                                            shape = RectangleShape,
                                            clip = true,
                                            spotColor = Color.DarkGray.copy(alpha = 0.5F),
                                            ambientColor = Color.DarkGray.copy(alpha = 0.5F)
                                        )
                                        .background(
                                            color = Color.White,
                                            shape = RectangleShape
                                        )
                                )
                            },
                            knobLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(
                                shape = RectangleShape,
                                idleColor = Color.LightGray.copy(alpha = 0.25F)
                            )
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.3F)
                        )
                    )
                )

                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            paddingValues = PaddingValues(),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(idleColor = Color.LightGray.copy(alpha = 0.2F)),
                            knobLayerContentType = ScrollbarsLayerContentType.Custom {
                                // Set shadowed knob.
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(
                                            elevation = 4.dp,
                                            shape = CircleShape,
                                            clip = true
                                        )
                                        .background(
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                )
                            }
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.4F)
                        )
                    )
                )

                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            paddingValues = PaddingValues(),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(idleColor = Color.LightGray.copy(alpha = 0.25F)),
                            knobLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive(
                                idleColor = Color.LightGray.copy(alpha = 0.4F),
                                activeColor = Color.LightGray,
                                outAnimationSpec = tween(
                                    durationMillis = 200,
                                    delayMillis = 300
                                )
                            )
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.5F)
                        )
                    )
                )

                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            paddingValues = PaddingValues(),
                            layersType = ScrollbarsLayersType.Wrap(
                                paddingValues = PaddingValues(
                                    horizontal = 4.dp,
                                    vertical = 6.dp
                                )
                            ),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive(
                                idleColor = Color.LightGray.copy(alpha = 0.4F),
                                activeColor = Color.LightGray.copy(alpha = 0.8F),
                                shape = CutCornerShape(percent = 100),
                                styleType = ScrollbarsLayerContentStyleType.Border(width = 2.dp),
                                outAnimationSpec = tween(
                                    durationMillis = 200,
                                    delayMillis = 300
                                )
                            ),
                            knobLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive(
                                idleColor = Color.LightGray.copy(alpha = 0.4F),
                                activeColor = Color.LightGray.copy(alpha = 0.8F),
                                shape = CutCornerShape(percent = 100),
                                outAnimationSpec = tween(
                                    durationMillis = 200,
                                    delayMillis = 300
                                )
                            )
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.6F)
                        )
                    )
                )

                Box(
                    modifier = Modifier
                        .height(height = 160.dp)
                        .width(width = 250.dp)
                        .background(color = sampleTitleBgColor)
                        .verticalScroll(state = verticalScrollState)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 32.dp,
                                vertical = 48.dp
                            ),
                        text = "Compose\nScrollbars",
                        color = sampleTitleColor,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamilySpaceGrotesk
                    )
                }

                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            gravity = ScrollbarsGravity.Start,
                            paddingValues = PaddingValues(),
                            layersType = ScrollbarsLayersType.Split(
                                backgroundLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 2.dp),
                                    layerGravity = ScrollbarsLayerGravity.Start
                                ),
                                knobLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 6.dp),
                                    layerGravity = ScrollbarsLayerGravity.Start,
                                    paddingValues = PaddingValues(start = 2.dp)
                                )
                            ),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive(
                                idleColor = Color.LightGray.copy(alpha = 0.4F),
                                activeColor = Color.LightGray.copy(alpha = 0.8F),
                                shape = RectangleShape,
                                outAnimationSpec = tween(
                                    durationMillis = 200,
                                    delayMillis = 300
                                )
                            ),
                            knobLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive(
                                idleColor = Color.LightGray.copy(alpha = 0.4F),
                                activeColor = Color.LightGray.copy(alpha = 0.8F),
                                shape = RectangleShape,
                                outAnimationSpec = tween(
                                    durationMillis = 200,
                                    delayMillis = 300
                                )
                            )
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.6F)
                        )
                    )
                )

                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            gravity = ScrollbarsGravity.Start,
                            paddingValues = PaddingValues(),
                            layersType = ScrollbarsLayersType.Split(
                                backgroundLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 2.dp),
                                    layerGravity = ScrollbarsLayerGravity.Center
                                ),
                                knobLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 10.dp),
                                    layerGravity = ScrollbarsLayerGravity.Center
                                )
                            ),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive(
                                idleColor = Color.LightGray.copy(alpha = 0.3F),
                                activeColor = Color.LightGray.copy(alpha = 0.6F),
                                shape = RectangleShape,
                                outAnimationSpec = tween(
                                    durationMillis = 200,
                                    delayMillis = 300
                                )
                            ),
                            knobLayerContentType = ScrollbarsLayerContentType.Custom { state ->
                                // Take the idle active color from the background to match it on knob.
                                val idleActiveColor = (state.config.backgroundLayerContentType as?
                                        ScrollbarsLayerContentType.Default.Colored.IdleActive)?.idleActiveColor ?: Color.LightGray.copy(alpha = 0.3F)

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = Color.White,
                                            shape = RectangleShape
                                        )
                                        .border(
                                            color = idleActiveColor,
                                            width = 2.dp
                                        )
                                )
                            }
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.5F)
                        )
                    )
                )

                Scrollbars(
                    modifier = Modifier
                        .width(width = 16.dp)
                        .alpha(alpha = 0.3F),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            paddingValues = PaddingValues(),
                            layersType = ScrollbarsLayersType.Split(
                                backgroundLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 4.dp),
                                    layerGravity = ScrollbarsLayerGravity.Center
                                ),
                                knobLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 16.dp),
                                    layerGravity = ScrollbarsLayerGravity.Center
                                )
                            ),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Custom {
                                // Custom line with bulbs background.
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .align(alignment = Alignment.Center)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(width = 2.dp)
                                            .padding(vertical = 8.dp)
                                            .align(alignment = Alignment.Center)
                                            .background(color = Color.LightGray)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 6.dp)
                                            .size(size = 4.dp)
                                            .background(
                                                color = Color.LightGray,
                                                shape = CircleShape
                                            )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                            .size(size = 4.dp)
                                            .align(alignment = Alignment.BottomCenter)
                                            .background(
                                                color = Color.LightGray,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            },
                            knobLayerContentType = ScrollbarsLayerContentType.Custom {
                                // Custom dot with increasing size custom knob.
                                val knobFraction = 1.0F - if (it.scrollFraction <= 0.5F) {
                                    1.0F - (it.scrollFraction * 2.0F)
                                } else {
                                    (it.scrollFraction - 0.5F) * 2.0F
                                }
                                val knobSize = lerp(
                                    start = 6.dp,
                                    stop = 16.dp,
                                    fraction = knobFraction
                                )
                                val knobColor = androidx.compose.ui.graphics.lerp(
                                    start = Color.LightGray,
                                    stop = Color.DarkGray,
                                    fraction = knobFraction
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(alignment = Alignment.Center),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(size = knobSize)
                                            .background(
                                                color = knobColor,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Exact(size = 16.dp)
                        )
                    )
                )

                Scrollbars(
                    modifier = Modifier.width(width = 16.dp),
                    state = rememberScrollbarsState(
                        config = ScrollbarsConfig(
                            orientation = ScrollbarsOrientation.Vertical,
                            paddingValues = PaddingValues(),
                            layersType = ScrollbarsLayersType.Split(
                                backgroundLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 2.dp),
                                    layerGravity = ScrollbarsLayerGravity.Center,
                                    paddingValues = PaddingValues(vertical = 6.dp)
                                ),
                                knobLayerConfig = ScrollbarsLayerConfig(
                                    thicknessType = ScrollbarsThicknessType.Exact(thickness = 12.dp),
                                    layerGravity = ScrollbarsLayerGravity.Center
                                )
                            ),
                            backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(
                                idleColor = Color.LightGray.copy(alpha = 0.2F),
                                shape = CircleShape
                            ),
                            knobLayerContentType = ScrollbarsLayerContentType.Custom {
                                // Custom circle shadow knob.
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(
                                            elevation = 4.dp,
                                            shape = CircleShape,
                                            clip = true
                                        )
                                        .background(
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                )
                            }
                        ),
                        scrollType = ScrollbarsScrollType.Scroll(
                            state = verticalScrollState,
                            knobType = ScrollbarsStaticKnobType.Exact(size = 12.dp)
                        )
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 32.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(width = 160.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Scrollbars(
                        modifier = Modifier.height(height = 10.dp),
                        state = rememberScrollbarsState(
                            config = ScrollbarsConfig(
                                orientation = ScrollbarsOrientation.Horizontal,
                                gravity = ScrollbarsGravity.Start,
                                paddingValues = PaddingValues(),
                                layersType = ScrollbarsLayersType.Split(
                                    backgroundLayerConfig = ScrollbarsLayerConfig(
                                        thicknessType = ScrollbarsThicknessType.Exact(thickness = 5.dp),
                                        layerGravity = ScrollbarsLayerGravity.Center
                                    ),
                                    knobLayerConfig = ScrollbarsLayerConfig(
                                        thicknessType = ScrollbarsThicknessType.Exact(thickness = 10.dp),
                                        layerGravity = ScrollbarsLayerGravity.Center
                                    )
                                ),
                                backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(
                                    idleColor = Color.LightGray.copy(alpha = 0.45F),
                                    shape = CircleShape
                                ),
                                knobLayerContentType = ScrollbarsLayerContentType.Custom {
                                    // Custom circle long knob.
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                color = Color.White,
                                                shape = CircleShape
                                            )
                                            .border(
                                                color = Color.LightGray.copy(alpha = 0.45F),
                                                width = 2.dp,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            ),
                            scrollType = ScrollbarsScrollType.Scroll(
                                state = horizontalScrollState,
                                knobType = ScrollbarsStaticKnobType.Fraction(fraction = 0.5F)
                            )
                        )
                    )

                    Scrollbars(
                        modifier = Modifier.height(height = 16.dp),
                        state = rememberScrollbarsState(
                            config = ScrollbarsConfig(
                                orientation = ScrollbarsOrientation.Horizontal,
                                gravity = ScrollbarsGravity.End,
                                paddingValues = PaddingValues(),
                                layersType = ScrollbarsLayersType.Split(
                                    backgroundLayerConfig = ScrollbarsLayerConfig(
                                        thicknessType = ScrollbarsThicknessType.Exact(thickness = 2.dp),
                                        layerGravity = ScrollbarsLayerGravity.End
                                    ),
                                    knobLayerConfig = ScrollbarsLayerConfig(
                                        thicknessType = ScrollbarsThicknessType.Exact(thickness = 10.dp),
                                        layerGravity = ScrollbarsLayerGravity.End,
                                        paddingValues = PaddingValues(bottom = 6.dp)
                                    )
                                ),
                                backgroundLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(
                                    idleColor = Color.LightGray.copy(alpha = 0.55F),
                                    shape = RectangleShape
                                ),
                                knobLayerContentType = ScrollbarsLayerContentType.Default.Colored.Idle(
                                    idleColor = Color.LightGray.copy(alpha = 0.55F),
                                    shape = RectangleShape
                                ),
                            ),
                            scrollType = ScrollbarsScrollType.Scroll(
                                state = horizontalScrollState,
                                knobType = ScrollbarsStaticKnobType.Exact(size = 10.dp)
                            )
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .width(width = 250.dp)
                        .height(height = 48.dp)
                        .background(color = sampleDescriptionBgColor)
                        .horizontalScroll(state = horizontalScrollState),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Polish Android Compose UI with advanced scrollbars",
                        color = sampleDescriptionColor,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamilyOpenSans
                    )
                }

                Column(
                    modifier = Modifier
                        .width(width = 160.dp)
                        .height(height = 48.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Scrollbars(
                        state = rememberScrollbarsState(
                            config = ScrollbarsConfig(
                                orientation = ScrollbarsOrientation.Horizontal,
                                gravity = ScrollbarsGravity.End,
                                paddingValues = PaddingValues(),
                                layersType = ScrollbarsLayersType.Split(
                                    backgroundLayerConfig = ScrollbarsLayerConfig(
                                        thicknessType = ScrollbarsThicknessType.Wrap,
                                        layerGravity = ScrollbarsLayerGravity.End,
                                        paddingValues = PaddingValues(horizontal = 12.dp)
                                    ),
                                    knobLayerConfig = ScrollbarsLayerConfig(
                                        thicknessType = ScrollbarsThicknessType.Wrap,
                                        layerGravity = ScrollbarsLayerGravity.End
                                    ),
                                ),
                                backgroundLayerContentType = ScrollbarsLayerContentType.Custom { state ->
                                    // Custom increasing triangle background.
                                    val bgColor = androidx.compose.ui.graphics.lerp(
                                        start = Color.LightGray.copy(alpha = 0.25F),
                                        stop = Color.LightGray.copy(alpha = 0.55F),
                                        fraction = state.scrollFraction
                                    )

                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(height = 6.dp)
                                    ) {
                                        drawPath(
                                            path = Path().apply {
                                                moveTo(
                                                    x = 0.0F,
                                                    y = size.height
                                                )
                                                lineTo(
                                                    x = size.width,
                                                    y = 0.0F
                                                )
                                                lineTo(
                                                    x = size.width,
                                                    y = size.height
                                                )
                                                close()
                                            },
                                            color = bgColor
                                        )
                                    }
                                },
                                knobLayerContentType = ScrollbarsLayerContentType.Custom { state ->
                                    // Custom sliding popup with a 100% special star knob.
                                    val maxReached by remember(state.scrollFraction) {
                                        derivedStateOf {
                                            state.scrollFraction >= 0.99F
                                        }
                                    }
                                    var prevScrollFraction by remember { mutableFloatStateOf(state.scrollFraction) }
                                    val isForward = when {
                                        prevScrollFraction < state.scrollFraction -> {
                                            true
                                        }
                                        prevScrollFraction > state.scrollFraction -> {
                                            false
                                        }
                                        else -> {
                                            null
                                        }
                                    }
                                    prevScrollFraction = state.scrollFraction

                                    val progressKnobColor = androidx.compose.ui.graphics.lerp(
                                        start = Color.LightGray.copy(alpha = 0.25F),
                                        stop = Color.LightGray.copy(alpha = 0.55F),
                                        fraction = state.scrollFraction
                                    )
                                    val knobColor by animateColorAsState(
                                        targetValue = if (maxReached) {
                                            sampleStarBgColor
                                        } else {
                                            progressKnobColor
                                        },
                                        label = "KnobColor"
                                    )
                                    val progressTextColor = androidx.compose.ui.graphics.lerp(
                                        start = Color.DarkGray.copy(alpha = 0.5F),
                                        stop = Color.DarkGray.copy(alpha = 0.8F),
                                        fraction = state.scrollFraction
                                    )
                                    val textColor by animateColorAsState(
                                        targetValue = if (maxReached) {
                                            sampleStarColor
                                        } else {
                                            progressTextColor
                                        },
                                        label = "TextColor"
                                    )
                                    val progressKnobScale = androidx.compose.ui.util.lerp(
                                        start = 0.85F,
                                        stop = 1.25F,
                                        fraction = state.scrollFraction
                                    )
                                    val knobScale by animateFloatAsState(
                                        targetValue = if (maxReached) {
                                            1.85F
                                        } else {
                                            progressKnobScale
                                        },
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = 7_000.0F
                                        ),
                                        label = "KnobScale"
                                    )
                                    val knobOffset = lerp(
                                        start = 0.dp,
                                        stop = (-6).dp,
                                        fraction = state.scrollFraction
                                    )
                                    val knobOffsetPx = with(LocalDensity.current) { knobOffset.toPx() }
                                    val knobRotation by animateFloatAsState(
                                        targetValue = if (maxReached) {
                                            0.0F
                                        } else {
                                            if (state.scrollType.isScrollInProgress && isForward != null) {
                                                if (isForward) {
                                                    -12.0F
                                                } else {
                                                    12.0F
                                                }
                                            } else {
                                                0.0F
                                            }
                                        },
                                        animationSpec = if (maxReached) {
                                            spring(
                                                dampingRatio = 0.18F,
                                                stiffness = Spring.StiffnessMedium
                                            )
                                        } else {
                                            spring(
                                                dampingRatio = 0.35F,
                                                stiffness = Spring.StiffnessMediumLow
                                            )
                                        },
                                        label = "KnobRotation"
                                    )
                                    val knobText = (state.scrollFraction * 100.0F).roundToInt().toString()

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp)
                                            .graphicsLayer(
                                                scaleX = knobScale,
                                                scaleY = knobScale,
                                                translationY = knobOffsetPx,
                                                rotationZ = knobRotation,
                                                transformOrigin = TransformOrigin(
                                                    pivotFractionX = 0.5F,
                                                    pivotFractionY = 1.0F
                                                )
                                            ),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(height = 22.dp)
                                                .background(
                                                    color = knobColor,
                                                    shape = RoundedCornerShape(size = 6.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            AnimatedContent(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center,
                                                targetState = maxReached,
                                                transitionSpec = {
                                                    (fadeIn(animationSpec = spring()) +
                                                            scaleIn(animationSpec = spring())).togetherWith(
                                                        fadeOut(animationSpec = spring()) +
                                                                scaleOut(animationSpec = spring())
                                                    )
                                                },
                                                label = "KnobTextIconContent"
                                            ) { maxReachedState ->
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (maxReachedState) {
                                                        Image(
                                                            modifier = Modifier.size(size = 14.dp),
                                                            painter = painterResource(id = R.drawable.ic_star),
                                                            contentScale = ContentScale.FillBounds,
                                                            colorFilter = ColorFilter.tint(color = sampleStarColor),
                                                            contentDescription = ""
                                                        )
                                                    } else {
                                                        Text(
                                                            text = knobText,
                                                            color = textColor,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = TextUnit(
                                                                value = 10.0F,
                                                                type = TextUnitType.Sp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Canvas(
                                            modifier = Modifier
                                                .height(height = 6.dp)
                                                .width(width = 12.dp)
                                        ) {
                                            val path = Path().apply {
                                                moveTo(
                                                    x = 0.0F,
                                                    y = 0.0F
                                                )
                                                lineTo(
                                                    x = size.width * 0.25F,
                                                    y = 0.0F
                                                )
                                                lineTo(
                                                    x = size.center.x,
                                                    y = size.height
                                                )
                                                lineTo(
                                                    x = size.width * 0.75F,
                                                    y = 0.0F
                                                )
                                                lineTo(
                                                    x = size.width,
                                                    y = 0.0F
                                                )
                                                lineTo(
                                                    x = size.width,
                                                    y = 0.0F
                                                )
                                                lineTo(
                                                    x = 0.0F,
                                                    y = 0.0F
                                                )
                                                close()
                                            }

                                            drawIntoCanvas { canvas ->
                                                canvas.drawOutline(
                                                    outline = Outline.Generic(path = path),
                                                    paint = Paint().apply {
                                                        color = knobColor
                                                        style = PaintingStyle.Fill

                                                        pathEffect = PathEffect.cornerPathEffect(radius = 8.dp.toPx())
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            ),
                            scrollType = ScrollbarsScrollType.Scroll(
                                state = horizontalScrollState,
                                knobType = ScrollbarsStaticKnobType.Exact(size = 24.dp)
                            )
                        )
                    )
                }
            }
        }
    }
}