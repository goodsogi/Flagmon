package com.gntsoft.flagmon.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.detail.PhotoDetailFragment;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 3. 2.
 */
public class SearchActivity extends FMCommonActivity {


    String [] mapOptionDatas = {"인기순","최근 등록순"};
    String [] listOptionDatas = {"인기순","최근 등록순","거리순"};
    private int mMainContentType;
    private String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();


            }

    private void init() {

        mMainContentType = getIntent().getIntExtra(FMConstants.KEY_MAIN_CONTENT_TYPE,0);

        final EditText editText = (EditText) findViewById(R.id.searchInput);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(v);
                }
                //return false라고 하면 키보드가 내려감
                return false;
            }
        });

        editText.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager manager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText, 0);
            }
        }, 100);
    }


    private void searchMap(String sortType) {
        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_KEYWORD, mKeyword);
        bundle.putString(FMConstants.KEY_SORT_TYPE, sortType);
        MapSearchFragment fragment = new MapSearchFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_search, fragment)
                .commit();

    }



    public void doSearch(View v) {
        EditText searchView = (EditText) findViewById(R.id.searchInput);
        mKeyword = searchView.getText().toString();

        if(mKeyword.equals("")) {
            PlusToaster.doIt(this,"검색어를 입력해주세요");
            return;

        }

        showSearchResult(FMConstants.SORT_BY_POPULAR);

    }

    private void showSearchResult(String sortType) {
        if(mMainContentType == FMConstants.CONTENT_MAP) searchMap(sortType);
        else searchList(sortType);
    }

    private void searchList(String sortType) {
        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_KEYWORD, mKeyword);
        bundle.putString(FMConstants.KEY_SORT_TYPE, sortType);
        ListSearchFragment fragment = new ListSearchFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_search, fragment)
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
        //sort 값 수정!!
        showSearchResult(FMConstants.SORT_BY_DISTANCE);

    }

    private void sortByRecent() {
        showSearchResult(FMConstants.SORT_BY_RECENT);

    }

    private void sortByPopular() {
        showSearchResult(FMConstants.SORT_BY_POPULAR);

    }


    public String[] getItemDatas() {


        return mMainContentType == FMConstants.CONTENT_MAP? mapOptionDatas:listOptionDatas;
    }


}
