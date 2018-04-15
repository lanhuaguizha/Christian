package com.christian.navadapter

import android.content.Intent
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.View
import com.christian.R
import com.christian.navdetail.NavDetailActivity

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

class NavItemView(itemView: View, override var presenter: NavItemContract.Presenter) : NavItemContract.View(itemView) {

    init {

        presenter.navItemView = this

        itemView.setOnClickListener { itemView.context.startActivity(Intent(itemView.context, NavDetailActivity::class.java)) }

        itemView.findViewById<AppCompatImageButton>(R.id.ib_nav_item).setOnClickListener { v: View ->  showPopupMenu(v) }

    }

    private fun showPopupMenu(v: View) {

        val popupMenu = PopupMenu(v.context, v)
        popupMenu.gravity = Gravity.END

        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { false }
        popupMenu.show()

    }

}