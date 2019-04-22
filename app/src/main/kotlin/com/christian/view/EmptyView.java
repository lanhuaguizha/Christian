package com.christian.view;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.christian.R;

/**
 * 空页面，用于显示友好提示：“请待会再试一次，我们的后台出问题了，并且本页面没有缓存任何数据，所以显示此页面给您”
 */

public class EmptyView extends ConstraintLayout {
    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadView(context);
    }

    private void loadView(Context context) {
        // 第三个参数直接使用true，可以少去解析布局后的addView代码，解析布局耗费性能还是这里使用Anko替代今后
        LayoutInflater.from(context).inflate(R.layout.empty_view, this, true);
    }


}
