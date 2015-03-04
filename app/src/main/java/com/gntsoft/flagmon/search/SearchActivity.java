package com.gntsoft.flagmon.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 3. 2.
 */
public class SearchActivity extends FragmentActivity {


    String [] mapOptionDatas = {"인기순","최근 등록순"};
    String [] listOptionDatas = {"인기순","최근 등록순","거리순"};
    private int mMainContentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

            }

    private void searchMap() {
        //검색하게 수정!!
        getFragmentManager().beginTransaction()
                .replace(R.id.container_search, new MapSearchFragment())
                .commit();

    }


    public void goBack(View v) {
        finish();
    }

    public void doSearch(View v) {
        //구현!!
        PlusToaster.doIt(this, "준비중...");
        mMainContentType = getIntent().getIntExtra(FMConstants.KEY_MAIN_CONTENT_TYPE,0);
        if(mMainContentType == FMConstants.CONTENT_MAP) searchMap();
        else searchList();
    }

    private void searchList() {
        //검색하게 수정!!
        getFragmentManager().beginTransaction()
                .replace(R.id.container_search, new ListSearchFragment())
                .commit();

    }

    public void showSortPopup(View v) {
        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("정렬방식을 선택해주세요.");
        ab.setItems(getItemDatas(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                doSort(whichButton);

            }
        }).setNegativeButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        ab.show();
    }

    private void doSort(int whichButton) {


        switch (whichButton) {
            case 0: sortByPopular();
                break;

            case 1: sortByRecent();
                break;

            case 2:sortByDistance();
                break;

        }
    }

    private void sortByDistance() {
        PlusToaster.doIt(this,"준비중...");
        //구현!!
    }

    private void sortByRecent() {
        PlusToaster.doIt(this,"준비중...");
        //구현!!
    }

    private void sortByPopular() {
        PlusToaster.doIt(this,"준비중...");
        //구현!!
    }


    public String[] getItemDatas() {


        return mMainContentType == FMConstants.CONTENT_MAP? mapOptionDatas:listOptionDatas;
    }


}
