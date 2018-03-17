package com.christian.navdetail

import android.view.View
import com.christian.nav.NavActivity
import kotlinx.android.synthetic.main.act_nav.*
import kotlinx.android.synthetic.main.sb_nav.*

/**
 * The nav details page contains all the logic of the nav page.
 */
class NavDetailActivity : NavActivity() {


    // 不需要禁用滑动
    override fun initSbl() {

    }

    // 不需要底部导航栏
    override fun initBnv() {
        bnv_nav.visibility = View.GONE
    }

    override fun setupToolbar(title: String) {

        search_view_container.visibility = View.GONE

    }
}