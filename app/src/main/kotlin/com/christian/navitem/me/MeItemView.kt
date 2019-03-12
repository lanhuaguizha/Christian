package com.christian.navitem.me

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.nav.NavActivity
import com.christian.navitem.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.nav_item_view_button.view.*
import org.jetbrains.anko.info
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.topPadding

/**
 * 继承NavItemView通用的父类，分化出GospelItemView、DiscipleItemView、MeItemView
 */
class MeItemView(presenter: NavItemContract.IPresenter, containerView: View, navActivity: NavActivity) : NavItemView(presenter, containerView, navActivity) {

    override fun onCreateView(parent: ViewGroup, viewType: Int, itemView: View): NavItemView {
        info { "onCreateView" }
        var meItemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view, parent, false)
        when (viewType) {
            VIEW_TYPE_PORTRAIT -> {
                meItemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_potrait, parent, false)
            }
            VIEW_TYPE_SMALL -> {
                meItemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_small, parent, false)
            }
            VIEW_TYPE_BUTTON -> {
                meItemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_button, parent, false)
                info { "left${meItemView.login_nav_item.leftPadding}top${meItemView.login_nav_item.topPadding}right${meItemView.login_nav_item.rightPadding}" }
                meItemView.login_nav_item.setOnClickListener {
                    presenter.createUser()
                }
            }
        }
        return super.onCreateView(parent, viewType, meItemView)
    }

    override fun updateUserUI(currentUser: FirebaseUser?) {
    }

    /**
     * 回收掉MeItemView
     */
    override fun deinitView() {

    }
}