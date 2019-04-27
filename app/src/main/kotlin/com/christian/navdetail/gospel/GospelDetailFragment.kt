package com.christian.navdetail.gospel

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.christian.R
import com.christian.navdetailitem.GospelDetailAdapter
import com.christian.nav.NavActivity
import com.christian.nav.NavFragment
import com.christian.view.GospelDetailItemDecoration
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import kotlinx.android.synthetic.main.gospel_detail_fragment.view.*

/**
 * MVVM-databinding架构开发
 * 福音详情页
 */
class GospelDetailFragment : NavFragment() {

    companion object {
        fun newInstance() = GospelDetailFragment()
    }

    private lateinit var viewModel: GospelDetailViewModel

    lateinit var firestore: FirebaseFirestore

    private lateinit var gospelRef: DocumentReference

    private lateinit var gospelDetailAdapter: GospelDetailAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.gospel_detail_fragment, container, false)

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true)
        // Firestore
        firestore = FirebaseFirestore.getInstance()
        // Get ${LIMIT} gospels
        gospelRef = firestore.collection("gospels").document("2019.3.15 10:31")
        gospelDetailAdapter = object : GospelDetailAdapter(gospelRef, this@GospelDetailFragment.activity as NavActivity) {
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
        // stopListening
        gospelDetailAdapter.setQuery(gospelRef)

        view.rv_gospel_detail.adapter = gospelDetailAdapter
        view.rv_gospel_detail.addItemDecoration(GospelDetailItemDecoration(resources.getDimension(R.dimen.activity_horizontal_margin).toInt()))

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GospelDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStop() {
        super.onStop()
        gospelDetailAdapter.stopListening()
    }
}
