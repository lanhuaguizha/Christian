package com.christian.nav

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class NavFragmentPagerAdapter(private val navFragmentList: MutableList<NavFragment>, fm: FragmentManager, private val tabTitleList: ArrayList<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return navFragmentList[position]
    }

    override fun getCount(): Int {
        return navFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitleList[position]
    }

    fun cleanFragmentList() {
        navFragmentList.clear()
    }
}