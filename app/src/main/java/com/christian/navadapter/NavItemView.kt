package com.christian.navadapter

import android.util.Log
import android.view.View

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

class NavItemView(itemView: View, override var presenter: NavItemContract.Presenter) : NavItemContract.View(itemView) {

    init {
        presenter.navItemView = this
    }

}