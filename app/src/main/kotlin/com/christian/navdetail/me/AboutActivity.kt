package com.christian.navdetail.me

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.christian.BuildConfig
import com.christian.R
import me.drakeet.support.about.*
import me.drakeet.support.about.provided.PicassoImageLoader


class AboutActivity : AbsAboutActivity(), OnRecommendationClickedListener, OnContributorClickedListener {
    override fun onItemsCreated(items: MutableList<Any>) {
        items.add(Category("介绍与帮助"))
//        items.add(Card(getString(R.string.card_content)))
//        items.add(Card(getString(R.string.card_content)))

        items.add(Category("Developers"))
        items.add(Contributor(R.drawable.me, "drakeet", "Developer & designer", "http://weibo.com/drak11t"))
        items.add(Contributor(R.drawable.me, "黑猫酱", "Developer", "https://drakeet.me"))
        items.add(Contributor(R.drawable.me, "小艾大人", "Developer"))

        items.add(Category("我独立开发的应用"))
        items.add(Recommendation(
                0, getString(R.string._Gen),
//                0, getString(R.string.pure_writer),
                "https://storage.recommend.wetolink.com/storage/app_recommend/images/YBMHN6SRpZeF0VHbPZWZGWJ2GyB6uaPx.png",
                "com.drakeet.purewriter",
                "快速的纯文本编辑器，我们希望写作能够回到原本的样子：纯粹、有安全感、随时、绝对不丢失内容、具备良好的写作体验。",
                "https://www.coolapk.com/apk/com.drakeet.purewriter",
                "2017-10-09 16:46:57",
                "2017-10-09 16:46:57", 2.93, true)
        )
        items.add(Recommendation(
                1, getString(R.string._Gen),
//                1, getString(R.string.pure_mosaic),
                "http://image.coolapk.com/apk_logo/2016/0831/ic_pure_mosaic-2-for-16599-o_1argff2ddgvt1lfv1b3mk2vd6pq-uid-435200.png",
                "me.drakeet.puremosaic",
                "专注打码的轻应用，包含功能：传统马赛克、毛玻璃效果、选区和手指模式打码，更有创新型高亮打码和 LowPoly 风格马赛克。只为满足一个纯纯的打码需求，让打码也能成为一种赏心悦目。",
                "https://www.coolapk.com/apk/me.drakeet.puremosaic",
                "2017-10-09 16:46:57",
                "2017-10-09 16:46:57", 2.64, true)
        )
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setImageLoader(PicassoImageLoader())
        onRecommendationClickedListener = this@AboutActivity
        onContributorClickedListener = this@AboutActivity
    }

    override fun onCreateHeader(icon: ImageView, slogan: TextView, version: TextView) {
        icon.setImageResource(R.mipmap.ic_launcher)
        slogan.setText(getString(R.string.app_name))
        version.setText("v" + BuildConfig.VERSION_NAME)
    }
}
