package com.christian.nav.gospel

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.customview.widget.ViewDragHelper.INVALID_POINTER
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.christian.BuildConfig
import com.christian.R
import com.christian.data.MeBean
import com.christian.multitype.Card
import com.christian.multitype.Category
import com.christian.nav.*
import com.christian.nav.me.AbsAboutActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout.MODE_FIXED
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.nav_activity.*
import kotlinx.android.synthetic.main.nav_fragment.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.warn
import java.util.*

private fun NavActivity.initTbWithTitle(title: String) {
    sbl_nav.visibility = View.GONE

    /**
     * set up button
     */
    setSupportActionBar(tb_nav)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.title = getString(R.string.app_name)
    tb_nav.setNavigationOnClickListener { finish() }
}

/**
 * The nav details page contains all the logic of the nav page.
 */
class NavDetailActivity : AbsAboutActivity() {

    private lateinit var meBean: MeBean
    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()
    private var snapshot: DocumentSnapshot? = null

    var isMovingRight: Boolean = true // true不会崩溃，进入nav detail左滑的时候

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.removeItem(R.id.menu_options_nav)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_nav_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
//                Snackbar.make(getString(R.string.toast_share)).show()
                true
            }
            R.id.menu_favorite -> {
//                snackbar(getString(R.string.toast_favorite)).show()
                true
            }
            R.id.menu_translate -> {
//                snackbar(getString(R.string.toast_translate)).show()
                true
            }
            R.id.menu_read -> {
//                snackbar(getString(R.string.toast_read)).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class NavDetailFragmentPagerAdapter(fm: FragmentManager) : NavActivity.NavFragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    val gospelDetailFragment = NavDetailFragment()
                    return gospelDetailFragment
                }
//                1 -> {
//                    val gospelReviewFragment = GospelReviewFragment()
//                    gospelReviewFragment.navId = position + 31
//                    return gospelReviewFragment
//                }
            }
            return super.getItem(position)
        }

        override fun getCount(): Int {
            return 1
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            when (position) {
                0 -> {
                    currentFragment = `object` as NavDetailFragment
                }
//                1 -> {
//                    currentFragment = `object` as GospelReviewFragment
//                }
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meRef = firestore.collection("mes").document("kT04H8SFVsOvqz4YLfUq")
        startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListening()
    }

    override fun onItemsCreated(items: MutableList<Any>) {
    }

    override fun onEvent(documentSnapshots: DocumentSnapshot?, e: FirebaseFirestoreException?) {
        if (e != null) {
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
        // 恢复位置
        val sharedPreferences = getSharedPreferences(intent?.extras?.getString(toolbarTitle)
                ?: nullString, Activity.MODE_PRIVATE)
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(sharedPreferences.getInt("lastPosition", 0), sharedPreferences.getInt("lastOffset", 0))
    }

    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        slogan.text = getString(R.string.app_name)
        icon.setImageResource(R.drawable.me)
//        title = intent?.extras?.getString(toolbarTitle) ?: nullString
//        version.text = BuildConfig.VERSION_NAME
        title = getString(R.string.app_name)
    }
}
