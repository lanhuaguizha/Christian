package com.christian.navitem

import android.content.Intent
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatImageButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.MeBean
import com.christian.data.NavBean
import com.christian.index.TextGetter
import com.christian.nav.*
import com.christian.navdetail.NavDetailActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.nav_item_view.*
import kotlinx.android.synthetic.main.nav_item_view_button.view.*
import kotlinx.android.synthetic.main.nav_item_view_small.*
import org.jetbrains.anko.image
import org.jetbrains.anko.info
import java.util.*

/**
 * NavItemPresenter/Adapter is business logic of nav items.
 */
class NavItemPresenter<Bean>(var navs: Bean, private val hasElevation: Boolean = true, private val navId: Int) : NavItemContract.IPresenter, RecyclerView.Adapter<NavItemView>(), TextGetter {
    override lateinit var view: NavItemContract.IView

    private lateinit var navItemView: NavItemView
    private lateinit var navActivity: NavActivity
    private val providers = Arrays.asList(AuthUI.IdpConfig.PhoneBuilder().build())
    private var isOn = false

    companion object {
        const val RC_SIGN_IN = 0
    }

    // 创建账号
    override fun createUser() {
        // Create and launch sign-in intent
        navActivity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    // 退出账号
    override fun updateUser() {
        AuthUI.getInstance()
                .signOut(navActivity)
                .addOnCompleteListener {
                    // ...
                }
    }

    // 删除账号
    override fun deleteUser() {
        AuthUI.getInstance()
                .delete(navActivity)
                .addOnCompleteListener {
            // ...
        }
    }

    override fun getTitle(pos: Int): String {

        return when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
                val navBean = navs as List<NavBean>
                navBean[pos].title
            }
            VIEW_ME -> {
                info { "pos$pos" }
                when (pos) {
                    0 -> "我的"
                    in 1..4 -> {
                        val meBean = navs as MeBean
                        meBean.settings[pos - 1].name
                    }
                    else -> ""
                }
            }
            else -> {
                return ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemView {

        navActivity = parent.context as NavActivity
        info { "navActivity$navActivity" }

        /**
         * First "present = this" init Presenter in constructor, then "navItemView = this" init View in init method
         */
        val itemView: View
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                when (viewType) {
                    VIEW_TYPE_PORTRAIT -> {
                        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_potrait, parent, false)
                        navItemView = NavItemView(itemView, this, itemView, navActivity)
                        navItemView.initView(hasElevation)
                        return navItemView
                    }
                    VIEW_TYPE_SMALL -> {
                        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_small, parent, false)
                        navItemView = NavItemView(itemView, this, itemView, navActivity)
                        navItemView.initView(hasElevation)
                        return navItemView
                    }
                    VIEW_TYPE_BUTTON -> {
                        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_button, parent, false)
                        itemView.login_nav_item.setOnClickListener {
                            createUser()
                        }
                        navItemView = NavItemView(itemView, this, itemView, navActivity)
                        navItemView.initView(hasElevation)
                        return navItemView
                    }
                }
            }
        }
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view, parent, false)
        navItemView = NavItemView(itemView, this, itemView, navActivity)
        navItemView.initView(hasElevation)
        return navItemView
    }

    override fun getItemCount(): Int {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                return (navs as MeBean).settings.size + 2
            }
        }
        return (navs as List<NavBean>).size
    }

    override fun getItemViewType(position: Int): Int {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                return when (position) {
                    0 -> VIEW_TYPE_PORTRAIT
                    (navs as MeBean).settings.size + 1 -> VIEW_TYPE_BUTTON
                    else -> VIEW_TYPE_SMALL
                }
            }
        }
        return VIEW_TYPE_NORMAL
    }

    private var mPosition: Int = -1

    override fun onBindViewHolder(holder: NavItemView, position: Int) {

        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
                holder.containerView.setOnClickListener {
                    val i = Intent(holder.containerView.context, NavDetailActivity::class.java)
                    i.putExtra(toolbarTitle, getTitle(holder.adapterPosition))
                    holder.containerView.context.startActivity(i)
                }
                holder.containerView.findViewById<AppCompatImageButton>(R.id.ib_nav_item).setOnClickListener { v: View ->
                    navItemView.showPopupMenu(v)
                }

                // 第一次加载不可见，后续invalidate时才可见
                if ((navs as List<NavBean>)[position].subtitle == "") {
                    holder.itemView.visibility = View.GONE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                    holder.tv_subtitle_nav_item.text = (navs as List<NavBean>)[position].subtitle
                    holder.tv_title_nav_item.text = (navs as List<NavBean>)[position].title
                    holder.tv_detail_nav_item.text = (navs as List<NavBean>)[position].detail

                    if (position == 7) {
                        holder.iv_nav_item.image = ResourcesCompat.getDrawable(holder.containerView.resources, R.drawable.the_virgin, holder.containerView.context.theme)
                        holder.iv_nav_item.visibility = View.VISIBLE
                    } else {
                        holder.iv_nav_item.visibility = View.GONE
                    }
                    applyViewHolderAnimation(holder)
                }
            }
            VIEW_ME -> {
                when (holder.adapterPosition) {
                    1 -> {
                        holder.containerView.setOnClickListener {
                            if (isOn) {
                                holder.containerView.findViewById<Switch>(R.id.switch_nav_item_small).isChecked = false
                                isOn = false
                            } else {
                                holder.containerView.findViewById<Switch>(R.id.switch_nav_item_small).isChecked = true
                                isOn = true
                            }
                        }
                    }
                    else -> {
                        holder.containerView.setOnClickListener {
                            val i = Intent(holder.containerView.context, NavDetailActivity::class.java)
                            i.putExtra(toolbarTitle, getTitle(holder.adapterPosition))
                            holder.containerView.context.startActivity(i)
                        }
                    }
                }

                info { "VIEW_ME position$position" }
                if (position == 0) {
                    if (holder.tv_nav_item_small != null) holder.tv_nav_item_small.text = (navs as MeBean).name
                    if (holder.tv2_nav_item_small != null) {
                        applyMarqueeEffect(holder.tv2_nav_item_small)
                        holder.tv2_nav_item_small.text = (navs as MeBean).nickName
                    }
                    if (holder.iv_nav_item_small != null) {
                        Glide.with(navActivity).load(generateUrlId((navs as MeBean).url)).into(holder.iv_nav_item_small)
                    }
                } else if (position in 1..position && (navs as MeBean).settings.isNotEmpty()) {
                    if (holder.switch_nav_item_small != null && position == 1) holder.switch_nav_item_small.visibility = View.VISIBLE
                    if (holder.tv_nav_item_small != null) holder.tv_nav_item_small.text = (navs as MeBean).settings[position - 1].name
                    if (holder.tv2_nav_item_small != null) holder.tv2_nav_item_small.text = (navs as MeBean).settings[position - 1].desc
                    if (holder.iv_nav_item_small != null) {
                        val url = (navs as MeBean).settings[position - 1].url
                        Glide.with(navActivity).load(generateUrlId(url)).into(holder.iv_nav_item_small)
                    }
                }
            }
        }
    }

    private fun generateUrlId(url: String?): Int {
        return when (url) {
            "R.drawable.me" -> R.drawable.me
            "R.drawable.the_virgin" -> R.drawable.the_virgin
            "ic_brightness_medium_black_24dp" -> R.drawable.ic_brightness_medium_black_24dp
            "ic_folder_open_black_24dp" -> R.drawable.ic_folder_open_black_24dp
            "ic_folder_special_black_24dp" -> R.drawable.ic_folder_special_black_24dp
            "ic_crop_free_black_24dp" -> R.drawable.ic_crop_free_black_24dp
            "ic_wallpaper_black_24dp" -> R.drawable.ic_wallpaper_black_24dp
            "R.drawable.ic_settings_black_24dp" -> R.drawable.ic_settings_black_24dp
            else -> {
                return 0
            }
        }
    }

    private fun applyViewHolderAnimation(holder: NavItemView) {
        info { "mPosition$mPosition" }
        if (hasElevation && holder.adapterPosition > mPosition) {
            navItemView.animate(holder.itemView)
        } else {
            navItemView.clearAnimate(holder.itemView)
        }
        mPosition = holder.adapterPosition
    }

    override fun getTextFromAdapter(pos: Int): String {
        return if ((navs as List<NavBean>)[0].subtitle.isNotEmpty()) {
            (navs as List<NavBean>)[pos].subtitle[0].toUpperCase().toString()
        } else {
            ""
        }
    }

}

const val VIEW_TYPE_NORMAL = 0
const val VIEW_TYPE_PORTRAIT = 1
const val VIEW_TYPE_SMALL = 2
const val VIEW_TYPE_BUTTON = 3
