package com.christian.nav.gospel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.text.style.AlignmentSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.christian.R
import com.christian.data.MeBean
import com.christian.nav.NavActivity
import com.christian.nav.me.AbsAboutActivity
import com.christian.nav.nullString
import com.christian.nav.toolbarTitle
import com.christian.util.ChristianUtil
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import kotlinx.android.synthetic.main.about_page_main_activity.*
import kotlinx.android.synthetic.main.about_page_main_activity.fab12
import kotlinx.android.synthetic.main.about_page_main_activity.fab22
import kotlinx.android.synthetic.main.about_page_main_activity.menu_yellow
import kotlinx.android.synthetic.main.activity_nav_detail.*
import kotlinx.android.synthetic.main.nav_activity.*
import org.commonmark.node.Image
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import ren.qinc.markdowneditors.view.EditorActivity
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
class NavDetailActivity : AbsAboutActivity(), AnkoLogger {

    private lateinit var userId: String
    private lateinit var gospelCategory: String
    private lateinit var gospelTime: String
    private lateinit var gospelTitle: String
    private lateinit var gospelContent: String
    private lateinit var gospelAuthor: String
    private lateinit var gospelChurch: String

    private lateinit var meBean: MeBean
    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()
    private var snapshot: DocumentSnapshot? = null

    var isMovingRight: Boolean = true // true不会崩溃，进入nav detail左滑的时候

    private var lastPosition = 0//位置
    private var lastOffset = 0//偏移量

    override fun onDestroy() {
        super.onDestroy()
        stopListening()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_nav_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meRef = firestore.collection(getString(R.string.gospels)).document(gospelTitle)
        startListening()

        fab12.setOnClickListener {
            val intent = Intent(this@NavDetailActivity, EditorActivity::class.java)
            intent.putExtra(ChristianUtil.DOCUMENT_GOSPEL_PATH, gospelTitle)
            startActivity(intent)
        }
        fab22.setOnClickListener {
            val editor = getSharedPreferences("mImg", Context.MODE_PRIVATE).edit()
            editor.putString(gospelTitle, "")
            editor.apply()

            finish()
            firestore.collection(getString(R.string.gospels)).document(gospelTitle)
                    .delete()
                    .addOnSuccessListener {
                        debug { }
                    }
                    .addOnFailureListener { e ->
                        debug { e }
                    }
        }

        if (auth.currentUser?.uid == userId) {
            menu_yellow.visibility = View.VISIBLE
        } else {
            menu_yellow.visibility = View.GONE
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        menu.removeItem(R.id.menu_options_nav)
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

    override fun onItemsCreated(items: MutableList<Any>) {
//        items.add(Author("$gospelAuthor·$gospelChurch·$gospelTime"))
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
        gospelCategory = meBean.desc
        gospelTitle = meBean.name
        gospelContent = meBean.content
//        items.add(Card(gospelContent))

        setMarkdownToTextView(gospelContent)

        gospelAuthor = meBean.author
        gospelChurch = meBean.church
        gospelTime = meBean.time
        userId = meBean.userId
        // 恢复位置
        val sharedPreferences = getSharedPreferences(intent?.extras?.getString(toolbarTitle)
                ?: nullString, Activity.MODE_PRIVATE)
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(sharedPreferences.getInt("lastPosition", 0), sharedPreferences.getInt("lastOffset", 0))
    }

    private fun setMarkdownToTextView(gospelContent: String) {
        val markwon = Markwon.builder(this) // automatically create Glide instance
                .usePlugin(ImagesPlugin.create()) // use supplied Glide instance
                .usePlugin(GlideImagesPlugin.create(Glide.with(this)))
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(HtmlPlugin.create()) //                // if you need more control
                .usePlugin(object : AbstractMarkwonPlugin() {
                    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                        builder.addFactory(Image::class.java) { configuration: MarkwonConfiguration?, props: RenderProps? -> AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER) }
                    }
                })
                .build()
        markwon.setMarkdown(textView, gospelContent)
    }

    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {

//        gospelCategory = intent.getStringExtra(getString(R.string.category))
//                ?: getString(R.string.uncategorized)
        gospelTitle = intent.getStringExtra(getString(R.string.name))
                ?: getString(R.string.no_title)
//        gospelContent = intent.getStringExtra(getString(R.string.content_lower_case))
//                ?: getString(R.string.no_content)
//        gospelAuthor = intent.getStringExtra(getString(R.string.author))
//                ?: getString(R.string.no_author)
//        gospelChurch = intent.getStringExtra(R.string.church_lower_case.toString())
//                ?: getString(R.string.no_church)
//        gospelTime = intent.getStringExtra(getString(R.string.time)) ?: getString(R.string.no_time)
        userId = intent.getStringExtra(getString(R.string.userId)) ?: ""

//        collapsingToolbar.subtitle = gospelAuthor
        collapsingToolbar.title = gospelTitle
    }
}
