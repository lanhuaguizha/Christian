package com.christian.nav

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.christian.R
import com.christian.data.MeBean
import com.christian.data.NavBean
import com.christian.navitem.NavItemPresenter
import com.christian.view.ItemDecoration
import com.google.gson.Gson
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.view.*
import org.jetbrains.anko.info

open class NavFragment() : Fragment(), NavContract.INavFragment, Parcelable {

    override lateinit var presenter: NavContract.IPresenter
    private lateinit var navActivity: NavActivity
    private lateinit var ctx: Context
    private lateinit var adapter: NavItemPresenter<List<NavBean>>
    private lateinit var meAdapter: NavItemPresenter<MeBean>
    private lateinit var v: View
    var navId = -1

    constructor(parcel: Parcel) : this() {
        navId = parcel.readInt()
    }

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
        presenter.init(this, savedInstanceState)
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
        v.srl_nav.setColorSchemeColors(ResourcesCompat.getColor(context!!.resources, R.color.colorAccent, context?.theme))
        v.srl_nav.setOnRefreshListener { presenter.createNav(navId, true, this) }
        v.srl_nav.background = ResourcesCompat.getDrawable(resources, R.color.default_background_nav, context?.theme)
    }

    private fun initFs() {
        v.fs_nav.setRecyclerView(v.rv_nav)
    }

    private fun initRv(navBeans: List<NavBean>) {
        when (navId) {
            VIEW_HOME, VIEW_GOSPEL, VIEW_DISCIPLE -> {
                adapter = NavItemPresenter(navs = navBeans, navId = navId)
                v.rv_nav.adapter = adapter
            }
            VIEW_ME -> {
                val meBeans = MeBean()
                meAdapter = NavItemPresenter(navs = meBeans, navId = navId)
                v.rv_nav.adapter = meAdapter
            }
        }
        v.rv_nav.addItemDecoration(ItemDecoration(resources.getDimension(R.dimen.search_margin_horizontal).toInt()))
        v.rv_nav.layoutManager = LinearLayoutManager(context)
        v.rv_nav.addOnScrollListener(object : HidingScrollListener(this) {

            override fun onHide() {
                activity?.fab_nav?.hide()
            }

            override fun onShow() {
                navActivity.showFAB()
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

    override fun showSrl() {
    }

    override fun hideSrl() {
        v.srl_nav.isRefreshing = false
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
        runLayoutAnimation(v.rv_nav)
        navActivity.showFAB()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val animation = AnimationUtils.loadLayoutAnimation(recyclerView.context, R.anim.layout_animation_from_right)
        recyclerView.layoutAnimation = animation
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putShort(NAV_ID, navId.toShort())
//        super.onSaveInstanceState(outState)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(navId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavFragment> {
        override fun createFromParcel(parcel: Parcel): NavFragment {
            return NavFragment(parcel)
        }

        override fun newArray(size: Int): Array<NavFragment?> {
            return arrayOfNulls(size)
        }
    }
}

