package com.christian.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ContextMenu
import android.view.View

class ContextMenuRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private var contextMenuInfo: ContextMenuInfo? = null

    override fun getContextMenuInfo() = contextMenuInfo

    override fun showContextMenu(): Boolean {
        return super.showContextMenu()
    }

    override fun showContextMenuForChild(originalView: View): Boolean {
        saveContextMenuInfo(originalView)
        return super.showContextMenuForChild(originalView)
    }

    override fun showContextMenuForChild(originalView: View, x: Float, y: Float): Boolean {
        saveContextMenuInfo(originalView)
        return super.showContextMenuForChild(originalView, x, y)
    }

    private fun saveContextMenuInfo(originalView: View) {
        val position = getChildAdapterPosition(originalView)
        val longId = getChildItemId(originalView)
        contextMenuInfo = ContextMenuInfo(this, originalView, position, longId)
    }

    class ContextMenuInfo(
            val recyclerView: RecyclerView,
            val itemView: View,
            val position: Int,
            val id: Long
    ) : ContextMenu.ContextMenuInfo

}