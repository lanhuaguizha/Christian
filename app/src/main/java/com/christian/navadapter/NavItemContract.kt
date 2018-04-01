package com.christian.navadapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.christian.BasePresenter
import com.christian.BaseView
import com.christian.R

class NavItemContract {

    abstract class View(itemView: android.view.View) : BaseView<Presenter>, RecyclerView.ViewHolder(itemView) {

        val cv_nav_item = itemView.findViewById<CardView>(R.id.cv_nav_item)

        val cl_nav_item = itemView.findViewById<ConstraintLayout>(R.id.cl_nav_item)

        val iv_nav_item = itemView.findViewById<ImageView>(R.id.iv_nav_item)

        val tv_subtitle_nav_item = itemView.findViewById<TextView>(R.id.tv_subtitle_nav_item)

        val tv_title_nav_item = itemView.findViewById<TextView>(R.id.tv_title_nav_item)

        val tv_detail_nav_item = itemView.findViewById<TextView>(R.id.tv_detail_nav_item)

        val ib_nav_item = itemView.findViewById<AppCompatImageButton>(R.id.ib_nav_item)

    }

    abstract class Presenter : BasePresenter, RecyclerView.Adapter<View>() {

        /**
         * NavItemView late init after NavItemPresenter, log is below,
         * 03-31 15:27:55.635 2605-2605/com.christian I/NavItemPresenter: init
         * 03-31 15:27:55.721 2605-2611/com.christian I/zygote64: Compiler allocated 7MB to compile void android.widget.TextView.<init>(android.content.Context, android.util.AttributeSet, int, int)
         * 03-31 15:27:55.883 2605-2635/com.christian I/OpenGLRenderer: Initialized EGL, version 1.4
         * 03-31 15:27:55.939 2605-2605/com.christian I/NavItemView: init
         */
        lateinit var navItemView: NavItemView

    }

}