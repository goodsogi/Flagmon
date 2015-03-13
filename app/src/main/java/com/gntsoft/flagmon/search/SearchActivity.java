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



}
