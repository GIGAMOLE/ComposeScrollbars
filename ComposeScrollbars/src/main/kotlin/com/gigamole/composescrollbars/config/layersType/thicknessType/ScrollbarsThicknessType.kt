package com.gigamole.composescrollbars.config.layersType.thicknessType

import androidx.compose.ui.unit.Dp
import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.layersType.layerConfig.ScrollbarsLayerConfig

/**
 * The scrollbars thickness type configuration for knob and/or background [ScrollbarsLayersType].
 *
 * @see ScrollbarsLayerConfig
 * @see ScrollbarsOrientation
 * @author GIGAMOLE
 */
sealed interface ScrollbarsThicknessType {

    /**
     * The exact scrollbars thickness size (width or height, depends on [ScrollbarsOrientation]).
     *
     * @property thickness The exact thickness size.
     */
    data class Exact(
        val thickness: Dp = ScrollbarsThicknessTypeDefaults.Exact.Thickness
    ) : ScrollbarsThicknessType

    /**
     * The wrap content scrollbars thickness size (width or height, depends on [ScrollbarsOrientation]).
     *
     * Basically, it is only used for a [ScrollbarsLayerContentType.Custom], so it is possible to have a dynamic thickness size.
     */
    data object Wrap : ScrollbarsThicknessType
}