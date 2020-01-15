package com.christian.nav.me

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.christian.data.MeBean
import com.christian.multitype.*
import com.christian.nav.nullString
import com.christian.nav.toolbarTitle
import com.christian.util.restoreScrolledPositionOfDetailPage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.nav_activity.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


class AboutActivity : AbsAboutActivity(), OnRecommendationClickedListener, OnContributorClickedListener, AnkoLogger {

    private lateinit var meBean: MeBean
    private var snapshot: DocumentSnapshot? = null

    override fun onEvent(documentSnapshots: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
            warn { "onEvent:error$e" }
            Snackbar.make(cl_nav, "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            return
        }

        if (documentSnapshots == null) {
            return
        }

        snapshot = documentSnapshots
        meBean = snapshot?.toObject(MeBean::class.java) ?: MeBean()

        if (items.isNotEmpty())
            items.clear()
        for (me in meBean.detail) {
            if (me.type == "category")
                items.add(Category(me.category))
            if (me.type == "card")
                items.add(Card(me.card))
        }

        restoreScrolledPositionOfDetailPage(this, recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImageLoader(PicassoImageLoader())
        onRecommendationClickedListener = this@AboutActivity
        onContributorClickedListener = this@AboutActivity
        meRef = firestore.collection("mes").document("kT04H8SFVsOvqz4YLfUq")
        startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListening()
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
//        icon.setImageResource(R.drawable.ic_group_add_black_24dp)
//        slogan.text = getString(R.string.app_name)
        title = intent?.extras?.getString(toolbarTitle) ?: nullString
//        version.text = BuildConfig.VERSION_NAME
    }
}
