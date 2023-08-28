package com.gigamole.composescrollbars.config.layersType.layerConfig

import androidx.compose.foundation.layout.PaddingValues
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType

/**
 * The scrollbars layer config inside the layers container for [ScrollbarsLayersType.Split].
 *
 * @property thicknessType The scrollbars layer [ScrollbarsThicknessType].
 * @property layerGravity The [ScrollbarsLayerGravity].
 * @property paddingValues The scrollbars layer [PaddingValues].
 * @see ScrollbarsLayersType
 * @author GIGAMOLE
 */
data class ScrollbarsLayerConfig(
    val thicknessType: ScrollbarsThicknessType = ScrollbarsLayerConfigDefaults.ThicknessType,
    val layerGravity: ScrollbarsLayerGravity = ScrollbarsLayerConfigDefaults.Gravity,
    val paddingValues: PaddingValues = ScrollbarsLayerConfigDefaults.PaddingValues
)