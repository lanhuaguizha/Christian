package com.christian.navitem

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.data.Nav
import com.christian.index.TextGetter
import kotlinx.android.synthetic.main.nav_item_view.*
import org.jetbrains.anko.image

/**
 * NavItemPresenter/Adapter is business logic of nav items.
 */
class NavItemPresenter(var navs: List<Nav>, private val hasElevation: Boolean = true) : NavItemContract.Presenter, RecyclerView.Adapter<NavItemView>(), TextGetter {

    private lateinit var navItemView: NavItemView
    private val mContentGravityCenter = "<TEXTFORMAT LEADING=\"2\"><P ALIGN=\"CENTER\"><FONT FACE=\"Microsoft Yahei,微软雅黑\" SIZE=\"24\" COLOR=\"#333333\" LETTERSPACING=\"0\" KERNING=\"0\">我先来个居中对齐!</FONT></P></TEXTFORMAT>"
    private val mContentGravityRight = "<TEXTFORMAT LEADING=\"2\"><P ALIGN=\"RIGHT\"><FONT FACE=\"Microsoft Yahei,微软雅黑\" SIZE=\"24\" COLOR=\"#333333\" LETTERSPACING=\"0\" KERNING=\"0\">我是来右对齐的!</FONT></P></TEXTFORMAT>"
    private val mContentStyle = "<TEXTFORMAT LEADING=\"2\">" +
            "<P ALIGN=\"LEFT\">" +
            "<FONT FACE=\"Microsoft Yahei,微软雅黑\" SIZE=\"24\" COLOR=\"#333333\" LETTERSPACING=\"2\" KERNING=\"0\">" +
            "我可以设置很多不同的字体风格,比如:<B>加粗</B>、<I>斜体</I>、<U>下划线</U>。" +
            "</FONT>" +
            "<FONT FACE=\"Microsoft Yahei,微软雅黑\" SIZE=\"22\" COLOR=\"#2196f3\" LETTERSPACING=\"0\" KERNING=\"0\">" +
            "<FONT SIZE=\"30\">我是很大的字</FONT>" +
            "我居然比旁边的字小" +
            "<FONT SIZE=\"12\">我最小...啊啊啊</FONT>" +
            "</FONT>" +
            "</P>" +
            "</TEXTFORMAT>"
    private val mContentUrl = "<TEXTFORMAT LEADING=\"2\"><P ALIGN=\"LEFT\"><FONT FACE=\"Microsoft Yahei,微软雅黑\" SIZE=\"24\" COLOR=\"#333333\" LETTERSPACING=\"0\" KERNING=\"0\">我可以设置一个超链接,牛逼吗 <FONT COLOR=\"#0000ff\"><A HREF=\"我是超链接\" TARGET=\"_blank\"><FONT COLOR=\"#6698ff\"><U>快戳我看看</U></FONT></A></FONT></FONT></P></TEXTFORMAT>"

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
        navItemView = NavItemView(itemView, this, itemView)

        navItemView.initView(hasElevation)

        return navItemView

    }

    override fun getItemCount(): Int {
        return navs.size
    }

    override fun onBindViewHolder(holder: NavItemView, position: Int) {

        if (navs[position].subtitle == "") {

            holder.itemView.visibility = View.GONE

        } else {

            holder.itemView.visibility = View.VISIBLE

            if (hasElevation) {
                navItemView.animate(holder.itemView)
            }

            holder.tv_subtitle_nav_item.text = navs[position].subtitle

            holder.tv_title_nav_item.text = navs[position].title

//            holder.tv_detail_nav_item.loadContent(mContentStyle)
            holder.tv_detail_nav_item.text = navs[position].detail

            if (position == 3) {
                holder.iv_nav_item.image = ResourcesCompat.getDrawable(holder.containerView.resources, R.drawable.the_virgin, holder.containerView.context.theme)
                holder.iv_nav_item.visibility = View.VISIBLE
            } else{
                holder.iv_nav_item.visibility = View.GONE
            }

        }

    }

    override fun getTextFromAdapter(pos: Int): String {
        return if (navs[0].subtitle.isNotEmpty()) {
            navs[pos].subtitle[0].toUpperCase().toString()
        } else {
            ""
        }
    }
}