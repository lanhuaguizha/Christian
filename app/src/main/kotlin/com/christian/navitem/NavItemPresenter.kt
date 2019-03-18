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
import com.christian.gospeldetail.NavDetailActivity
import com.christian.nav.*
import com.christian.navitem.me.MeItemView
import com.christian.util.ChristianUtil
import com.christian.view.ContextMenuRecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.nav_item_view.*
import kotlinx.android.synthetic.main.nav_item_view_small.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.image
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * NavItemPresenter/Adapter is business logic of nav items.
 */
abstract class NavItemPresenter<Bean>(private var query: Query, private val listener: OnGospelSelectedListener, var navs: Bean, private val navId: Int) : NavItemContract.IPresenter, RecyclerView.Adapter<NavItemView>(), EventListener<QuerySnapshot> {
    private val snapshots = ArrayList<DocumentSnapshot>()
    private var registration: ListenerRegistration? = null

    abstract fun onDataChanged()
    abstract fun onError(e: FirebaseFirestoreException)

    fun setQuery(query: Query) {
        // Stop listening
        stopListening()

        // Clear existing data
        snapshots.clear()
        notifyDataSetChanged()

        // Listen to new query
        this.query = query
        startListening()
    }

    fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        notifyDataSetChanged()
    }

    override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            warn { "onEvent:error$e" }
            onError(e)
            return
        }

        if (documentSnapshots == null) {
            return
        }

        // Dispatch the event
        debug { "onEvent:numChanges:${documentSnapshots.documentChanges.size}" }
        for (change in documentSnapshots.documentChanges) {
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }

        onDataChanged()
    }
    private fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    // 回到给NavActivity跳转到GospelDetailActivity
    interface OnGospelSelectedListener {
        fun onGospelSelected(gospel: DocumentSnapshot)
    }

    override lateinit var view: NavItemContract.IView

    private lateinit var navItemView: NavItemView
    protected lateinit var navActivity: NavActivity
    private var isOn = false

    companion object {
        const val RC_SIGN_IN = 0
    }

    override fun deinit() {
    }

    // 创建账号
    override fun createUser() {
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
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.nav_item_view, parent, false)
        navItemView = NavItemView(this, itemView, navActivity)

        /**
         * First "present = this" init Presenter in constructor, then "navItemView = this" init View in init method
         */
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                navItemView = MeItemView(this, itemView, navActivity)
                navItemView.onCreateView(parent, viewType, itemView)
            }
        }
        return navItemView.onCreateView(parent, viewType, itemView)
    }

    override fun getItemCount(): Int {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                return (navs as MeBean).settings.size + 2
            }
        }
        return snapshots.size
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
                    mRvNav?.showContextMenuForChild(holder.containerView, holder.containerView.measuredWidth.toFloat() - ChristianUtil.dpToPx(200), ChristianUtil.dpToPx(4).toFloat())
//                    navItemView.showPopupMenu(v)
                }

                holder.bind(snapshots[position], listener)
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
        if (holder.adapterPosition > mPosition) {
            navItemView.animateItemView(holder.itemView)
        } else {
            navItemView.clearItemAnimation(holder.itemView)
        }
        mPosition = holder.adapterPosition
    }

    private var mRvNav: ContextMenuRecyclerView? = null

    fun setRv(rv_nav: ContextMenuRecyclerView?) {
        mRvNav = rv_nav
    }

}

const val VIEW_TYPE_NORMAL = 0
const val VIEW_TYPE_PORTRAIT = 1
const val VIEW_TYPE_SMALL = 2
const val VIEW_TYPE_BUTTON = 3
