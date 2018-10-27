package com.christian.navitem

import com.christian.BasePresenterContract

class NavItemContract {

    interface View : BasePresenterContract.View<Presenter> {

        fun initView(hasElevation: Boolean)

        fun animate(itemView: android.view.View)

    }

    interface Presenter : BasePresenterContract.Presenter {

        fun getTitle(pos: Int): String

    }

}