package com.christian.navadapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.christian.R
import com.christian.data.Nav
import kotlinx.android.synthetic.main.nav_item_view.*

/**
 * NavItemPresenter/Adapter is business logic of nav items.
 */
class NavItemPresenter(var navs: List<Nav>, private val hasElevation: Boolean = true) : NavItemContract.Presenter, RecyclerView.Adapter<NavItemView>() {

    private lateinit var navItemView: NavItemView

    init {

        Log.i("NavItemPresenter", "init")
    }

    override fun start() {

    }

    override fun getTitle(pos: Int): String {

        return navs[pos].title

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemView {

        /**
         * First "present = this" init Presenter in constructor, then "navItemView = this" init View in init method
         */
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view, parent, false)
        navItemView = NavItemView(itemView, this, hasElevation, itemView)

        return navItemView

    }

    override fun getItemCount(): Int {
        return navs.size
    }

    override fun onBindViewHolder(holder: NavItemView, position: Int) {

        navItemView.animate(holder.itemView)

        holder.tv_subtitle_nav_item.text = navs[position].subtitle

        holder.tv_title_nav_item.text = navs[position].title

        holder.tv_detail_nav_item.text = navs[position].detail

    }
}