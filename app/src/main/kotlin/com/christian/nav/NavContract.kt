package com.christian.nav

import com.christian.BaseContract
import com.christian.data.Bean
import com.christian.data.Gospels
import org.jetbrains.anko.AnkoLogger

/**
 * Contract of Nav View & Presenter.
 * You can see all the interfaces of the Nav here,
 * also defining everything.
 */
class NavContract {

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav view
     */
    interface INavActivity : BaseContract.IView<IPresenter> {

        /**
         * Summary of init view, initCl(), initAbl(), initTb(), initSb(), initSrl(), initRv(), initFs()
         * initFab(), initBv(), initBnv(), initPb()
         */
        fun initView(navFragmentList: ArrayList<NavFragment>)

        /**
         * Start the loading animation before inserting the data.
         */
        fun showPb()

        /**
         * Stop the loading animation after inserting the data.
         */
        fun hidePb()

        /**
         * Hide before each display to meet the material design specification.
         */
        fun showFab(drawableId: Int)

        fun hideFab()

        /**
         * Activity has these views,
         * ConstraintLayout, CoordinatorLayout, AppBarLayout, Toolbar, SearchEditTextLayout,
         * ConstraintLayout, SwipeRefreshLayout, RecyclerView, FastScroller,
         * AnimationFloatingActionButton, BlurView, BottomNavigationView, ProgressBar
         */
        fun deinitView()
    }

    interface INavFragment : AnkoLogger {

        fun initView()

        fun showSrl()

        fun hideSrl()

        /**
         * You have to show the view in init view and invalidate view in the callback.
         */
        fun invalidateRv(bean: Bean)
    }

    /**
     * Write Nav view and presenter separately, first of all you think there are ways to
     * play the Nav business logic
     */
    interface IPresenter : BaseContract.IPresenter<INavActivity> {

        fun init(whichActivity: Int?, navFragment: NavFragment? = null)

        /**
         * Summary of business logic is db's CRUD
         *
         * Create()
         *
         * Delete()
         *
         * Update()
         *
         * Read()
         */

        /**
         * The next view has been pressed
         */
        fun deleteNav(navId: String)

        fun updateNav(gospels: List<Gospels>)

        fun readNav()
    }
}
