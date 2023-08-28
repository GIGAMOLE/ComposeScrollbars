package com.gigamole.composescrollbars.config.layersType.layerConfig

import com.gigamole.composescrollbars.config.ScrollbarsOrientation
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType

/**
 * The scrollbars layer gravity inside the layers container for [ScrollbarsLayerConfig].
 *
 * @see ScrollbarsLayersType.Split
 * @author GIGAMOLE
 */
enum class ScrollbarsLayerGravity {

    /** The scrollbars layer container gravity at the start anchor (left or top, depends on [ScrollbarsOrientation]). */
    Start,

    /** The scrollbars layer container gravity at the center anchor. */
    Center,

    /** The scrollbars layer container gravity at the start anchor (right or bottom, depends on [ScrollbarsOrientation]). */
    End
}