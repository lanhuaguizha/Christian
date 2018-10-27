package com.christian

import com.christian.data.Nav
import org.jetbrains.anko.AnkoLogger

/**
 * Contains presenter properties. View initializes first
 */
class BaseViewContract {

    /**
     * View of MVP architecture.
     * Responsible for handling view logic.
     */
    interface View<T> : AnkoLogger {

        var presenter: T

        /**
         * Summary of init view,
         *
         * initCl()
         *
         * initAbl()
         *
         * initTb()
         *
         * initSb()
         *
         * initSrl()
         *
         * initRv()
         *
         * initFs()
         *
         * initFab()
         *
         * initBv()
         *
         * initBnv()
         *
         * initPb()
         */
        fun initView(navs: List<Nav>)

        /**
         * Activity has these views,
         * ConstraintLayout, CoordinatorLayout, AppBarLayout, Toolbar, SearchEditTextLayout,
         * ConstraintLayout, SwipeRefreshLayout, RecyclerView, FastScroller,
         * AnimationFloatingActionButton, BlurView, BottomNavigationView, ProgressBar
         */
        fun deinitView()
    }

    /**
     * Presenter of MVP architecture.
     * Responsible for handling business logic.
     */
    interface Presenter : AnkoLogger {

        /**
         * the view has been pressed
         */
        fun init()

        /**
         * The next view has been pressed
         */
        fun deleteNav(navId: String)

        fun generateNavId(itemId: Int) : String
    }
}