package com.pluslibrary.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PlusListHeightCalculator {
	/**
	 * ScrollView 안에서 강제로 리스트 높이 지정
	 * 
	 * @param list
	 * @return
	 */
	public static int setListHeightBasedOnChildren(Context context, ListView list, int singleItemHeightDp) {
		ListAdapter listAdapter = list.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		// f를 붙여야 올림이 제대로 계산됨
		int totalHeight = PlusDpPixelConverter.doIt(context, singleItemHeightDp)
				* listAdapter.getCount() + 2* listAdapter.getCount();

		ViewGroup.LayoutParams params = list.getLayoutParams();
		params.height = totalHeight;
		list.setLayoutParams(params);

		return totalHeight;

	}
	
	/**
	 * ScrollView 안에서 강제로 GridView 높이 지정 
	 * @param context
	 * @param gridView
	 * @param columnNo
	 * @param singleItemHeightDp
	 */
	public static int setGridViewHeightBasedOnChildren(Context context, GridView gridView, float columnNo, int singleItemHeightDp) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		// f를 붙여야 올림이 제대로 계산됨
		int totalHeight = (int) (PlusDpPixelConverter.doIt(context, singleItemHeightDp) * Math
				.ceil(listAdapter.getCount() / columnNo)  + 2* listAdapter.getCount());

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight;
		gridView.setLayoutParams(params);
		
		return totalHeight;

	}

    /**
     * ExpandableListView가 확장되었을 때 높이 계산
     * @param context
     * @param elist
     * @param originalHeight
     * @param numOfChildren
     * @param childItemHeightDp
     * @return
     */
    public static int setExpandableListViewHeightExpand(Context context, ExpandableListView elist, int originalHeight, int numOfChildren, int childItemHeightDp) {


        // f를 붙여야 올림이 제대로 계산됨
        int totalHeight = originalHeight + (int) (PlusDpPixelConverter.doIt(context, childItemHeightDp) * numOfChildren) + 2* numOfChildren;

        ViewGroup.LayoutParams params = elist.getLayoutParams();
        params.height = totalHeight;
        elist.setLayoutParams(params);

        return totalHeight;

    }

    /**
     * ExpandableListView가 축소되었을 때 높이 계산
     * @param context
     * @param elist
     * @param originalHeight
     * @param numOfChildren
     * @param childItemHeightDp
     * @return
     */
    public static int setExpandableListViewHeightCollapse(Context context, ExpandableListView elist, int originalHeight, int numOfChildren, int childItemHeightDp) {


        // f를 붙여야 올림이 제대로 계산됨
        int totalHeight = originalHeight - (int) (PlusDpPixelConverter.doIt(context, childItemHeightDp) * numOfChildren)- 2* numOfChildren;

        ViewGroup.LayoutParams params = elist.getLayoutParams();
        params.height = totalHeight;
        elist.setLayoutParams(params);

        return totalHeight;

    }

}
