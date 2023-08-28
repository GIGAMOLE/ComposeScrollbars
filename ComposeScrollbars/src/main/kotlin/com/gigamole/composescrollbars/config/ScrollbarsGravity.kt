package com.gigamole.composescrollbars.config

import com.gigamole.composescrollbars.config.visibilitytype.ScrollbarsVisibilityType

/**
 * The scrollbars layers container gravity for [ScrollbarsConfig].
 *
 * @see ScrollbarsOrientation
 * @see ScrollbarsVisibilityType
 * @author GIGAMOLE
 */
enum class ScrollbarsGravity {

    /** The scrollbars layers container gravity at the start anchor (left or top, depends on [ScrollbarsOrientation]). */
    Start,

    /** The scrollbars layers container gravity at the end anchor (right or bottom, depends on [ScrollbarsOrientation]). */
    End
}