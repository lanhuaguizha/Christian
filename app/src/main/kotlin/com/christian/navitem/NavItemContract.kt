package com.christian.navitem

import com.christian.BasePresenterContract

class NavItemContract {

    interface View : BasePresenterContract.View {

        fun initView(hasElevation: Boolean)

        fun animate(itemView: android.view.View)

    }

    interface Presenter : BasePresenterContract.Presenter<View> {

        fun getTitle(pos: Int): String

    }

}