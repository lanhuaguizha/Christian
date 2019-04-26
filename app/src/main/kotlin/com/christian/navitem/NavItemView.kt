package com.christian.navitem

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.Gospels
import com.christian.data.MeBean
import com.christian.nav.toolbarTitle
import com.christian.navdetail.NavDetailActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nav_item_gospel.*

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

open class NavItemView(final override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

//    override fun onCreateView(parent: ViewGroup, viewType: Int, itemView: View): NavItemView {
//        initView()
//        return NavItemView(presenter, itemView, navActivity)
//    }

    init {
        containerView.setOnClickListener {

        }
//        containerView.login_nav_item.setOnClickListener {
//            // Create and launch sign-in intent
//            (presenter as NavItemPresenter).ctx.startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(providers)
//                            .build(),
//                    NavActivity.RC_SIGN_IN)
//        }

//        when (adapterPosition) {
//            1 -> {
//                itemView.setOnClickListener {
//                    if (isOn) {
//                        itemView.findViewById<Switch>(R.id.switch_nav_item_small).isChecked = false
//                        isOn = false
//                    } else {
//                        itemView.findViewById<Switch>(R.id.switch_nav_item_small).isChecked = true
//                        isOn = true
//                    }
//                }
//            }
//            else -> {
//                itemView.setOnClickListener {
//                    val i = Intent(itemView.context, NavDetailActivity::class.java)
//                    i.putExtra(toolbarTitle, presenter.getTitle(adapterPosition))
//                    itemView.context.startActivity(i)
//                }
//            }
//        }
//        itemView.findViewById<AppCompatImageButton>(R.id.ib_nav_item).setOnClickListener { v: View -> showPopupMenu(v) }

    }

    fun initView() {
//        cv_nav_item.radius = 0f
//        cv_nav_item.foreground = null
//        tv_subtitle_nav_item.visibility = View.GONE
//        tv_title_nav_item.visibility = View.GONE
//        tv_detail_nav_item.textColor = ResourcesCompat.getColor(itemView.resources, R.color.text_color_primary, itemView.context.theme)
//        tv_detail_nav_item.textSize = 18f
//        tv_detail_nav_item.maxLines = Integer.MAX_VALUE
    }

    fun animateItemView(itemView: View) {

        val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.up_from_bottom)

        itemView.startAnimation(animation)

    }

    fun clearItemAnimation(itemView: View) {
        itemView.clearAnimation()
    }

    fun bind(gospels: Gospels) {
        tv_title_nav_item.text = gospels.title
        tv_subtitle_nav_item.text = gospels.subtitle
        if (gospels.gospelDetails.isNotEmpty()) {
            Glide.with(containerView.context).load(gospels.gospelDetails[0].image).into(iv_nav_item)
            tv_detail_nav_item.text = gospels.gospelDetails[0].content
        }
        containerView.setOnClickListener {
            startGospelDetailActivity(gospels)
        }
        tv_title_nav_item.setOnClickListener {
            //            gospelId = gospels.id
            startGospelDetailActivity(gospels)
        }
    }

    private fun startGospelDetailActivity(gospels: Gospels) {
        val intent = Intent(containerView.context, NavDetailActivity::class.java)
        intent.putExtra(toolbarTitle, gospels.title)
        containerView.context.startActivity(intent)
    }

    fun bind(snapshot: MeBean) {
    }
}