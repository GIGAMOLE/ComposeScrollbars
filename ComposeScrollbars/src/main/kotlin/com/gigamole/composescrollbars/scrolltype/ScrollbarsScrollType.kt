package com.gigamole.composescrollbars.scrolltype

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import com.gigamole.composescrollbars.ScrollbarsState
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsDynamicKnobType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsKnobType
import com.gigamole.composescrollbars.scrolltype.knobtype.ScrollbarsStaticKnobType

/**
 * The scrollbars scroll configuration for [ScrollbarsState].
 *
 * @author GIGAMOLE
 */
sealed interface ScrollbarsScrollType {

    /** The [ScrollbarsKnobType]. */
    val knobType: ScrollbarsKnobType

    /** Indicates whether the scroll is in progress. */
    val isScrollInProgress: Boolean

    /** Indicates whether the scroll is possible. */
    val isScrollPossible: Boolean

    /**
     * The scrollbars for a [ScrollState] content.
     *
     * @property knobType The [ScrollbarsStaticKnobType].
     * @property state The [ScrollState].
     */
    data class Scroll(
        override val knobType: ScrollbarsStaticKnobType = ScrollbarsScrollTypeDefaults.StaticKnobType,
        val state: ScrollState,
    ) : ScrollbarsScrollType {

        /** Indicates whether the scroll is in progress. */
        override val isScrollInProgress: Boolean
            get() = state.isScrollInProgress

        /** Indicates whether the scroll is possible. */
        override val isScrollPossible: Boolean
            get() = state.canScrollForward || state.canScrollBackward
    }

    /** The scrollbars for a lazy scrollable content. */
    sealed interface Lazy : ScrollbarsScrollType {

        /** The scrollbars for a [LazyListState] content. */
        sealed interface List : Lazy {

            /** The [LazyListState]. */
            val state: LazyListState

            /** Indicates whether the scroll is in progress. */
            override val isScrollInProgress: Boolean
                get() = state.isScrollInProgress

            /** Indicates whether the scroll is possible. */
            override val isScrollPossible: Boolean
                get() = state.canScrollForward || state.canScrollBackward

            /**
             * The scrollbars for a [LazyListState] content with a static items heights.
             *
             * @property knobType The [ScrollbarsStaticKnobType].
             * @property state The [LazyListState].
             */
            data class Static(
                override val knobType: ScrollbarsStaticKnobType = ScrollbarsScrollTypeDefaults.StaticKnobType,
                override val state: LazyListState
            ) : List

            /**
             * The scrollbars for a [LazyListState] content with a dynamic items heights.
             *
             * @property knobType The [ScrollbarsDynamicKnobType].
             * @property state The [LazyListState].
             */
            data class Dynamic(
                override val knobType: ScrollbarsDynamicKnobType = ScrollbarsScrollTypeDefaults.DynamicKnobType,
                override val state: LazyListState
            ) : List
        }

        /** The scrollbars for a [LazyGridState] content. */
        sealed interface Grid : Lazy {

            /** The [LazyGridState]. */
            val state: LazyGridState

            /** The grid span count. */
            val spanCount: Int

            /** Indicates whether the scroll is in progress. */
            override val isScrollInProgress: Boolean
                get() = state.isScrollInProgress

            /** Indicates whether the scroll is possible. */
            override val isScrollPossible: Boolean
                get() = state.canScrollForward || state.canScrollBackward

            /**
             * The scrollbars for a [LazyGridState] content with a static items heights.
             *
             * @property knobType The [ScrollbarsStaticKnobType].
             * @property state The [LazyGridState].
             * @property spanCount The grid span count.
             */
            data class Static(
                override val knobType: ScrollbarsStaticKnobType = ScrollbarsScrollTypeDefaults.StaticKnobType,
                override val state: LazyGridState,
                override val spanCount: Int
            ) : Grid

            /**
             * The scrollbars for a [LazyGridState] content with a dynamic items heights.
             *
             * @property knobType The [ScrollbarsDynamicKnobType].
             * @property state The [LazyGridState].
             * @property spanCount The grid span count.
             */
            data class Dynamic(
                override val knobType: ScrollbarsDynamicKnobType = ScrollbarsScrollTypeDefaults.DynamicKnobType,
                override val state: LazyGridState,
                override val spanCount: Int
            ) : Grid
        }

        /**
         * The scrollbars for a [LazyStaggeredGridState] content.
         *
         * The ComposeScrollbars library highly recommends to use a [knobType] with a provided [ScrollbarsDynamicKnobType.animationSpec], because staggered layout
         * sometimes can be unpredicted and some items can be placed in the way that the interpolation between these items is too short or almost zero, which causes some
         * knob jumps. Also, [LazyStaggeredGridState.layoutInfo] item spacing, before and after content paddings, causes some extra calculations, so if it is possible,
         * add these padding to the items/cards instead, to improve performance.
         *
         * @property knobType The [ScrollbarsDynamicKnobType].
         * @property state The [LazyStaggeredGridState].
         * @property spanCount The grid span count.
         */
        data class StaggeredGrid(
            override val knobType: ScrollbarsDynamicKnobType = ScrollbarsScrollTypeDefaults.DynamicKnobType,
            val state: LazyStaggeredGridState,
            val spanCount: Int
        ) : Lazy {

            /** Indicates whether the scroll is in progress. */
            override val isScrollInProgress: Boolean
                get() = state.isScrollInProgress

            /** Indicates whether the scroll is possible. */
            override val isScrollPossible: Boolean
                get() = state.canScrollForward || state.canScrollBackward
        }
    }
}
