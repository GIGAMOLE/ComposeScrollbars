package com.gigamole.composescrollbars.config

import androidx.compose.foundation.layout.PaddingValues
import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.sizetype.ScrollbarsSizeType
import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityType
import com.gigamole.composescrollbars.scrolltype.ScrollbarsScrollType

/**
 * The scrollbars appearance configuration for [ScrollbarsState].
 *
 * The [orientation] should be the same as the orientation of [ScrollbarsScrollType.knobType] state.
 *
 * @property orientation The [ScrollbarsOrientation].
 * @property gravity The [ScrollbarsGravity].
 * @property isReverseLayout The reverse layout indicator.
 * @property paddingValues The scrollbars layers container [PaddingValues].
 * @property sizeType The [ScrollbarsSizeType].
 * @property layersType The [ScrollbarsLayersType].
 * @property backgroundLayerContentType The background [ScrollbarsLayerContentType].
 * @property knobLayerContentType The knob [ScrollbarsLayerContentType].
 * @property visibilityType The [ScrollbarsVisibilityType].
 * @see ScrollbarsScrollType
 * @author GIGAMOLE
 */
data class ScrollbarsConfig(
    val orientation: ScrollbarsOrientation,
    val gravity: ScrollbarsGravity = ScrollbarsConfigDefaults.Gravity,
    val isReverseLayout: Boolean = ScrollbarsConfigDefaults.IsReverseLayout,
    val paddingValues: PaddingValues = ScrollbarsConfigDefaults.PaddingValues,
    val sizeType: ScrollbarsSizeType = ScrollbarsConfigDefaults.SizeType,
    val layersType: ScrollbarsLayersType = ScrollbarsConfigDefaults.LayersType,
    val backgroundLayerContentType: ScrollbarsLayerContentType = ScrollbarsConfigDefaults.BackgroundLayerContentType,
    val knobLayerContentType: ScrollbarsLayerContentType = ScrollbarsConfigDefaults.KnobLayerContentType,
    val visibilityType: ScrollbarsVisibilityType = ScrollbarsConfigDefaults.VisibilityType
)