package com.christian.nav

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.animation.AnimationUtils
import com.christian.ChristianApplication
import com.christian.R
import com.christian.data.MeBean
import com.christian.data.NavBean
import com.christian.navitem.NavItemPresenter
import com.christian.view.ContextMenuRecyclerView
import com.christian.view.ItemDecoration
import com.google.gson.Gson
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import kotlinx.android.synthetic.main.nav_fragment.view.*
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.dip

open class NavFragment : Fragment(), NavContract.INavFragment {

    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navActivity: NavActivity
    private lateinit var ctx: Context
    private lateinit var adapter: NavItemPresenter<List<NavBean>>
    private lateinit var meAdapter: NavItemPresenter<MeBean>
    private var v: View? = null
    var navId = -1

    init {
        info { "look at init times" }
    }

    /**
     * It is the first place application code can run where the fragment is ready to be used
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navActivity = context as NavActivity
        ctx = context
        info { "onAttach" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info { "nav fragment is onCreateView, savedInstanceState, $savedInstanceState ---onCreateView" }
        v = inflater.inflate(R.layout.nav_fragment, container, false)
        presenter = navActivity.presenter
        presenter.init(whichActivity = null, navFragment = this)
        return v
    }

    override fun initView(navBeans: List<NavBean>) {
        info { "nav fragment is $this and navId is $navId --initView" }
        initSrl()
        initFs()
        initRv(navBeans)
        presenter.createNav(navId, navFragment = this)
    }

    private fun initSrl() {
        v?.srl_nav?.setColorSchemeColors(ResourcesCompat.getColor(context!!.resources, R.color.colorAccent, context?.theme))
        v?.srl_nav?.setOnRefreshListener { presenter.createNav(navId, true, this) }
        v?.srl_nav?.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, context?.theme)
    }

    private fun initFs() {
        v?.fs_nav?.setRecyclerView(v?.rv_nav)
    }

    private fun initRv(navBeans: List<NavBean>) {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
                adapter = NavItemPresenter(navs = navBeans, navId = navId)
                adapter.setRv(v?.rv_nav)
                v?.rv_nav?.adapter = adapter
            }
            VIEW_ME -> {
                val meBeans = Gson().fromJson<MeBean>(getJson("me.json", ctx), MeBean::class.java)
                meAdapter = NavItemPresenter(navs = meBeans, navId = navId)
                v?.rv_nav?.adapter = meAdapter
            }
        }

        if (navId == 1) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                navActivity.abl_nav.elevation = dip(0).toFloat()
//            }
            v?.bv_tabs_nav?.let { makeViewBlur(it, navActivity.cl_nav) }
            v?.bv_tabs_nav?.visibility = View.VISIBLE
            for (tabTitle in (presenter as NavPresenter).tabTitleList) {
                v?.tl_nav?.newTab()?.setText(tabTitle)?.let { v?.tl_nav?.addTab(it) }
            }
            v?.rv_nav?.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt(), dip(8)))
        } else {
            v?.bv_tabs_nav?.visibility = View.GONE
            v?.rv_nav?.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt(), dip(8)))
        }
        v?.rv_nav?.layoutManager = LinearLayoutManager(context)
        registerForContextMenu(v?.rv_nav)

        v?.rv_nav?.addOnScrollListener(object : HidingScrollListener(this) {

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
        navActivity.showFAB()
        if (navId == 1 && bv_tabs_nav.visibility == View.GONE) {
            bv_tabs_nav.visibility = View.VISIBLE
            val fadeIn = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in)
            bv_tabs_nav.startAnimation(fadeIn)
        }
    }

    fun hide() {
        activity?.fab_nav?.hide()
        if (navId == 1 && bv_tabs_nav.visibility == View.VISIBLE) {
            val fadeOut = AnimationUtils.loadAnimation(context, R.anim.abc_fade_out)
            bv_tabs_nav.startAnimation(fadeOut)
            bv_tabs_nav.visibility = View.GONE
        }
    }

    override fun showSrl() {
    }

    override fun hideSrl() {
        v?.srl_nav?.isRefreshing = false
    }

    override fun invalidateRv(navBeans: List<NavBean>) {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
                adapter.navs = navBeans
            }
            VIEW_ME -> {
                val meBean = Gson().fromJson<MeBean>(getJson("me.json", ctx), MeBean::class.java)
                meAdapter.navs = meBean
            }
        }
        runLayoutAnimation(v?.rv_nav)
        navActivity.showFAB()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView?) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView?.context, R.anim.layout_animation_from_right)
        recyclerView?.layoutAnimation = animation
        recyclerView?.adapter?.notifyDataSetChanged()
        recyclerView?.scheduleLayoutAnimation()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
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
        v = null

        val refWatcher = ChristianApplication.getRefWatcher(ctx)
        refWatcher.watch(this)
    }
}

