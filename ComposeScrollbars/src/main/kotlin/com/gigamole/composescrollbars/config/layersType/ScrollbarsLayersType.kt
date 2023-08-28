package com.gigamole.composescrollbars.config.layersType

import androidx.compose.foundation.layout.PaddingValues
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerConfig
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType

/**
 * The scrollbars layers type configuration for [ScrollbarsConfig].
 *
 * @see ScrollbarsLayerConfig
 * @see ScrollbarsThicknessType
 * @see ScrollbarsLayerContentType
 * @author GIGAMOLE
 */
sealed interface ScrollbarsLayersType {

    /**
     * The scrollbars layers type which wraps a knob layer into a background layer. The layers are centered.
     *
     * @property thicknessType The knob [ScrollbarsThicknessType].
     * @property paddingValues The background [PaddingValues].
     */
    data class Wrap(
        val thicknessType: ScrollbarsThicknessType = ScrollbarsLayersTypeDefaults.Wrap.ThicknessType,
        val paddingValues: PaddingValues = ScrollbarsLayersTypeDefaults.Wrap.PaddingValues
    ) : ScrollbarsLayersType

    /**
     * The scrollbars layers type which splits a knob and a background layer into each own [ScrollbarsLayerConfig].
     *
     * @property backgroundLayerConfig The knob [ScrollbarsLayerConfig].
     * @property knobLayerConfig The background [ScrollbarsLayerConfig].
     */
    data class Split(
        val backgroundLayerConfig: ScrollbarsLayerConfig = ScrollbarsLayersTypeDefaults.Split.BackgroundLayerConfig,
        val knobLayerConfig: ScrollbarsLayerConfig = ScrollbarsLayersTypeDefaults.Split.KnobLayerConfig
    ) : ScrollbarsLayersType
}