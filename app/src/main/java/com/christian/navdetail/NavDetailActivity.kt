package com.christian.navdetail

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.christian.R
import com.christian.nav.NavActivity
import kotlinx.android.synthetic.main.act_nav.*
import kotlinx.android.synthetic.main.sb_nav.*
import kotlinx.android.synthetic.main.tb_nav.*

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

    override fun setTb(title: String) {

        search_view_container.visibility = View.GONE

        /**
         * set up button
         */
        setSupportActionBar(toolbar_actionbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "虚心的人有福了"
        supportActionBar?.subtitle = "马太福音"
        toolbar_actionbar.setNavigationOnClickListener { finish() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_share -> {
                true
            }
            R.id.menu_download -> {
                true
            }
            R.id.menu_collection -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}