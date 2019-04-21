package com.christian.nav

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class NavFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    lateinit var currentFragment : NavFragment

    override fun getItem(position: Int): Fragment {
        val navFragment = NavFragment()
        navFragment.navId = position
        return navFragment
    }

    override fun getCount(): Int {
        return 4
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        currentFragment = `object` as NavFragment
        super.setPrimaryItem(container, position, `object`)
    }

}