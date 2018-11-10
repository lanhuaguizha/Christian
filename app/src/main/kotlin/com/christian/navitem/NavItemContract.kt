package com.christian.navitem

import com.christian.BaseContract

class NavItemContract {

    interface IView : BaseContract.IView<IPresenter> {

        fun initView(hasElevation: Boolean)

        fun animate(itemView: android.view.View)

    }

    interface IPresenter : BaseContract.IPresenter<IView> {

        fun getTitle(pos: Int): String

    }

}