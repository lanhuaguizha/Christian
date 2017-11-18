package com.christian.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.christian.R;
import com.christian.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.content_fragment)
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;

    @Event(R.id.fab)
    private void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        setUpButton();
        initListener();
    }

    private void setUpButton() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        // Get a support ActionBar corresponding to this mToolbar
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationContentDescription(R.string.go_back);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().finish();
                getActivity().onBackPressed();
            }
        });
    }
}
