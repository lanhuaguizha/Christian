package com.christian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.christian.R;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * author：Administrator on 2017/4/17 01:36
 * email：lanhuaguizha@gmail.com
 */

@ContentView(R.layout.activity_home_detial)
public class HomeDetailActivity extends BaseActivity {
    private static String TAG = HomeDetailActivity.class.getSimpleName();
    @ViewInject(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @ViewInject(R.id.app_bar)
    AppBarLayout appBarLayout;
    @ViewInject(R.id.fab)
    FloatingActionButton floatingActionButton;
    @ViewInject(R.id.gospel_detail)
    TextView gospelDetail;
    private Toolbar toolbar;
    private ShareActionProvider mShareActionProvider;
    private boolean isScrollToBottom;
    private boolean hasHidePerformedOnce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initListener() {
        if (toolbar != null)
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        if (nestedScrollView != null)
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                    Log.i(TAG, "scrollX: " + scrollX);
//                    Log.i(TAG, "scrollY: " + scrollY);
//                    Log.i(TAG, "oldScrollX: " + oldScrollX);
//                    Log.i(TAG, "oldScrollY: " + oldScrollY);
//
//                    Log.i(TAG, "v.getChildAt(0).getHeight(): " + v.getChildAt(0).getHeight());
//                    Log.i(TAG, "v.getHeight(): " + v.getHeight());
//                    Log.i(TAG, "v.getScrollY(): " + v.getScrollY());
                    if (v.getChildAt(0).getHeight() == v.getHeight() + v.getScrollY()) {
                        isScrollToBottom = true;
                        scaleToShow();

                    } else if (v.getScrollY() == 0) {
                        isScrollToBottom = false;
                        scaleToShow();

                    } else {
                        if (!hasHidePerformedOnce) {
                            Log.i(TAG, "onScrollChange: perform scale to hide");
                            scaleToHide();
                        }
                    }
                }
            });
    }

    private void scaleToHide() {
        Animation scaleToHide = AnimationUtils.loadAnimation(HomeDetailActivity.this, R.anim.scale_to_hide);
        if (floatingActionButton.getVisibility() == View.VISIBLE) {
            floatingActionButton.startAnimation(scaleToHide);
            scaleToHide.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    hasHidePerformedOnce = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    floatingActionButton.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void scaleToShow() {
        Animation scaleToShow = AnimationUtils.loadAnimation(HomeDetailActivity.this, R.anim.scale_to_show);
        floatingActionButton.startAnimation(scaleToShow);
        scaleToShow.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                hasHidePerformedOnce = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingActionButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Event(value = R.id.fab,
            type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                nestedScrollView.setSmoothScrollingEnabled(true);
                if (!isScrollToBottom) {
                    if (nestedScrollView != null)
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    if (appBarLayout != null)
                        appBarLayout.setExpanded(false, true);
                    isScrollToBottom = true;
                } else {
                    if (nestedScrollView != null)
                        nestedScrollView.fullScroll(View.FOCUS_UP);
                    if (appBarLayout != null)
                        appBarLayout.setExpanded(true, true);
                    isScrollToBottom = false;
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        enableBackButton();
//        restoreScrollPosition();
        loadGospelDetail();
    }

    private void loadGospelDetail() {
        gospelDetail.postDelayed(new Runnable() {
            @Override
            public void run() {
                gospelDetail.setText(textLargeWord);
            }
        }, 100);
        restoreScrollPosition();
    }

    private void enableBackButton() {
        // my_child_toolbar is defined in the layout file
        toolbar = getActionBarToolbar();
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        if (toolbar != null)
            toolbar.setNavigationContentDescription(R.string.go_back);
    }

    private void restoreScrollPosition() {
        appBarLayout.setExpanded(false, false);
        // Must using post.Runnable, this is so suck!
        nestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.scrollTo(0, 0);
            }
        }, 250);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);

        MenuItem shareItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareIntent(createShareIntent());

        // Configure the search info and add any event listeners...
//        shareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menu_share:
//                        Log.i("", "onMenuItemClick: ");
//                        return true;
//                }
//                return false;
//            }
//        });

        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "http://www.baidu.com");
        return shareIntent;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

//    @Event(value = R.id.nested_scroll_view, type = NestedScrollView.OnScrollChangeListener.class)
//    private void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        floatingActionButton.animate().scaleY(Math.abs(scrollY));
//        floatingActionButton.animate().scaleX(Math.abs(scrollY));
//    }

    private String textLargeWord = "private String textLargeWord = \"\\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\n\" +\n" +
            "        \"        \\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\n\" +\n" +
            "        \"        \\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\n\" +\n" +
            "        \"        \\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\n\" +\n" +
            "        \"        \\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t『用最小的工具来奏最大的效果，』这个就是神的律。神从同一阶段里面挑选教会的改革者，如同古时拣选使徒们一般，这些无非为着证明给世人看，这个工作并非出于人，乃是出于神自己。改革家金文阁（Zwingli）起于亚尔帕斯山麓之牧人茅舍，改教时代的神学家麦来赏（Malancthon）来自兵工匠店中，而马丁路德（Martin Luther）生在贫寒之矿工小屋内。人生命史上的第一阶段往往是最重要的，因为里面\\\\n\\\\n\\\\n\\\\n\\n\" +\n" +
            "        \"        \\\\n\\\\t\\\\t「从前引导你们、传神之道给你们的人，你们要想念他们，效法他们的信心，留心看他们为人的结局。」（希伯来十三章七节）本书收集一些历代神所使用属灵人之小传，这些小传虽然是从他们的传记或自传中摘录的重要片段，但仍然保留了传记的形式。我们可以从这些摘录中看出神在这些圣徒身上荣耀的作为及法则，也可以从他们一生的经历中看见奉献生活的榜样。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t这些属灵人小传，包括了教会的各种职事，如司布真、怀特腓、裴尼是福音的使者，慕勒是信心的见证人，慕安德烈是属灵的教师，宾易路师母是十字架信息的使者。因着他们的奉献及专一的跟从主，服事他们的那一世代的人，神也以他的权能印证了他们的工作。\\\\n\\\\n\\n\" +\n" +
            "        \"\\t\\\\t\\\\t\\\\t\\\\t虽然，我们这些蒙召的信徒，在恩赐及服事上与他们同样带领不尽相同。但是神在奉献的圣徒身上带领的法则，仍然不变。就如从他们在那一世代中如何突破当时属灵的困境，并为当代的教会开创了光明的途径；他们的经历，我们可以认识真正奉献的道路，以及如何在现今的时代专一跟从主的法则。但愿这些小传，再次激励我们奉献的心智，一同起来寻求神的旨意，在这一世代中活出神荣耀的见证。\\\\n\\\\n\\n\" +\n" +
            "        \"    \\\\t\\\\t\\\\t\\\\t马丁路德小传\\\\n\\\\n\\n\" +\n" +
            "";
}
