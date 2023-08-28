package com.gigamole.composescrollbars.config.layersType

import androidx.compose.foundation.layout.PaddingValues
import com.gigamole.composescrollbars.config.ScrollbarsConfig
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersTypeDefaults.Wrap.PaddingValues
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerConfig
import com.gigamole.composescrollbars.config.layersType.thicknessType.ScrollbarsThicknessType

/**
 * The default values for [ScrollbarsLayersType].
 *
 * @see ScrollbarsConfig
 * @author GIGAMOLE
 */
class ScrollbarsLayersTypeDefaults {

    /** The default values for [ScrollbarsLayersType.Wrap]. */
    object Wrap {

        /** The default scrollbars layer [ScrollbarsThicknessType]. */
        val ThicknessType: ScrollbarsThicknessType = ScrollbarsThicknessType.Exact()

        /** The default scrollbars layer [PaddingValues]. */
        val PaddingValues: PaddingValues = PaddingValues()
    }

    /** The default values for [ScrollbarsLayersType.Split]. */
    object Split {

        /** The default background [ScrollbarsLayerConfig]. */
        val BackgroundLayerConfig: ScrollbarsLayerConfig = ScrollbarsLayerConfig()

        /** The default knob [ScrollbarsLayerConfig]. */
        val KnobLayerConfig: ScrollbarsLayerConfig = ScrollbarsLayerConfig()
    }
}