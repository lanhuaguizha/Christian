//package me.drakeet.support.about.darkmode
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Build
//import android.os.Bundle
//import androidx.fragment.app.FragmentActivity
//import com.afollestad.aesthetic.Aesthetic
//import com.kotlinpermissions.KotlinPermissions
//import me.drakeet.support.about.R
//import java.util.*
//
///**
// * Created by christian on 19-6-7.
// */
//class SunriseSunset {
//
//    private fun sunriseSunset(activity: FragmentActivity) {
//        getLatitudeLongitudePermissions(activity)
//    }
//
//    // start--
//    private fun getLatitudeLongitudePermissions(activity: FragmentActivity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            KotlinPermissions.with(activity) // where this is an FragmentActivity instance
//                    .permissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
//                    .onAccepted { permissions ->
//                        //List of accepted permissions
//                        sunriseSunsetApply(activity)
//                    }
//                    .onDenied { permissions ->
//                        //List of denied permissions
//                    }
//                    .onForeverDenied { permissions ->
//                        //List of forever denied permissions
//                    }
//                    .ask()
//        } else {
//            sunriseSunsetApply(activity)
//        }
//    }
//
//    private fun sunriseSunsetApply(activity: FragmentActivity) {
//        val ll = getLngAndLat(activity)
//
//        val sunriseSunset = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), ll[0], ll[1])
//
//        if (System.currentTimeMillis() in sunriseSunset[0].timeInMillis..sunriseSunset[1].timeInMillis) {
//            // 恢复应用默认皮肤
//            Aesthetic.config {
//                activityTheme(R.style.Christian)
//                isDark(false)
//                textColorPrimary(res = R.color.text_color_primary)
//                textColorSecondary(res = R.color.text_color_secondary)
//                attribute(R.attr.my_custom_attr, res = R.color.default_background_nav)
//                attribute(R.attr.my_custom_attr2, res = R.color.white)
//            }
//        } else {
//            // 夜间模式
//            Aesthetic.config {
//                activityTheme(R.style.ChristianDark)
//                isDark(true)
//                textColorPrimary(res = android.R.color.primary_text_dark)
//                textColorSecondary(res = android.R.color.secondary_text_dark)
//                attribute(R.attr.my_custom_attr, res = R.color.text_color_primary)
//                attribute(R.attr.my_custom_attr2, res = R.color.background_material_dark)
//            }
//        }
//    }
//
//    /**
//     * 获取经纬度
//     */
//    private fun getLngAndLat(context: Context): Array<Double> {
//        var latitude = 0.0
//        var longitude = 0.0
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return arrayOf(latitude, longitude)
//                }
//            }
//            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            if (location != null) {
//                latitude = location.latitude
//                longitude = location.longitude
//            } else {//当GPS信号弱没获取到位置的时候又从网络获取
//                return getLngAndLatWithNetwork()
//            }
//        } else {    //从网络获取经纬度
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, locationListener)
//            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//            if (location != null) {
//                latitude = location.latitude
//                longitude = location.longitude
//            }
//        }
//        return arrayOf(latitude, longitude)
//    }
//
//    //从网络获取经纬度
//    private fun getLngAndLatWithNetwork(): Array<Double> {
//        var latitude = 0.0
//        var longitude = 0.0
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    Activity#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for Activity#requestPermissions for more details.
//                return arrayOf(latitude, longitude)
//            }
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, locationListener)
//        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//        latitude = location.latitude
//        longitude = location.longitude
//        return arrayOf(latitude, longitude)
//    }
//
//    internal var locationListener: LocationListener = object : LocationListener {
//
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
//
//        }
//
//        // Provider被enable时触发此函数，比如GPS被打开
//        override fun onProviderEnabled(provider: String) {
//
//        }
//
//        // Provider被disable时触发此函数，比如GPS被关闭
//        override fun onProviderDisabled(provider: String) {
//
//        }
//
//        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        override fun onLocationChanged(location: Location) {}
//    }
//    // end--
//}