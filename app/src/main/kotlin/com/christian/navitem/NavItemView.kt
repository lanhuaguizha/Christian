package com.christian.navitem

import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.christian.R
import com.christian.nav.NavActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nav_item_view.*
import org.jetbrains.anko.textColor

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

open class NavItemView(override var presenter: NavItemContract.IPresenter, final override val containerView: View, private val navActivity: NavActivity) : NavItemContract.IView, RecyclerView.ViewHolder(containerView), LayoutContainer {

    override fun onCreateView(parent: ViewGroup, viewType: Int, itemView: View): NavItemView {
        initView()
        return NavItemView(presenter, itemView, navActivity)
    }

    override fun updateUserUI(currentUser: FirebaseUser?) {
    }

    init {
        containerView.isLongClickable = true

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

    override fun initView() {
//        cv_nav_item.radius = 0f
//        cv_nav_item.foreground = null
//        tv_subtitle_nav_item.visibility = View.GONE
//        tv_title_nav_item.visibility = View.GONE
//        tv_detail_nav_item.textColor = ResourcesCompat.getColor(itemView.resources, R.color.text_color_primary, itemView.context.theme)
//        tv_detail_nav_item.textSize = 18f
//        tv_detail_nav_item.maxLines = Integer.MAX_VALUE
    }

    override fun deinitView() {
    }

    override fun animateItemView(itemView: View) {

        val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.up_from_bottom)

        itemView.startAnimation(animation)

    }

    fun clearItemAnimation(itemView: View) {
        itemView.clearAnimation()
    }

}