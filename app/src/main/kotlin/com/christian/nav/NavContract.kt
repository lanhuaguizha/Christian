package com.christian.nav

import com.christian.BaseContract
import com.christian.data.Gospel
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

        fun showFab()

        fun hideFab()
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

        fun updateNav(gospels: List<Gospel>)

        fun readNav()
    }
}
