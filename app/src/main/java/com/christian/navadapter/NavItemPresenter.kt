package com.christian.navadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.christian.R
import com.christian.data.Nav

/**
 * NavItemPresenter/Adapter is business logic of nav items.
 */
class NavItemPresenter(var navs: List<Nav>) : NavItemContract.Presenter() {

    init {
//        navItemView.presenter = this
        Log.i("NavItemPresenter", "init")
    }

    override fun start() {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemContract.View {

        /**
         * First "present = this" init Presenter in constructor, then "navItemView = this" init View in init method
         */
        return NavItemView(LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view, parent, false), this)

    }

    override fun getItemCount(): Int {
        return navs.size
    }

    override fun onBindViewHolder(holder: NavItemContract.View, position: Int) {

        holder.tv_subtitle_nav_item.text = navs[position].subtitle

        holder.tv_title_nav_item.text = navs[position].title

        holder.tv_detail_nav_item.text = navs[position].detail

    }

}