package com.christian.nav

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.content.edit
import com.afollestad.aesthetic.Aesthetic
import com.christian.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

/**
 * Created by christian on 19-6-12.
 * 获取地理位置、
 */
class NavService : Service(), AnkoLogger {

    private lateinit var locationManager: LocationManager

    override fun onBind(intent: Intent): IBinder {
        sunriseSunsetReal()
        return NavBinder()
    }

    override fun onUnbind(intent: Intent): Boolean {
        locationManager.removeUpdates(locationListener)
        stopSelf()
        return super.onUnbind(intent)
    }

    inner class NavBinder : Binder() {
        val navService: NavService
            get() = this@NavService
    }

    // start--
    private fun sunriseSunsetReal() {
        val ll = getLngAndLat()
        info { ll[0] }
        info { ll[1] }
        val sunriseSunset = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), ll[0], ll[1])
        val sharedPreferences = getSharedPreferences("christian", Activity.MODE_PRIVATE)
        sharedPreferences.edit {
            putString("sunrise", sunriseSunset[0].time.toString())
            putString("sunset", sunriseSunset[1].time.toString())
        }
        info { "Sunrise at: " + sunriseSunset[0].time }
        info { "Sunset at: " + sunriseSunset[1].time }
        info { "currentTimeMillis: ${Date(System.currentTimeMillis())}" }
        info { "-----------------" }
        info { "Sunrise at: " + sunriseSunset[0].timeInMillis }
        info { "Sunset at: " + sunriseSunset[1].timeInMillis }
        info { "currentTimeMillis: ${System.currentTimeMillis()}" }
        if (System.currentTimeMillis() in sunriseSunset[0].timeInMillis..sunriseSunset[1].timeInMillis) {
            // 恢复应用默认皮肤
            info { "恢复应用默认皮肤" }
            Aesthetic.config {
                activityTheme(R.style.Christian)
                isDark(false)
                textColorPrimary(res = R.color.text_color_primary)
                textColorSecondary(res = R.color.text_color_secondary)
                attribute(R.attr.my_custom_attr, res = R.color.default_background_nav)
                attribute(R.attr.my_custom_attr2, res = R.color.white)
                attribute(R.attr.my_custom_attr3, res = R.color.colorOverlay)
            }
        } else {
            // 夜间模式
            info { "夜间模式" }
            Aesthetic.config {
                activityTheme(R.style.ChristianDark)
                isDark(true)
                textColorPrimary(res = android.R.color.primary_text_dark)
                textColorSecondary(res = android.R.color.secondary_text_dark)
                attribute(R.attr.my_custom_attr, res = R.color.text_color_primary)
                attribute(R.attr.my_custom_attr2, res = R.color.background_material_dark)
                attribute(R.attr.my_custom_attr3, res = R.color.fui_transparent)
            }
        }
    }

    /**
     * 获取经纬度
     */
    private fun getLngAndLat(): Array<Double> {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return arrayOf(latitude, longitude)
                }
            }
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            } else {//当GPS信号弱没获取到位置的时候又从网络获取
                return getLngAndLatWithNetwork()
            }
        } else {    //从网络获取经纬度
            return getLngAndLatWithNetwork()
        }
        return arrayOf(latitude, longitude)
    }

    //从网络获取经纬度
    private fun getLngAndLatWithNetwork(): Array<Double> {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return arrayOf(latitude, longitude)
            }
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, locationListener)
        } catch (e: Exception) {
            return arrayOf(latitude, longitude)
        }
        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        latitude = location.latitude
        longitude = location.longitude
        return arrayOf(latitude, longitude)
    }

    private var locationListener: LocationListener = object : LocationListener {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {}
    }
    // end--

}