package com.christian.navitem

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.Gospels
import com.christian.nav.NavActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.nav_item_gospel.*
import java.util.*

/**
 * NavItemView/NavItemHolder is view logic of nav items.
 */

open class NavItemView(override var presenter: NavItemContract.IPresenter, final override val containerView: View, private val navActivity: NavActivity) : NavItemContract.IView, androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {

    override fun onCreateView(parent: ViewGroup, viewType: Int, itemView: View): NavItemView {
        initView()
        return NavItemView(presenter, itemView, navActivity)
    }

    override fun updateUserUI(currentUser: FirebaseUser?) {
    }

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

    fun bind(snapshot: DocumentSnapshot, listener: NavItemPresenter.OnGospelSelectedListener) {
        containerView.postDelayed({
            val gospels = snapshot.toObject(Gospels::class.java)
        }, 3000)
        Glide.with(containerView.context).load(R.drawable.the_virgin).into(iv_nav_item)
        tv_title_nav_item.text = snapshot.data?.get("title").toString()
        tv_subtitle_nav_item.text = snapshot.data?.get("subtitle").toString()
        tv_detail_nav_item.text = ((snapshot.data?.get("detail") as java.util.ArrayList<*>)[0] as HashMap<*, *>)["content"].toString()
        containerView.setOnClickListener {
            listener.onGospelSelected(snapshot)
        }
        tv_title_nav_item.setOnClickListener { listener.onGospelSelected(snapshot) }
    }
}