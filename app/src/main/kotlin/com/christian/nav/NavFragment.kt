package com.christian.nav

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

class NavFragment(navPresenter: NavPresenter) : Fragment(), NavContract.INavFragment {

    override var presenter: NavContract.IPresenter = navPresenter
    private lateinit var adapter: NavItemPresenter
    lateinit var v: View
    var navId: Int = 0

    init {
        info { "look at init times" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "onCreate" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info { "onCreateView" }
        v = inflater.inflate(R.layout.nav_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.init(navFragment = this)
    }

    override fun initView(navFragments: List<NavFragment>, navs: List<Nav>) {
        info { "initView" }
        initSrl()
        initFs()
        (activity as NavActivity).initFAB()
        initRv(listOf(Nav()))
    }

    override fun deinitView() {
    }

    override fun initSrl() {
        v.srl_nav.setColorSchemeColors(ResourcesCompat.getColor(context!!.resources, R.color.colorAccent, context?.theme))
        v.srl_nav.setOnRefreshListener { presenter.createNav(navId.toString(), true, this) }
        v.srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, context?.theme)
    }

    override fun initFs() {
        v.fs_nav.setRecyclerView(v.rv_nav)
    }

    override fun initRv(navs: List<Nav>) {
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
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

}