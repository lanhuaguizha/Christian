package com.christian.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.christian.R
import com.christian.base.BaseFragment
import kotlinx.android.synthetic.main.scrolling_detail_fragment.*
import org.xutils.view.annotation.ContentView

@ContentView(R.layout.scrolling_detail_fragment)
class ScrollingDetailFragment : BaseFragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
////        fab.setOnClickListener { view ->
////            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                    .setAction("Action", null).show()
////        }
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()
//        initListener()
    }

//    private fun initListener() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    private fun initView() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
}
