package com.christian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.christian.R;
import com.christian.adapter.MeAdapter;
import com.christian.view.MeItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * author：Administrator on 2017/4/2 00:20
 * email：lanhuaguizha@gmail.com
 */
@ContentView(R.layout.fragment_me)
public class MeFragment extends BaseFragment {
    @ViewInject(R.id.recycler_view_me)
    RecyclerView recyclerViewMe;
    @ViewInject(R.id.toolbar_actionbar)
    Toolbar toolbar;
    private String[] dataSet;
    private static final int DATA_SET_COUNT = 12;
    protected LayoutManagerType currentLayoutManagerType;
    protected RecyclerView.LayoutManager layoutManager;
    private static final int SPAN_COUNT = 2;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.inflateMenu(R.menu.menu_me);
        initData();
        initView();
    }

    private void initView() {
//        toolbar.setTitle(getString(R.string.title_me));

        MeAdapter meAdapter = new MeAdapter(dataSet);
        recyclerViewMe.setAdapter(meAdapter);
        recyclerViewMe.addItemDecoration(new MeItemDecoration((int) getResources().getDimension(R.dimen.activity_horizontal_margin)));
        currentLayoutManagerType = HomeFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(currentLayoutManagerType);
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerViewMe.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerViewMe.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity());
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerViewMe.setLayoutManager(layoutManager);
        recyclerViewMe.scrollToPosition(scrollPosition);
    }

    private void initData() {
        initDataSet();
    }

    /**
     * Generates Strings for RecyclerView's meAdapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataSet() {
        dataSet = new String[DATA_SET_COUNT];
        for (int i = 0; i < DATA_SET_COUNT; i++) {
            dataSet[i] = getString(R.string.next_week);
        }
    }
}
