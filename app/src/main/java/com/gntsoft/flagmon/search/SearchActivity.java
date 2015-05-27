package com.gntsoft.flagmon.search;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.pluslibrary.utils.PlusToaster;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by johnny on 15. 3. 2.
 */
public class SearchActivity extends FMCommonActivity {


    private int mMainContentType;
    private String mKeyword;

    public void doSearch(View v) {
        EditText searchView = (EditText) findViewById(R.id.searchInput);
        String keyword = searchView.getText().toString();

        if (keyword.equals("")) {
            PlusToaster.doIt(this, "검색어를 입력해주세요");
            return;

        }


        try {
            mKeyword = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        saveKeyword(keyword);
        showSearchResult(FMConstants.SORT_BY_POPULAR);

    }

    private void saveKeyword(String keyword) {
        SharedPreferences sharedPreferences = getSharedPreferences(FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FMConstants.PREF_KEY_KEYWORD, keyword);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();


    }

    private void init() {

        mMainContentType = getIntent().getIntExtra(FMConstants.KEY_MAIN_CONTENT_TYPE, 0);

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
                InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText, 0);
            }
        }, 100);

        editText.setText(getKeyword());
    }

    private String getKeyword() {
        SharedPreferences sharedPreferences = getSharedPreferences(FMConstants.PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(FMConstants.PREF_KEY_KEYWORD,"");
    }

    private void searchMap(String sortType) {
        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_KEYWORD, mKeyword);
        bundle.putString(FMConstants.KEY_SORT_TYPE, sortType);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_search, fragment)
                .commit();

    }

    private void showSearchResult(String sortType) {
        if (mMainContentType == FMConstants.CONTENT_MAP) searchMap(sortType);
        else searchList(sortType);
    }

    private void searchList(String sortType) {
        Bundle bundle = new Bundle();
        bundle.putString(FMConstants.KEY_KEYWORD, mKeyword);
        bundle.putString(FMConstants.KEY_SORT_TYPE, sortType);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_search, fragment)
                .commit();

    }


}
