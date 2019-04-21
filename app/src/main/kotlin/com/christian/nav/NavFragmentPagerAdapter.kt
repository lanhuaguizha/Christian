package com.christian.nav

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class NavFragmentPagerAdapter(private val navFragmentList: List<NavFragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return navFragmentList[position]
    }

    override fun getCount(): Int {
        return navFragmentList.size
    }

}