package com.christian.navdetail.gospel

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.nav.NavFragment

class GospelReviewFragment : NavFragment() {

    companion object {
        fun newInstance() = GospelReviewFragment()
    }

    private lateinit var viewModel: GospelDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GospelDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
