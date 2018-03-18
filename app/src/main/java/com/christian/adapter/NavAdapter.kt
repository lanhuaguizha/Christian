package com.christian.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.christian.R
import com.christian.data.Nav

class NavAdapter(private val navs: List<Nav>) : RecyclerView.Adapter<NavAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_nav, parent, false))
    }

    override fun getItemCount(): Int {
        return navs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.ivNav

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivNav: ImageView = itemView.findViewById(R.id.iv_nav)

    }

}