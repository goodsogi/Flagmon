package com.gntsoft.flagmon.utils;

import android.widget.AbsListView;

import com.pluslibrary.utils.PlusLogger;

/**
 * Created by johnny on 15. 5. 22.
 */
public class PlusListScrollListener implements AbsListView.OnScrollListener {
    private final ListScrollBottomListener mListener;
    private int mVisibleThreshold = 10;
    private int mCurrentPage = 0;
    private int mPreviousTotal = 0;
    private boolean mIsLoading = true;

    public PlusListScrollListener(ListScrollBottomListener listener) {
        mListener = listener;
    }
    public PlusListScrollListener(ListScrollBottomListener listener,int visibleThreshold) {
        mListener = listener;
        this.mVisibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (mIsLoading) {
            if (totalItemCount > mPreviousTotal) {
                mIsLoading = false;
                mPreviousTotal = totalItemCount;
                mCurrentPage++;
            }
        }
        PlusLogger.doIt("scroll", " totalItemCount: "+totalItemCount + " visibleItemCount: " +visibleItemCount + " firstVisibleItem: " +firstVisibleItem);

        if (!mIsLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem)) {
       // if (!mIsLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            mListener.onScrollBottom(mCurrentPage);
            mIsLoading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
