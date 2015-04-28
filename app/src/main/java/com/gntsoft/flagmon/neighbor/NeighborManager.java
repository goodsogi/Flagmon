package com.gntsoft.flagmon.neighbor;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.FMTabManager;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.search.SearchActivity;
import com.pluslibrary.utils.PlusClickGuard;

/**
 * Created by johnny on 15. 3. 3.
 */
public class NeighborManager implements FMTabManager {

    private final Activity mActivity;
    private int mContentType;

    public NeighborManager(Activity activity) {
        mActivity = activity;
    }

    public void chooseFragment() {
        showNeighborTopBar();
        addListenerToButton();
        showMap();
    }

    private void addListenerToButton() {
        Button naviMenu = (Button) mActivity.findViewById(R.id.naviMenu);
        naviMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu(v);
            }
        });

        Button search = (Button) mActivity.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSearchActivity(v);
            }
        });
    }

    private void toggleMenu(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    showMap();
                }}, 1000);
        } else {
            v.setSelected(true);
            showList();
        }
    }

    private void showMap() {
        mContentType = FMConstants.CONTENT_MAP;
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new MapFragment())
                .commit();
    }

    private void showList() {
        mContentType = FMConstants.CONTENT_LIST;
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.container_main, new ListFragment())
                .commit();


    }

    private void launchSearchActivity(View v) {
        PlusClickGuard.doIt(v);

        Intent intent = new Intent(mActivity, SearchActivity.class);
        intent.putExtra(FMConstants.KEY_MAIN_CONTENT_TYPE, mContentType);
        mActivity.startActivity(intent);
    }


    private void showNeighborTopBar() {
        FrameLayout topBarContainer = (FrameLayout) mActivity.findViewById(R.id.container_top_bar);
        topBarContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View neighborTopBar = inflater.inflate(R.layout.top_bar_neighbor, null);
        topBarContainer.addView(neighborTopBar);
    }


}
