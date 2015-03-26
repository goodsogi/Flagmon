package com.gntsoft.flagmon.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnny on 15. 2. 27.
 */
public class UserPageActivity extends FMCommonActivity implements
        PlusOnGetDataListener {

    private static final int ADD_FRIEND = 0;
    private int mtotalUserPost;

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case ADD_FRIEND:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().equals("success") ? "친구 추가되었습니다" : "친구 추가되지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                }
                break;

        }

    }

    public void addFriend(View v) {

        PlusClickGuard.doIt(v);
        if (LoginChecker.isLogIn(this)) showLoginAlertDialog();
        else showAddFriendAlertDialog();


    }

    public void toggleMenu(View v) {

        if (v.isSelected()) {
            v.setSelected(false);
            showMap();
        } else {
            v.setSelected(true);
            showList();
        }

    }

    public int getTotalUserPost() {


        return mtotalUserPost;
    }

    public void setTotalUserPost(int totalUserPost) {


        mtotalUserPost = totalUserPost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        showUserName();
        showMap();

    }

    private void showUserName() {
        TextView userName = (TextView) findViewById(R.id.userName);
        userName.setText(getIntent().getStringExtra(FMConstants.KEY_USER_NAME));
    }

    private void showAddFriendAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("친구 신청 하시겠습니까?");
        ab.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                requestAddFriend();
            }
        });
        ab.show();
    }

    private void requestAddFriend() {
        //특정 사용자 아이디등 처리!!
        //수정!!
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        //postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_NEIGHBOR));
        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(this, this, false).execute(ADD_FRIEND,
                FMApiConstants.ADD_FRIEND, new PlusInputStreamStringConverter(),
                postParams);
    }

    private void showLoginAlertDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("친구 신청은 로그인후 가능합니다. 로그인 하시겠습니까?");
        ab.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                launchLoginActivity();
            }
        });
        ab.show();
    }

    private void launchLoginActivity() {


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showList() {


        getFragmentManager().beginTransaction()
                .replace(R.id.container_user, new UserListFragment())
                .commit();


    }

    private void showMap() {


        getFragmentManager().beginTransaction()
                .replace(R.id.container_user, new UserMapFragment())
                .commit();

    }
}
