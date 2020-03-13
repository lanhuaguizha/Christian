package com.christian

import android.os.Bundle
import com.christian.nav.toolbarTitle
import com.christian.swipe.SwipeBackActivity
import com.christian.util.fixToolbarElevation
import com.christian.util.setToolbarAsUp
import kotlinx.android.synthetic.main.settings_activity.*

class HistoryAndMyArticlesActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_and_my_articles_activity)
        setToolbarAsUp(this, settings_toolbar, intent.getStringExtra(toolbarTitle))
        fixToolbarElevation(settings_abl)
    }
}