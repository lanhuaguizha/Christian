package com.christian.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * author：Administrator on 2017/5/31 12:02
 * email：lanhuaguizha@gmail.com
 */

public class CustomViewPage extends ViewPager {
    public CustomViewPage(Context context) {
        super(context);
    }

    public CustomViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Removing the slide page effect of page switching
//    @Override
//    public void setCurrentItem(int item) {
//        super.setCurrentItem(item, false);
//    }
}
