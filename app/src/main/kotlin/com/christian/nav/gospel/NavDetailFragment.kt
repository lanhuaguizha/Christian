package com.christian.nav.gospel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.christian.R
import com.christian.nav.NavActivity
import com.christian.nav.NavFragment
import com.christian.view.GospelDetailItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.view.*

/**
 * MVVM-databinding架构开发
 * 福音详情页
 */
class NavDetailFragment : NavFragment() {

    companion object {
        fun newInstance() = NavDetailFragment()
    }

    private lateinit var viewModel: GospelDetailViewModel

    private lateinit var gospelRef: DocumentReference

    private lateinit var gospelDetailAdapter: GospelDetailAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navActivity = context as NavDetailActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        super.onCreateView(inflater, container, savedInstanceState)
        // Enable Firestore logging
//        FirebaseFirestore.setLoggingEnabled(true)
        // Firestore
        // Get ${LIMIT} gospels
        gospelRef = navActivity.firestore.collection("gospels").document("2019.3.15 10:31")
        gospelDetailAdapter = object : GospelDetailAdapter(gospelRef, this@NavDetailFragment.activity as NavActivity) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    v.rv_nav.visibility = View.GONE
//                    viewEmpty.visibility = View.VISIBLE
                } else {
                    v.rv_nav.visibility = View.VISIBLE
                    v.pb_nav.visibility = View.GONE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(cl_nav,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }
        // stopListening
        gospelDetailAdapter.setQuery(gospelRef)

        v.rv_nav.adapter = gospelDetailAdapter
        v.rv_nav.addItemDecoration(GospelDetailItemDecoration(resources.getDimension(R.dimen.activity_horizontal_margin).toInt()))

        return v
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
