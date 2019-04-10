package com.christian.nav

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.AnimationUtils
import com.christian.ChristianApplication
import com.christian.R
import com.christian.data.MeBean
import com.christian.data.NavBean
import com.christian.gospeldetail.NavDetailActivity
import com.christian.navitem.NavItemPresenter
import com.christian.view.ContextMenuRecyclerView
import com.christian.view.ItemDecoration
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.nav_fragment.view.*
import org.jetbrains.anko.debug


open class NavFragment : Fragment(), NavContract.INavFragment, NavItemPresenter.OnGospelSelectedListener {
    override fun onGospelSelected(gospel: DocumentSnapshot) {
        // Go to the details page for the selected restaurant
        gospelId = gospel.id
        val intent = Intent(this@NavFragment.navActivity, NavDetailActivity::class.java)
        intent.putExtra(toolbarTitle, gospel.data?.get("subtitle").toString())
        startActivity(intent)
    }

    open lateinit var firestore: FirebaseFirestore
    open lateinit var query: Query

    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navActivity: NavActivity
    private lateinit var ctx: Context
    private lateinit var navAdapter: NavItemPresenter<List<NavBean>>
    private lateinit var meAdapter: NavItemPresenter<MeBean>
    private lateinit var v: View
    var navId = -1
    lateinit var gospelId: String

    init {
        debug { "look at init times" }
    }

