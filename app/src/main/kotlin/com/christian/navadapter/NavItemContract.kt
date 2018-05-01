package com.christian.navadapter

import com.christian.BasePresenter
import com.christian.BaseView

class NavItemContract {

    interface View : BaseView<Presenter> {

        fun setCvRadius(radius: Float)

    }

    interface Presenter : BasePresenter {

        fun getTitle(pos: Int): String

    }

}