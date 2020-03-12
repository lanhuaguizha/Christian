package com.christian

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.christian.nav.NavActivity
import com.christian.nav.me.AboutActivity
import com.christian.nav.toolbarTitle
import com.christian.swipe.SwipeBackActivity
import com.christian.util.fixToolbarElevation
import com.christian.util.setToolbarAsUp
import kotlinx.android.synthetic.main.nav_item_me_for_setting_static.*
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : SwipeBackActivity() {
    private var isOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setToolbarAsUp(this, settings_toolbar, getString(R.string.settings))
        fixToolbarElevation(settings_abl)

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
            if (isOn) {
                switch_nav_item_small.isChecked = false
                // 恢复应用默认皮肤
//                        Aesthetic.config {
//                            activityTheme(R.style.Christian)
//                            isDark(false)
//                            textColorPrimary(res = R.color.text_color_primary)
//                            textColorSecondary(res = R.color.text_color_secondary)
//                            attribute(R.attr.my_custom_attr, res = R.color.default_background_nav)
//                            attribute(R.attr.my_custom_attr2, res = R.color.white)
//                        }
                isOn = false
            } else {
                switch_nav_item_small.isChecked = true
                // 夜间模式
//                        Aesthetic.config {
//                            activityTheme(R.style.ChristianDark)
//                            isDark(true)
//                            textColorPrimary(res = android.R.color.primary_text_dark)
//                            textColorSecondary(res = android.R.color.secondary_text_dark)
//                            attribute(R.attr.my_custom_attr, res = R.color.text_color_primary)
//                            attribute(R.attr.my_custom_attr2, res = R.color.background_material_dark)
//                        }
                isOn = true
            }
        }
    }


}