    /**
     * It is the first place application code can run where the fragment is ready to be used
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navActivity = context as NavActivity
        ctx = context
        debug { "onAttach" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        debug { "nav fragment is onCreateView, savedInstanceState, $savedInstanceState ---onCreateView" }
        v = inflater.inflate(R.layout.nav_fragment, container, false)
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true)
        // Firestore
        firestore = FirebaseFirestore.getInstance()
        // Get ${LIMIT} gospels
        query = firestore.collection("gospels")

        presenter = navActivity.presenter
        lifecycle.addObserver(NavFragmentLifecycleObserver())
        return v
    }

    inner class NavFragmentLifecycleObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun bindNavAdapter() {
            debug { "Lifecycle.Event.ON_CREATE" }
            presenter.init(whichActivity = null, navFragment = this@NavFragment)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun startListening() {
            if (navId != VIEW_ME) {
                navAdapter.startListening()
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun stopListening() {
            if (navId == VIEW_ME) {
                debug { "me stop listening" }
                meAdapter.stopListening()
            } else if (navId == VIEW_HOME || navId == VIEW_GOSPEL) {
                debug { "home or gospel stop listening" }
                navAdapter.stopListening()
            }
        }
    }

    override fun initView(navBeans: List<NavBean>) {
        debug { "nav fragment is $this and navId is $navId --initView" }
        initSrl()
        initRv(navBeans)
    }

    private fun initSrl() {
//        val headerView = ProgressLayout(navActivity)
//        headerView.setColorSchemeColors(R.color.colorAccent, R.color.colorAccentRed, R.color.colorPrimary)
//        v.srl_nav.setHeaderView(headerView)

        v.srl_nav.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout) {
                refreshLayout.finishRefreshing()
            }
        })
    }

    private fun initRv(navBeans: List<NavBean>) {
        v.cv_nav_frag.visibility = View.GONE
        registerForContextMenu(v.rv_nav)

        when (navId) {
            VIEW_HOME -> {

            }
            VIEW_GOSPEL -> {
                makeViewBlur(v.bv_tabs_nav, navActivity.cl_nav, navActivity.window)
                for (tabTitle in (presenter as NavPresenter).tabTitleList) {
                    v.tl_nav.newTab().setText(tabTitle).let { v.tl_nav.addTab(it) }
                }
            }
            VIEW_DISCIPLE -> {
            }
            VIEW_ME -> {
                val meBeans = Gson().fromJson<MeBean>(getJson("me.json", ctx), MeBean::class.java)
                meAdapter = object : NavItemPresenter<MeBean>(query, this@NavFragment, navs = meBeans, navId = navId) {
                    override fun onDataChanged() {
//                        if (itemCount == 0) {
//                            rv_nav.visibility = View.GONE
//                            navActivity.pb_nav.visibility = View.GONE
//                        } else {
//                            rv_nav.visibility = View.VISIBLE
//                            navActivity.pb_nav.visibility = View.GONE
//                        }
                    }

                    override fun onError(e: FirebaseFirestoreException) {
                        // Show a snackbar on errors
                        Snackbar.make(cl_gospel_detail,
                                "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
                    }
                }
                v.rv_nav.adapter = meAdapter
                unregisterForContextMenu(v.rv_nav)
            }
        }
        if (navId != VIEW_ME) {
            navAdapter = object : NavItemPresenter<List<NavBean>>(query, this@NavFragment, navs = navBeans, navId = navId) {
                override fun onDataChanged() {
                    if (itemCount == 0) {
                        rv_nav.visibility = View.GONE
//                        (activity as NavActivity).pb_nav.visibility = View.GONE
                    } else {
                        rv_nav.visibility = View.VISIBLE
//                        (activity as NavActivity).pb_nav.visibility = View.GONE
                    }
                }

                override fun onError(e: FirebaseFirestoreException) {
                    // Show a snackbar on errors
                    Snackbar.make(cl_gospel_detail,
                            "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
                }

            }

            // set Query
            if (navId != VIEW_ME) // 增加了复杂性，需要想办法统一Me
                navAdapter.setQuery(query) // onStop的时候注销了snapshotListener，onResume的时候一定要开启
            navAdapter.setRv(v.rv_nav)
            v.rv_nav.adapter = navAdapter
        }
        v.rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        v.rv_nav.addOnScrollListener(object : HidingScrollListener(this) {

            override fun onHide() {
                hide()
            }

            override fun onShow() {
                show()
            }

            override fun onTop() {
            }

            override fun onBottom() {
//                bv_nav.requestFocus()
//                Log.i("bottom", "onBottom")
            }
        })

//        val indexScrollListener = IndexScrollListener()
//        indexScrollListener.register(v.fs_nav)
//        v.rv_nav.addOnScrollListener(indexScrollListener)
    }

    fun show() {
//        navActivity.showFAB()
//        if (navId == 1 && cv_nav_frag.visibility == View.GONE) {
//            cv_nav_frag.visibility = View.VISIBLE
//            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in)
//            cv_nav_frag.startAnimation(fadeIn)
//        }
    }

    fun hide() {
//        navActivity.fab_nav.hide()
//        if (navId == 1 && cv_nav_frag.visibility == View.VISIBLE) {
//            val fadeOut = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out)
//            cv_nav_frag.startAnimation(fadeOut)
//            cv_nav_frag.visibility = View.GONE
//        }
    }

    override fun showSrl() {
    }

    override fun hideSrl() {
//        v.srl_nav.isRefreshing = false
    }

    override fun invalidateRv(navBeans: List<NavBean>) {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
                navAdapter.navs = navBeans
            }
            VIEW_ME -> {
                val meBean = Gson().fromJson<MeBean>(getJson("me.json", ctx), MeBean::class.java)
                meAdapter.navs = meBean
            }
        }
        runLayoutAnimation(v.rv_nav)
        navActivity.showFAB()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        navActivity.menuInflater.inflate(R.menu.menu_share, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as ContextMenuRecyclerView.ContextMenuInfo
        val position = info.position
        val data = info.itemView.tag
//        val message = getString(R.string.app_name, item.title, position, data)
//        AlertDialog.Builder(context!!).setMessage(message).show()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putShort(NAV_ID, navId.toShort())
//        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        // handling memory leaks
        val refWatcher = ChristianApplication.getRefWatcher(ctx)
        refWatcher.watch(this)
    }
}

