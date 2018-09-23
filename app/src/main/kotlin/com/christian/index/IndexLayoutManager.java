package com.christian.index;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.christian.index.Subscriber;

/**
 * Created by edgar on 5/31/15.
 */
public class IndexLayoutManager implements Subscriber {

    private RecyclerView indexList;

    // CONSTRUCTOR _________________________________________________________________________________
    public IndexLayoutManager(RelativeLayout rl) {
    }

    // UTIL ________________________________________________________________________________________
    private Boolean isHeader(TextView prev, TextView act) {
        if (isSameChar(prev.getText().charAt(0), act.getText().charAt(0))) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private Boolean isSameChar(char prev, char curr) {
        if (Character.toLowerCase(prev) == Character.toLowerCase(curr)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private void updatePosBasedOnReferenceList(RecyclerView referenceRv) {
        View firstVisibleView = referenceRv.getChildAt(0);
        int actual = referenceRv.getChildPosition(firstVisibleView);
        ((LinearLayoutManager) indexList.getLayoutManager()).scrollToPositionWithOffset(actual, firstVisibleView.getTop() + 0);
    }

    // SUBSCRIBER INTERFACE ________________________________________________________________________
    @Override
    public void update(RecyclerView referenceList, float dx, float dy) {

    }

    // GETTERS AND SETTERS _________________________________________________________________________
    public void setIndexList(RecyclerView indexList) {
        this.indexList = indexList;
    }

    /**/
    private char getIndexContext(TextView index) {
        return index.getText().charAt(0);
    }

}
