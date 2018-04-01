package com.christian.navadapter

import android.support.v7.widget.RecyclerView
import com.christian.BasePresenter
import com.christian.BaseView

class NavItemContract {

    interface View : BaseView<Presenter>, RecyclerView.ViewHolder(itemView) {

    }

    interface Presenter : BasePresenter, RecyclerView.Adapter<NavAdapter.ViewHolder>() {

    }

}