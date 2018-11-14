package com.christian.nav

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.christian.R
import com.christian.data.Nav
import com.christian.index.IndexScrollListener
import com.christian.navitem.NavItemPresenter
import com.christian.view.ItemDecoration
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.view.*
import org.jetbrains.anko.info

open class NavFragment(val navId: Int) : Fragment(), NavContract.INavFragment {

    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navActivity: NavActivity
    private lateinit var mContext: Context

    private lateinit var adapter: NavItemPresenter
    lateinit var v: View

    init {
        info { "look at init times" }
    }

    /**
     * It is the first place application code can run where the fragment is ready to be used
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        navActivity = context as NavActivity
        mContext = context
        presenter = navActivity.presenter
        presenter.init(navFragment = this)
        info { "onAttach" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "onCreate" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info { "onCreateView" }
        v = inflater.inflate(R.layout.nav_fragment, container, false)
        initView((presenter as NavPresenter).navList)
        return v
    }

    override fun initView(navs: List<Nav>) {
        info { "initView$navId" }
        initSrl()
        initFs()
        initRv(navs)
        presenter.createNav(navId)
    }

    private fun initSrl() {
        v.srl_nav.setColorSchemeColors(ResourcesCompat.getColor(context!!.resources, R.color.colorAccent, context?.theme))
        v.srl_nav.setOnRefreshListener { presenter.createNav(navId, true) }
        v.srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, context?.theme)
    }

    private fun initFs() {
        v.fs_nav.setRecyclerView(v.rv_nav)
    }

    private fun initRv(navs: List<Nav>) {
        adapter = NavItemPresenter(navs)
        v.rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        v.rv_nav.layoutManager = LinearLayoutManager(context)
        v.rv_nav.adapter = adapter
        v.rv_nav.addOnScrollListener(object : HidingScrollListener(this) {

            override fun onHide() {
                activity?.fab_nav?.hide()
            }

            override fun onShow() {
                activity?.fab_nav?.show()
            }

            override fun onTop() {
            }

            override fun onBottom() {
//                bv_nav.requestFocus()
//                Log.i("bottom", "onBottom")
            }
        })

        val indexScrollListener = IndexScrollListener()
        indexScrollListener.register(v.fs_nav)
        v.rv_nav.addOnScrollListener(indexScrollListener)
    }

    override fun showSrl() {
    }

    override fun hideSrl() {
        v.srl_nav.isRefreshing = false
    }

    override fun invalidateRv(navs: List<Nav>) {
        adapter.navs = navs
        runLayoutAnimation(v.rv_nav)
        navActivity.initFAB()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}