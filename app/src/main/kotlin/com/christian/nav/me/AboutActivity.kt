package com.christian.nav.me

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.MeBean
import com.christian.nav.getDocumentReference
import com.christian.nav.nullString
import com.christian.nav.toolbarTitle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import com.christian.library.multitype.*
import com.christian.library.multitype.PicassoImageLoader
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


class AboutActivity : AbsAboutActivity(), OnRecommendationClickedListener, OnContributorClickedListener, EventListener<DocumentSnapshot>, AnkoLogger {

    private lateinit var meBean: MeBean
    private lateinit var meRef: DocumentReference
    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()
    private var snapshot: DocumentSnapshot? = null

    private fun startListening() {
        if (registration == null) {
            registration = meRef.addSnapshotListener(this)
        }
    }

    private fun stopListening() {
        registration?.remove()
        registration = null

        snapshots.clear()
        adapter.notifyDataSetChanged()
    }

    private var lastPosition = 0//位置
    private var lastOffset = 0//偏移量

    override fun onEvent(documentSnapshots: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            warn { "onEvent:error$e" }
            Snackbar.make(cl_gospel_detail, "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            return
        }

        if (documentSnapshots == null) {
            return
        }

        snapshot = documentSnapshots
        meBean = snapshot?.toObject(MeBean::class.java) ?: MeBean()

        if (!items.isEmpty())
            items.clear()
        for (me in meBean.detail) {
            if (me.type == "category")
                items.add(Category(me.category))
            if (me.type == "card")
                items.add(Card(me.card))
        }
        // 恢复位置
        val sharedPreferences = getSharedPreferences(intent?.extras?.getString(toolbarTitle)
                ?: nullString, Activity.MODE_PRIVATE)
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(sharedPreferences.getInt("lastPosition", 0), sharedPreferences.getInt("lastOffset", 0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImageLoader(PicassoImageLoader())
        onRecommendationClickedListener = this@AboutActivity
        onContributorClickedListener = this@AboutActivity
        meRef = getDocumentReference("mes", "kT04H8SFVsOvqz4YLfUq")
        startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListening()
    }

    override fun onItemsCreated(items: MutableList<Any>) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val topView = recyclerView.layoutManager?.getChildAt(0) //获取可视的第一个view
                lastOffset = topView?.top ?: 0//获取与该view的顶部的偏移量
                lastPosition = topView?.let { recyclerView.layoutManager?.getPosition(it) }
                        ?: 0  //得到该View的数组位置
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val topView = recyclerView.layoutManager?.getChildAt(0) //获取可视的第一个view
                lastOffset = topView?.top ?: 0//获取与该view的顶部的偏移量
                lastPosition = topView?.let { recyclerView.layoutManager?.getPosition(it) }
                        ?: 0  //得到该View的数组位置

                val sharedPreferences = getSharedPreferences(intent?.extras?.getString(toolbarTitle)
                        ?: nullString, Activity.MODE_PRIVATE)
                sharedPreferences.edit {
                    putInt("lastPosition", lastPosition)
                    putInt("lastOffset", lastOffset)
                }
            }
        })
    }

    override fun onRecommendationClicked(itemView: View, recommendation: Recommendation): Boolean {
        Toast.makeText(this, "onRecommendationClicked: " + recommendation.appName, Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onContributorClicked(itemView: View, contributor: Contributor): Boolean {
        if (contributor.name.equals("小艾大人")) {
            Toast.makeText(this, "onContributorClicked: " + contributor.name, Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        icon.setImageResource(R.drawable.ic_group_add_black_24dp)
        slogan.text = getString(R.string.app_name)
        title = intent?.extras?.getString(toolbarTitle) ?: nullString
        version.text = getString(R.string.version)
    }
}
