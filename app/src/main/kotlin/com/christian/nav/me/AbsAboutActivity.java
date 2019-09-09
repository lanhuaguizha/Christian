package com.christian.nav.me;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.christian.multitype.*;
import com.christian.swipe.SwipeBackActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import me.drakeet.multitype.MultiTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author drakeet
 */
public abstract class AbsAboutActivity extends SwipeBackActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private LinearLayout headerContentLayout;

    private List<Object> items;
    private MultiTypeAdapter adapter;
    private TextView slogan, version;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private RecyclerView recyclerView;
    private @Nullable
    ImageLoader imageLoader;
    private boolean initialized;
    private @Nullable
    OnRecommendationClickedListener onRecommendationClickedListener;
    private @Nullable
    OnContributorClickedListener onContributorClickedListener;

    protected abstract void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version);

    protected abstract void onItemsCreated(@NonNull List<Object> items);

    protected void onTitleViewCreated(@NonNull CollapsingToolbarLayout collapsingToolbar) {
    }

    public void setImageLoader(@NonNull ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        if (initialized) {
            adapter.notifyDataSetChanged();
        }
    }

    public @Nullable
    ImageLoader getImageLoader() {
        return imageLoader;
    }

    @Override
    public final void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public final void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public final void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }


    public abstract class DoubleClickListener implements View.OnClickListener {
        private static final long DOUBLE_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
                onDoubleClick(v);
            }
            lastClickTime = currentTimeMillis;
        }

        public abstract void onDoubleClick(View v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.christian.R.layout.about_page_main_activity);
        toolbar = findViewById(com.christian.R.id.toolbar);
        ImageView icon = findViewById(com.christian.R.id.icon);
        slogan = findViewById(com.christian.R.id.slogan);
        version = findViewById(com.christian.R.id.version);
        collapsingToolbar = findViewById(com.christian.R.id.collapsing_toolbar);
        headerContentLayout = findViewById(com.christian.R.id.header_content_layout);
        onTitleViewCreated(collapsingToolbar);
        onCreateHeader(icon, slogan, version);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        onApplyPresetAttrs();
        recyclerView = findViewById(com.christian.R.id.list);


        toolbar.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        adapter = new MultiTypeAdapter();
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Card.class, new CardViewBinder());
        adapter.register(Line.class, new LineViewBinder());
        adapter.register(Contributor.class, new ContributorViewBinder(this));
        adapter.register(License.class, new LicenseViewBinder());
        adapter.register(Recommendation.class, new RecommendationViewBinder(this));
        items = new ArrayList<>();
        onItemsCreated(items);
        adapter.setItems(items);
        adapter.setHasStableIds(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(adapter));
        recyclerView.setAdapter(adapter);
        initialized = true;
    }

    private void onApplyPresetAttrs() {
        final TypedArray a = obtainStyledAttributes(com.christian.R.styleable.AbsAboutActivity);
        Drawable headerBackground = a.getDrawable(com.christian.R.styleable.AbsAboutActivity_aboutPageHeaderBackground);
        if (headerBackground != null) {
            setHeaderBackground(headerBackground);
        }
        Drawable headerContentScrim = a.getDrawable(com.christian.R.styleable.AbsAboutActivity_aboutPageHeaderContentScrim);
        if (headerContentScrim != null) {
            setHeaderContentScrim(headerContentScrim);
        }
        @ColorInt
        int headerTextColor = a.getColor(com.christian.R.styleable.AbsAboutActivity_aboutPageHeaderTextColor, -1);
        if (headerTextColor != -1) {
            setHeaderTextColor(headerTextColor);
        }
        Drawable navigationIcon = a.getDrawable(com.christian.R.styleable.AbsAboutActivity_aboutPageNavigationIcon);
        if (navigationIcon != null) {
            setNavigationIcon(navigationIcon);
        }
        a.recycle();
    }

    /**
     * Use {@link #setHeaderBackground(int)} instead.
     *
     * @param resId The resource id of header background
     */
    @Deprecated
    public void setHeaderBackgroundResource(@DrawableRes int resId) {
        setHeaderBackground(resId);
    }

    private void setHeaderBackground(@DrawableRes int resId) {
        setHeaderBackground(ContextCompat.getDrawable(this, resId));
    }

    private void setHeaderBackground(@NonNull Drawable drawable) {
        ViewCompat.setBackground(headerContentLayout, drawable);
    }

    /**
     * Set the drawable to use for the content scrim from resources. Providing null will disable
     * the scrim functionality.
     *
     * @param drawable the drawable to display
     */
    public void setHeaderContentScrim(@NonNull Drawable drawable) {
        collapsingToolbar.setContentScrim(drawable);
    }

    public void setHeaderContentScrim(@DrawableRes int resId) {
        setHeaderContentScrim(ContextCompat.getDrawable(this, resId));
    }

    public void setHeaderTextColor(@ColorInt int color) {
        collapsingToolbar.setCollapsedTitleTextColor(color);
        slogan.setTextColor(color);
        version.setTextColor(color);
    }

    /**
     * Set the icon to use for the toolbar's navigation button.
     *
     * @param resId Resource ID of a drawable to set
     */
    public void setNavigationIcon(@DrawableRes int resId) {
        toolbar.setNavigationIcon(resId);
    }

    public void setNavigationIcon(@NonNull Drawable drawable) {
        toolbar.setNavigationIcon(drawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void setTitle(@NonNull CharSequence title) {
        collapsingToolbar.setTitle(title);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public CollapsingToolbarLayout getCollapsingToolbar() {
        return collapsingToolbar;
    }

    public List<Object> getItems() {
        return items;
    }

    public MultiTypeAdapter getAdapter() {
        return adapter;
    }

    public TextView getSloganTextView() {
        return slogan;
    }

    public TextView getVersionTextView() {
        return version;
    }

    public void setOnRecommendationClickedListener(@Nullable OnRecommendationClickedListener listener) {
        this.onRecommendationClickedListener = listener;
    }

    public @Nullable
    OnRecommendationClickedListener getOnRecommendationClickedListener() {
        return onRecommendationClickedListener;
    }

    public void setOnContributorClickedListener(@Nullable OnContributorClickedListener listener) {
        this.onContributorClickedListener = listener;
    }

    public @Nullable
    OnContributorClickedListener getOnContributorClickedListener() {
        return onContributorClickedListener;
    }
}

