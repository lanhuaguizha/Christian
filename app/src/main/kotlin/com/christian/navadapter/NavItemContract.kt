package com.christian.navadapter

import com.christian.BasePresenter
import com.christian.BaseView

class NavItemContract {

    interface View : BaseView<Presenter> {

        fun setCvRadius(radius: Float)

        fun animate(itemView: android.view.View)

    }

    interface Presenter : BasePresenter {

        fun getTitle(pos: Int): String

    }

}