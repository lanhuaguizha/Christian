package com.christian

import com.christian.data.Nav
import com.christian.nav.NavFragment
import org.jetbrains.anko.AnkoLogger

/**
 * Contains presenter properties. View initializes first
 */
class BaseViewContract {

    /**
     * View of MVP architecture.
     * Responsible for handling view logic.
     */
    interface IView<T> : AnkoLogger {

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
        fun initView(navFragments: List<NavFragment>, navs: List<Nav>)

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
    interface IPresenter : AnkoLogger {

        /**
         * the view has been pressed
         */
        fun init(isActivityInit: Boolean = true, position: Int)
    }
}