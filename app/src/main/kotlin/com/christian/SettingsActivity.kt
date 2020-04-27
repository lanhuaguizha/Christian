package com.christian

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.christian.nav.me.AboutActivity
import com.christian.nav.switchNightModeIsOn
import com.christian.nav.toolbarTitle
import com.christian.swipe.SwipeBackActivity
import com.christian.util.fixToolbarElevation
import com.christian.util.setToolbarAsUp
import kotlinx.android.synthetic.main.nav_item_me_for_setting_static.*
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : SwipeBackActivity() {
    private var isOn = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setToolbarAsUp(this, settings_toolbar, getString(R.string.settings))
        fixToolbarElevation(settings_abl)

        sharedPreferences = getSharedPreferences(switchNightModeIsOn
                , Activity.MODE_PRIVATE)

        clear_cache.setOnClickListener {
            var intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val packageURI = Uri.parse("package:" + "com.christian")
            intent = intent.setData(packageURI)
            startActivity(intent)
        }

        about_us.setOnClickListener {
            val i = Intent(this, AboutActivity::class.java)
            i.putExtra(toolbarTitle, getString(R.string.about))
            startActivity(i)
        }

        dark_mode.setOnClickListener {
            if (sharedPreferences.getBoolean("isOn", false)) {
                switch_nav_item_small.isChecked = false
                // 恢复应用默认皮肤
                shouldEnableDarkMode(DarkModeConfig.NO)
                isOn = false
                sharedPreferences.edit { putBoolean("isOn", isOn) }
            } else {
                switch_nav_item_small.isChecked = true
                // 夜间模式
                shouldEnableDarkMode(DarkModeConfig.YES)
                isOn = true
                sharedPreferences.edit { putBoolean("isOn", isOn) }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        switch_nav_item_small.isChecked = sharedPreferences.getBoolean("isOn", false)
    }

    enum class DarkModeConfig {
        YES,
        NO,
        FOLLOW_SYSTEM
    }

    private fun shouldEnableDarkMode(darkModeConfig: DarkModeConfig) {
        when (darkModeConfig) {
            DarkModeConfig.YES -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DarkModeConfig.NO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DarkModeConfig.FOLLOW_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }


}