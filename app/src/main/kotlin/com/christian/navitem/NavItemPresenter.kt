package com.christian.navitem

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.MeBean
import com.christian.data.NavBean
import com.christian.index.TextGetter
import com.christian.nav.*
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
class NavItemPresenter<Bean>(var navs: List<Bean>, private val hasElevation: Boolean = true, private val navId: Int) : NavItemContract.IPresenter, RecyclerView.Adapter<NavItemView>(), TextGetter {

    override lateinit var view: NavItemContract.IView
    private lateinit var navItemView: NavItemView
    private lateinit var ctx: NavActivity
    private val providers = Arrays.asList(AuthUI.IdpConfig.PhoneBuilder().build())

    init {

    }

    override fun getTitle(pos: Int): String {

        return navs[pos].toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavItemView {

        ctx = parent.context as NavActivity
        info { "ctx$ctx" }

        /**
         * First "present = this" init Presenter in constructor, then "navItemView = this" init View in init method
         */
        val itemView: View
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                when (viewType) {
                    VIEW_TYPE_BUTTON -> {
                        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_button, parent, false)
                        itemView.login_nav_item.setOnClickListener {
                            // Create and launch sign-in intent
                            ctx.startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(providers)
                                            .build(),
                                    NavActivity.RC_SIGN_IN)
                        }
                        navItemView = NavItemView(itemView, this, itemView)
                        navItemView.initView(hasElevation)
                        return navItemView
                    }
                    VIEW_TYPE_SMALL -> {
                        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view_small, parent, false)
                        navItemView = NavItemView(itemView, this, itemView)
                        navItemView.initView(hasElevation)
                        return navItemView
                    }
                }
            }
        }
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view, parent, false)
        navItemView = NavItemView(itemView, this, itemView)
        navItemView.initView(hasElevation)
        return navItemView
    }

    override fun getItemCount(): Int {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                return navs.size + 1
            }
        }
        return navs.size
    }

    override fun getItemViewType(position: Int): Int {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                return when (position) {
                    navs.size -> VIEW_TYPE_BUTTON
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
                // 第一次加载不可见，后续invalidate时才可见
                if ((navs[position] as NavBean).subtitle == "") {
                    holder.itemView.visibility = View.GONE
                } else {
                    holder.itemView.visibility = View.VISIBLE
                    holder.tv_subtitle_nav_item.text = (navs[position] as NavBean).subtitle
                    holder.tv_title_nav_item.text = (navs[position] as NavBean).title
                    holder.tv_detail_nav_item.text = (navs[position] as NavBean).detail

                    if (position == 4) {
                        holder.iv_nav_item.image = ResourcesCompat.getDrawable(holder.containerView.resources, R.drawable.the_virgin, holder.containerView.context.theme)
                        holder.iv_nav_item.visibility = View.VISIBLE
                    } else {
                        holder.iv_nav_item.visibility = View.GONE
                    }
                    applyViewHolderAnimation(holder)
                }
            }
            VIEW_ME -> {
                if (holder.tv_nav_item_small != null) holder.tv_nav_item_small.text = (navs[position] as MeBean.Settings).name
                if (holder.tv2_nav_item_small != null) holder.tv2_nav_item_small.text = (navs[position] as MeBean.Settings).desc
                if (holder.iv_nav_item_small != null) {
                    val url = (navs[position] as MeBean.Settings).url
                    Glide.with(ctx).load(generateUrlId(url)).into(holder.iv_nav_item_small)
                }
            }
        }
    }

    private fun generateUrlId(url: String?): Int {
        return when (url) {
            "ic_brightness_medium_black_24dp" -> R.drawable.ic_brightness_medium_black_24dp
            "ic_folder_open_black_24dp" -> R.drawable.ic_folder_open_black_24dp
            "ic_folder_special_black_24dp" -> R.drawable.ic_folder_special_black_24dp
            "R.drawable.ic_settings_black_24dp" -> R.drawable.ic_settings_black_24dp
            "R.drawable.the_virgin" -> R.drawable.the_virgin
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
        return if ((navs[0] as NavBean).subtitle.isNotEmpty()) {
            (navs[pos] as NavBean).subtitle[0].toUpperCase().toString()
        } else {
            ""
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }
}

const val VIEW_TYPE_NORMAL = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_BUTTON = 2