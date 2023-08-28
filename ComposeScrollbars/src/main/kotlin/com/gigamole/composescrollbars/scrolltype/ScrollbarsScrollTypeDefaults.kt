package com.gigamole.composescrollbars.scrolltype

import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsDynamicKnobType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsStaticKnobType

/**
 * The default values for [ScrollbarsScrollType].
 *
 * @see ScrollbarsState
 * @author GIGAMOLE
 */
object ScrollbarsScrollTypeDefaults {

    /** The default static [ScrollbarsStaticKnobType]. */
    val StaticKnobType: ScrollbarsStaticKnobType = ScrollbarsStaticKnobType.Auto()

    /** The default dynamic [ScrollbarsStaticKnobType]. */
    val DynamicKnobType: ScrollbarsDynamicKnobType = ScrollbarsDynamicKnobType.Auto()
}
