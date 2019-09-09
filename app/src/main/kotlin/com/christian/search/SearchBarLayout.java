package com.christian.search;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.christian.R;
import com.christian.search.util.AnimUtils;
import com.christian.search.util.DialerUtils;

/**
 * 首页的搜索条控件
 */
public class SearchBarLayout extends FrameLayout {

    private static final float EXPAND_MARGIN_FRACTION_START = 0.8f;
    private static final int ANIMATION_DURATION = 200;
    private final SearchBarLayout searchBarLayout;
    /* Subclass-visible for testing */
    protected boolean isExpanded = false;
    protected boolean isFadedOut = false;
    private OnKeyListener preImeKeyListener;
    private int topMargin;
    private int bottomMargin;
    private int leftMargin;
    private int rightMargin;
    private float collapsedElevation;
    private View collapsed;
    private View expanded;
    private EditText searchView;
    private View searchIcon;
    private View collapsedSearchBox;
    private View voiceSearchButtonView;
    private View overflowButtonView;
    private View clearButtonView;

    private ValueAnimator animator;

    private Callback callback;

    private boolean voiceSearchEnabled;

    public SearchBarLayout(Context context) {
        this(context, null);
    }

    public SearchBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // xml解析，初始化
        searchBarLayout = (SearchBarLayout) inflate(getContext(), R.layout.search_bar_layout, this);
        initSb();
    }

    public void setPreImeKeyListener(OnKeyListener listener) {
        preImeKeyListener = listener;
    }

    public void setCallback(Callback listener) {
        callback = listener;
    }

    public void setVoiceSearchEnabled(boolean enabled) {
        voiceSearchEnabled = enabled;
        updateVisibility(isExpanded);
    }

    @Override
    protected void onFinishInflate() {


        super.onFinishInflate();
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (preImeKeyListener != null) {
            if (preImeKeyListener.onKey(this, event.getKeyCode(), event)) {
                return true;
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    public void fadeOut() {
        fadeOut(null);
    }

    public void fadeOut(AnimUtils.AnimationCallback callback) {
        AnimUtils.fadeOut(this, ANIMATION_DURATION, callback);
        isFadedOut = true;
    }

    public void fadeIn() {
        AnimUtils.fadeIn(this, ANIMATION_DURATION);
        isFadedOut = false;
    }

    public void fadeIn(AnimUtils.AnimationCallback callback) {
        AnimUtils.fadeIn(this, ANIMATION_DURATION, AnimUtils.NO_DELAY, callback);
        isFadedOut = false;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            setAlpha(1);
            setVisibility(View.VISIBLE);
            isFadedOut = false;
        } else {
            setAlpha(0);
            setVisibility(View.GONE);
            isFadedOut = true;
        }
    }

    public void expand(boolean animate, boolean requestFocus) {
        updateVisibility(true /* isExpand */);

        if (animate) {
            AnimUtils.crossFadeViews(expanded, collapsed, ANIMATION_DURATION);
            animator = ValueAnimator.ofFloat(EXPAND_MARGIN_FRACTION_START, 0f);
            setMargins(EXPAND_MARGIN_FRACTION_START);
            prepareAnimator(animator);
        } else {
            expanded.setVisibility(View.VISIBLE);
            expanded.setAlpha(1);
            setMargins(0f);
            collapsed.setVisibility(View.GONE);
        }

        // Set 9-patch background. This owns the padding, so we need to restore the original values.
        int paddingTop = this.getPaddingTop();
        int paddingStart = this.getPaddingLeft();
        int paddingBottom = this.getPaddingBottom();
        int paddingEnd = this.getPaddingRight();
        setBackgroundResource(R.drawable.search_shadow);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom);
        }

        if (requestFocus) {
            searchView.requestFocus();
        }
        isExpanded = true;
    }

    public void collapse(boolean animate) {
        updateVisibility(false /* isExpand */);

        if (animate) {
            AnimUtils.crossFadeViews(collapsed, expanded, ANIMATION_DURATION);
            animator = ValueAnimator.ofFloat(0f, 1f);
            prepareAnimator(animator);
        } else {
            collapsed.setVisibility(View.VISIBLE);
            collapsed.setAlpha(1);
            setMargins(1f);
            expanded.setVisibility(View.GONE);
        }

        isExpanded = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(collapsedElevation);
        }
        setBackgroundResource(R.drawable.rounded_corner);
    }

    /**
     * Updates the visibility of views depending on whether we will show the expanded or collapsed
     * search view. This helps prevent some jank with the crossfading if we are animating.
     *
     * @param isExpand Whether we are about to show the expanded search box.
     */
    private void updateVisibility(boolean isExpand) {
        int collapsedViewVisibility = isExpand ? View.GONE : View.VISIBLE;
        int expandedViewVisibility = isExpand ? View.VISIBLE : View.GONE;

        searchIcon.setVisibility(collapsedViewVisibility);
        collapsedSearchBox.setVisibility(collapsedViewVisibility);
        if (voiceSearchEnabled) {
            voiceSearchButtonView.setVisibility(collapsedViewVisibility);
        } else {
            voiceSearchButtonView.setVisibility(View.GONE);
        }
        overflowButtonView.setVisibility(collapsedViewVisibility);
        // TODO: Prevents keyboard from jumping up in landscape mode after exiting the
        // SearchFragment when the query string is empty. More elegant fix?
        // mExpandedSearchBox.setVisibility(expandedViewVisibility);
        if (TextUtils.isEmpty(searchView.getText())) {
            clearButtonView.setVisibility(View.GONE);
        } else {
            clearButtonView.setVisibility(expandedViewVisibility);
        }
    }

    private void prepareAnimator(ValueAnimator animator) {
        if (this.animator != null) {
            this.animator.cancel();
        }

        this.animator.addUpdateListener(
                animation -> {
                    final Float fraction = (Float) animation.getAnimatedValue();
                    setMargins(fraction);
                });

        this.animator.setDuration(ANIMATION_DURATION);
        this.animator.start();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public boolean isFadedOut() {
        return isFadedOut;
    }

    /**
     * Assigns margins to the search box as a fraction of its maximum margin size
     *
     * @param fraction How large the margins should be as a fraction of their full size
     */
    private void setMargins(float fraction) {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.topMargin = (int) (topMargin * fraction);
        params.bottomMargin = (int) (bottomMargin * fraction);
        params.leftMargin = (int) (leftMargin * fraction);
        params.rightMargin = (int) (rightMargin * fraction);
        requestLayout();
    }

    /**
     * Listener for the back button next to the search view being pressed
     */
    public interface Callback {

        void onBackButtonClicked();

        void onSearchViewClicked();
    }


    void initSb() {

        // 记得解开
//        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
//        topMargin = params.topMargin;
//        bottomMargin = params.bottomMargin;
//        leftMargin = params.leftMargin;
//        rightMargin = params.rightMargin;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            collapsedElevation = getElevation();
        }

        collapsed = searchBarLayout.findViewById(R.id.search_box_collapsed);
        expanded = searchBarLayout.findViewById(R.id.search_box_expanded);
        searchView = (EditText) expanded.findViewById(R.id.search_view);

        searchIcon = searchBarLayout.findViewById(R.id.search_magnifying_glass);
        collapsedSearchBox = searchBarLayout.findViewById(R.id.search_box_start_search);
        voiceSearchButtonView = searchBarLayout.findViewById(R.id.voice_search_button);
        overflowButtonView = searchBarLayout.findViewById(R.id.dialtacts_options_menu_button);
        clearButtonView = searchBarLayout.findViewById(R.id.search_close_button);

        // Convert a long click into a click to expand the search box. Touch events are also
        // forwarded to the searchView. This accelerates the long-press scenario for copy/paste.
        collapsed.setOnLongClickListener(
                view -> {
                    collapsed.performClick();
                    return false;
                });
        collapsed.setOnTouchListener(
                (v, event) -> {
                    searchView.onTouchEvent(event);
                    searchView.performClick();
                    return false;
                });

        searchView.setOnFocusChangeListener(
                (v, hasFocus) -> {
                    if (hasFocus) {
                        DialerUtils.showInputMethod(v);
                    } else {
                        DialerUtils.hideInputMethod(v);
                    }
                });

        searchView.setOnClickListener(
                v -> {
                    if (callback != null) {
                        callback.onSearchViewClicked();
                    }
                });

        searchView.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        clearButtonView.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        searchBarLayout.findViewById(R.id.search_close_button)
                .setOnClickListener(
                        v -> searchView.setText(null));

        searchBarLayout.findViewById(R.id.search_back_button)
                .setOnClickListener(
                        v -> {
                            if (callback != null) {
                                callback.onBackButtonClicked();
                            }
                        });

        searchIcon.setOnClickListener(v -> expand(true, true));
        collapsedSearchBox.setOnClickListener(v -> expand(true, true));
        searchBarLayout.findViewById(R.id.search_back_button).setOnClickListener(v -> {
            collapse(true);
        });

    }
}
