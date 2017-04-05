package com.christian.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.christian.Constant;
import com.christian.R;
import com.christian.fragment.HomeFragment;

/**
 * Created by Administrator on 2017/4/5.
 */

public class BaseActivity extends AppCompatActivity {

    private String currentFragmentTag;

//    public void switchFragment(FragmentManager fragmentManager, Fragment fragment, String tag, boolean isAddBackStack) {
//        currentFragmentTag = tag;
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        //fragment 替换了当前layout容器中的由com.onstar.cn.framework.R.id.fragment_container	标识的fragment.
//        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
//        fragmentTransaction.addToBackStack(null);//通过调用 addToBackStack(), replace事务被保存到back stack,
//        if (fragmentManager.findFragmentByTag(tag) == null) {
//            log.d("Initialize fragment,add fragment to backStack");
//            if (isAddBackStack) {
//                fragments.add(fragment);
//            }
//        }
//        fragmentTransaction.commitAllowingStateLoss();
//    }
}
