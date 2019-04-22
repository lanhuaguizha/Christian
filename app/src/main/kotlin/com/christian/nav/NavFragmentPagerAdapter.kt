package com.christian.nav

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.ViewGroup

class NavFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    lateinit var currentFragment : NavFragment

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
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