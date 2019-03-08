package com.christian.gospeldetail.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.gospeldetail.ui.adapters.GospelDetailAdapter
import com.christian.nav.NavFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import org.jetbrains.anko.info

/**
 * MVVM-databinding架构开发
 * 福音详情页
 */
class GospelDetailFragment : NavFragment() {

    companion object {
        fun newInstance() = GospelDetailFragment()
    }

    private lateinit var viewModel: GospelDetailViewModel

    private lateinit var firestore: FirebaseFirestore

    private lateinit var query: Query

    private lateinit var gospelDetailAdapter: GospelDetailAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true)
        // Firestore
        firestore = FirebaseFirestore.getInstance()
        // Get ${LIMIT} gospels
        query = firestore.collection("gospels")
        info { "query$query" }
        gospelDetailAdapter = object : GospelDetailAdapter(query, this@GospelDetailFragment) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    rv_gospel_detail.visibility = View.GONE
//                    viewEmpty.visibility = View.VISIBLE
                } else {
                    rv_gospel_detail.visibility = View.VISIBLE
//                    viewEmpty.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(cl_gospel_detail,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }
        // set Query
        gospelDetailAdapter.setQuery(query)

        return inflater.inflate(R.layout.gospel_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GospelDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
