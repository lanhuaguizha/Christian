package com.christian.navadapter

import android.content.Intent
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.christian.R
import com.christian.navdetail.NavDetailActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nav_item_view.*

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

class NavItemView(itemView: View, override var presenter: NavItemContract.Presenter, hasElevation: Boolean, override val containerView: View) : NavItemContract.View, RecyclerView.ViewHolder(itemView), LayoutContainer {

    init {

        cv_nav_item.hasElevation = hasElevation

        itemView.setOnClickListener {

            val i = Intent(itemView.context, NavDetailActivity::class.java)

            i.putExtra("title", presenter.getTitle(adapterPosition))

            itemView.context.startActivity(i)

        }

        itemView.findViewById<AppCompatImageButton>(R.id.ib_nav_item).setOnClickListener { v: View -> showPopupMenu(v) }

    }

    override fun setCvRadius(radius: Float) {

        cv_nav_item.radius = radius

    }

    private fun showPopupMenu(v: View) {

        val popupMenu = PopupMenu(v.context, v)
        popupMenu.gravity = Gravity.END

        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { false }
        popupMenu.show()

    }

}