package com.christian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.christian.R;
import com.christian.adapter.SearchViewAdapter;
import com.christian.helper.SearchViewHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * author：Administrator on 2017/4/2 00:20
 * email：lanhuaguizha@gmail.com
 */

@ContentView(R.layout.fragment_gospel)
public class GospelFragment extends BaseFragment {
    //    @ViewInject(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "GospelFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar toolbar;
    private boolean added;
    @ViewInject(R.id.search_recycler_view)
    private RecyclerView recyclerView;
    @ViewInject(R.id.cardView_search)
    private CardView mCardViewSearch;
    @ViewInject(R.id.et_search)
    private EditText mEtSearch;

    @Event(value = {R.id.tv, R.id.iv_search_back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                //        swipeRefreshLayout.setRefreshing(false);
                break;
            case R.id.iv_search_back:
                SearchViewHelper.handleToolBar(getContext(), mCardViewSearch, mEtSearch);
                break;
            default:
                break;
        }
    }

    public GospelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlusOneFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static GospelFragment newInstance() {
        GospelFragment fragment = new GospelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_search:
                        SearchViewHelper.handleToolBar(getContext(), mCardViewSearch, mEtSearch);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void setSearchResultItem() {
        ArrayList<String> list = new ArrayList<>();
        list.add("优酷");
        list.add("土豆");
        list.add("爱奇艺");
        list.add("哔哩哔哩");
        list.add("youtube");
        list.add("斗鱼");
        list.add("熊猫");
        SearchViewAdapter adapter = new SearchViewAdapter(list, new SearchViewAdapter.IListener() {
            @Override
            public void normalItemClick(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clearItemClick() {
                Toast.makeText(getContext(), "清除历史记录", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
        }
    }

    private void initView() {
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.title_book));
            // Removing more of Book Fragment
            if (!added) {
                toolbar.inflateMenu(R.menu.menu_gospel);
                added = true;
            }
        }
        setSearchResultItem();
    }

    private void initData() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}
