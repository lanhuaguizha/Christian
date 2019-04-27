package com.christian.navitem

import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.Disciple
import com.christian.data.Gospel
import com.christian.data.MeBean
import com.christian.data.Setting
import com.christian.nav.toolbarTitle
import com.christian.navdetail.NavDetailActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nav_item_gospel.*
import kotlinx.android.synthetic.main.nav_item_me.*
import kotlinx.android.synthetic.main.notification_template_custom_big.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

open class NavItemView(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer, AnkoLogger {

    init {
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

    fun bind(gospel: Gospel) {
        tv_title_nav_item.text = gospel.title
        tv_subtitle_nav_item.text = gospel.subtitle
        if (gospel.detail.isNotEmpty()) {
            Glide.with(containerView.context).load(gospel.detail[0].image).into(iv_nav_item)
            tv_detail_nav_item.text = gospel.detail[0].content
        }
        containerView.setOnClickListener {
            startGospelDetailActivity(gospel)
        }
        tv_title_nav_item.setOnClickListener {
            //            gospelId = gospel.id
            startGospelDetailActivity(gospel)
        }
    }

    private fun startGospelDetailActivity(gospel: Gospel) {
        val intent = Intent(containerView.context, NavDetailActivity::class.java)
        intent.putExtra(toolbarTitle, gospel.title)
        containerView.context.startActivity(intent)
    }

    private var isOn = false
    fun bind(setting: Setting) {
       /* when (adapterPosition) {
            1 -> {
                containerView.setOnClickListener {
                    if (isOn) {
                        containerView.findViewById<Switch>(R.id.switch_nav_item_small).isChecked = false
                        // 恢复应用默认皮肤
//                                SkinCompatManager.getInstance().restoreDefaultTheme()
                        isOn = false
                    } else {
                        containerView.findViewById<Switch>(R.id.switch_nav_item_small).isChecked = true
//                                SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
                        isOn = true
                    }
                }
            }
            else -> {
                containerView.setOnClickListener {
                    val i = Intent(containerView.context, NavDetailActivity::class.java)
                    i.putExtra(toolbarTitle, getTitle(meBean, adapterPosition))
                    containerView.context.startActivity(i)
                }
            }
        }*/

       /* if (adapterPosition == 0) {
            tv_nav_item_small.text = meBean.name
            applyMarqueeEffect(tv2_nav_item_small)
            tv2_nav_item_small.text = meBean.nickName
            Glide.with(containerView.context).load(meBean.url).into(iv_nav_item_small)
        } else if (adapterPosition in 1..adapterPosition && meBean.settings.isNotEmpty()) {
            if (adapterPosition == 1) switch_nav_item_small.visibility = View.VISIBLE
            tv_nav_item_small.text = meBean.settings[adapterPosition - 1].name
            tv2_nav_item_small.text = meBean.settings[adapterPosition - 1].desc
            val url = meBean.settings[adapterPosition - 1].url
            Glide.with(containerView.context).load(url).into(iv_nav_item_small)
        }*/

        tv_nav_item_small.text = setting.name
        tv2_nav_item_small.text = setting.desc
        Glide.with(containerView.context).load(setting.url).into(iv_nav_item_small)
    }

    private fun getTitle(meBean: MeBean, pos: Int): String {

//        return when (navId) {
//            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
//                snapshots[pos].data?.get("subtitle").toString()
//            }
//            VIEW_ME -> {
        return when (pos) {
            0 -> "我的"
            in 1..4 -> {
                meBean.settings[pos - 1].name
            }
            else -> ""
        }
//            }
//            else -> {
//                return ""
//            }
    }

    fun bind(disciple: Disciple) {
        tv_title_nav_item.text = disciple.id
        tv_subtitle_nav_item.text = disciple.name
    }

}