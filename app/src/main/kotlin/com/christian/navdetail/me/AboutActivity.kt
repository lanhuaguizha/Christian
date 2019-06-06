package com.christian.navdetail.me

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.christian.R
import com.christian.data.MeBean
import com.christian.nav.getDocumentReference
import com.christian.nav.nullString
import com.christian.nav.toolbarTitle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.gospel_detail_fragment.*
import me.drakeet.support.about.*
import me.drakeet.support.about.provided.PicassoImageLoader
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
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

        items.clear()
        for (me in meBean.detail) {
            if (me.type == "category")
                items.add(Category(me.category))
            if (me.type == "card")
                items.add(Card(me.card))
        }
        recyclerView.visibility = View.VISIBLE
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition, lastOffset)
        // Dispatch the event
        debug { "onEvent:numChanges:$documentSnapshots.documentChanges.size" }
//        for (change in documentSnapshots.documentChanges) {
//            when (change.type) {
//                DocumentChange.Type.ADDED -> onDocumentAdded(change)
//                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
//                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
//            }
//        }
//        onDocumentAdded(documentSnapshots)
        adapter.notifyDataSetChanged()
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
        recyclerView.visibility = View.INVISIBLE
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
            }
        })


        items.add(Category("介绍与帮助"))
        items.add(Card(getString(R.string.large_text)))
//        items.add(Card(getString(R.string.card_content)))

        items.add(Category(getString(R.string.developer)))
        items.add(Contributor(R.drawable.me, "陶永强", "Developer & designer", "https://github.com/lanhuaguizha"))
//        items.add(Contributor(R.mipmap.ic_launcher, "黑猫酱", "Developer", "https://drakeet.me"))
//        items.add(Contributor(R.mipmap.ic_launcher, "小艾大人", "Developer"))

        /* items.add(Category("我独立开发的应用"))
         items.add(Recommendation(
                 0, getString(R.string._Gen),
                 "https://storage.recommend.wetolink.com/storage/app_recommend/images/YBMHN6SRpZeF0VHbPZWZGWJ2GyB6uaPx.png",
                 "com.drakeet.purewriter",
                 "快速的纯文本编辑器，我们希望写作能够回到原本的样子：纯粹、有安全感、随时、绝对不丢失内容、具备良好的写作体验。",
                 "https://www.coolapk.com/apk/com.drakeet.purewriter",
                 "2017-10-09 16:46:57",
                 "2017-10-09 16:46:57", 2.93, true)
         )
         items.add(Recommendation(
                 1, getString(R.string._Gen),
                 "http://image.coolapk.com/apk_logo/2016/0831/ic_pure_mosaic-2-for-16599-o_1argff2ddgvt1lfv1b3mk2vd6pq-uid-435200.png",
                 "me.drakeet.puremosaic",
                 "专注打码的轻应用，包含功能：传统马赛克、毛玻璃效果、选区和手指模式打码，更有创新型高亮打码和 LowPoly 风格马赛克。只为满足一个纯纯的打码需求，让打码也能成为一种赏心悦目。",
                 "https://www.coolapk.com/apk/me.drakeet.puremosaic",
                 "2017-10-09 16:46:57",
                 "2017-10-09 16:46:57", 2.64, true)
         )*/
        // Load more Recommendation items from remote server asynchronously
//        RecommendationLoaderDelegate.attach(this, items.size, MoshiJsonConverter() /* or new GsonJsonConverter() */)
        // or
        // RecommendationLoader.getInstance().loadInto(this, items.size())

        items.add(Category("Open Source Licenses"))
        items.add(License("about-page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"))
        items.add(License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"))
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
        icon.setImageResource(R.mipmap.ic_launcher)
        slogan.text = getString(R.string.app_name)
        title = intent?.extras?.getString(toolbarTitle) ?: nullString
        version.text = getString(R.string.version)
    }
}
