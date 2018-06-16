package com.christian.navadapter

import android.content.Intent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import com.christian.R
import com.christian.navdetail.NavDetailActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nav_item_view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp
import org.jetbrains.anko.textColor

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

class NavItemView(itemView: View, override var presenter: NavItemContract.Presenter, override val containerView: View) : NavItemContract.View, RecyclerView.ViewHolder(itemView), LayoutContainer {

    init {

        itemView.setOnClickListener {

            val i = Intent(itemView.context, NavDetailActivity::class.java)

            i.putExtra("title", presenter.getTitle(adapterPosition))

            itemView.context.startActivity(i)

        }

        itemView.findViewById<AppCompatImageButton>(R.id.ib_nav_item).setOnClickListener { v: View -> showPopupMenu(v) }

    }

    override fun initView(hasElevation: Boolean) {

        if (hasElevation) {
        } else {
            cv_nav_item.hasElevation = hasElevation
            cv_nav_item.radius = 0f
            cv_nav_item.foreground = null
            tv_subtitle_nav_item.visibility = View.GONE
            tv_title_nav_item.visibility = View.GONE
            tv_detail_nav_item.textColor = ResourcesCompat.getColor(itemView.resources, R.color.text_color_primary, itemView.context.theme)
            tv_detail_nav_item.textSize = 18f
            tv_detail_nav_item.maxLines = Integer.MAX_VALUE
        }
    }

    private fun showPopupMenu(v: View) {

        val popupMenu = PopupMenu(v.context, v)
        popupMenu.gravity = Gravity.END

        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { false }
        popupMenu.show()

    }

    override fun animate(itemView: View) {

        val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.translate_in_right)

        itemView.startAnimation(animation)

    }

}