package com.christian

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.christian.nav.NavActivity
import com.christian.nav.me.AboutActivity
import com.christian.nav.toolbarTitle
import com.christian.swipe.SwipeBackActivity
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        about_us.setOnClickListener {
            val i = Intent(this, AboutActivity::class.java)
            startActivity(i)
        }
    }
}