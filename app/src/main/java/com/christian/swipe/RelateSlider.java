package com.christian.swipe;

public class RelateSlider implements SwipeListener {
    private SwipeBackPage curPage;
    private static final int DEFAULT_OFFSET = 40;
    private int offset = 500;

    RelateSlider(SwipeBackPage curActivity) {
        this.curPage = curActivity;
        //curPage.addListener(this);
    }

    void setOffset(int offset) {
        this.offset = offset;
    }

    void setEnable(boolean enable) {
        if (enable) curPage.addListener(this);
        else curPage.removeListener(this);
    }

    @Override
    public void onScroll(float percent, int px) {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) {
            page.getSwipeBackLayout().setX(Math.min(-offset * Math.max(1 - percent, 0) + DEFAULT_OFFSET, 0));
            if (percent == 0) {
                page.getSwipeBackLayout().setX(0);
            }
        }
    }

    @Override
    public void onEdgeTouch() {

    }

    @Override
    public void onScrollToClose() {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) page.getSwipeBackLayout().setX(0);
    }
}
