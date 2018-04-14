package com.christian.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.christian.R;

/**
 * author：Administrator on 2017/4/12 21:52
 * email：lanhuaguizha@gmail.com
 */

public class LaunchScreenView extends CoordinatorLayout {

    private View view;

    public LaunchScreenView(Context context) {
        this(context, null);
    }

    public LaunchScreenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LaunchScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadLaunchScreenLayout(context);
    }

    private void loadLaunchScreenLayout(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.launch_screen, this, true);
        animateLaunchScreenView();
    }

    private void animateLaunchScreenView() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha_to_hide);
        this.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
