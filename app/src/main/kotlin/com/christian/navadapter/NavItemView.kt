package com.christian.navadapter

import android.content.Intent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.View
import com.christian.R
import com.christian.navdetail.NavDetailActivity
import kotlinx.android.synthetic.main.nav_item_view.*

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

class NavItemView(itemView: View, override var presenter: NavItemContract.Presenter, hasElevation: Boolean) : NavItemContract.View(itemView) {

    init {

        cv_nav_item.hasElevation = hasElevation

        presenter.navItemView = this

        itemView.setOnClickListener {

            val i = Intent(itemView.context, NavDetailActivity::class.java)

            i.putExtra("title", presenter.getTitle(adapterPosition))

            itemView.context.startActivity(i)

        }

        itemView.findViewById<AppCompatImageButton>(R.id.ib_nav_item).setOnClickListener { v: View -> showPopupMenu(v) }

    }

    private fun showPopupMenu(v: View) {

        val popupMenu = PopupMenu(v.context, v)
        popupMenu.gravity = Gravity.END

        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { false }
        popupMenu.show()

    }

}