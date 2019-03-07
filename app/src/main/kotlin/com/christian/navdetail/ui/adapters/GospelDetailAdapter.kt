package com.christian.navdetail.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.data.GospelDetail

/**
 * Adapter for the RecyclerView in GospelDetailFragment
 */
class GospelDetailAdapter(val gospelId: Int, private val gospelDetailList: List<GospelDetail>) : RecyclerView.Adapter<GospelDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gospel_detail_title, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        when(position) {
            0 -> VIEW_TYPE_GOSPEL_DETAIL_TITLE
            gospelDetailList.size - 2 -> VIEW_TYPE_GOSPEL_DETAIL_AMEN
            gospelDetailList.size - 1 -> VIEW_TYPE_GOSPEL_DETAIL_AUTHOR
            gospelDetailList.size -> VIEW_TYPE_GOSPEL_DETAIL_DATE
        }
        return VIEW_TYPE_GOSPEL_DETAIL_CONTENT
    }

    override fun getItemCount(): Int {
        return gospelDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    companion object {
        const val VIEW_TYPE_GOSPEL_DETAIL_TITLE = 0
        const val VIEW_TYPE_GOSPEL_DETAIL_CONTENT = 1
        const val VIEW_TYPE_GOSPEL_DETAIL_AMEN = 2
        const val VIEW_TYPE_GOSPEL_DETAIL_AUTHOR = 3
        const val VIEW_TYPE_GOSPEL_DETAIL_DATE = 4
    }

}