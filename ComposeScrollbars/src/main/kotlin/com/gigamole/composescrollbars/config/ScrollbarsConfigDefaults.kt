package com.gigamole.composescrollbars.config

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.config.layercontenttype.ScrollbarsLayerContentType
import com.gigamole.composescrollbars.config.layersType.ScrollbarsLayersType
import com.gigamole.composescrollbars.config.sizetype.ScrollbarsSizeType
import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityType

/**
 * The default values for [ScrollbarsConfig].
 *
 * @see ScrollbarsState
 * @see ScrollbarsGravity
 * @see ScrollbarsOrientation
 * @author GIGAMOLE
 */
object ScrollbarsConfigDefaults {

    /** The default [ScrollbarsGravity]. */
    val Gravity: ScrollbarsGravity = ScrollbarsGravity.End

    /** The default is reverse layout indicator. */
    const val IsReverseLayout: Boolean = false

    /** The default scrollbars layers container [PaddingValues]. */
    val PaddingValues: PaddingValues = PaddingValues(all = 4.dp)

    /** The default [ScrollbarsSizeType]. */
    val SizeType: ScrollbarsSizeType = ScrollbarsSizeType.Full

    /** The default [ScrollbarsLayersType]. */
    val LayersType: ScrollbarsLayersType = ScrollbarsLayersType.Wrap()

    /** The default background [ScrollbarsLayerContentType]. */
    val BackgroundLayerContentType: ScrollbarsLayerContentType = ScrollbarsLayerContentType.None

    /** The default knob [ScrollbarsLayerContentType]. */
    val KnobLayerContentType: ScrollbarsLayerContentType = ScrollbarsLayerContentType.Default.Colored.IdleActive()

    /** The default [ScrollbarsVisibilityType]. */
    val VisibilityType: ScrollbarsVisibilityType = ScrollbarsVisibilityType.Dynamic.Fade()
}
