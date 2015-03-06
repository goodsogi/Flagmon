package com.gntsoft.flagmon.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gntsoft.flagmon.util.LoginChecker;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.login.LoginActivity;
import com.gntsoft.flagmon.reply.ReplyActivity;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusToaster;

/**
 * Created by johnny on 15. 2. 27.
 */
public class UserPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        showMap();

    }

    public void goBack(View v) {
        finish();
    }
    public void goToReply(View v) {
        PlusClickGuard.doIt(v);
        Intent intent = new Intent(this, ReplyActivity.class);
        startActivity(intent);

    }

    public void doPin(View v) {

        PlusClickGuard.doIt(v);

        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("로그인 후 스크랩 할 수 있어요. 로그인하시겠습니까?");
        ab.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                goToLogin();
            }
        });
        ab.show();

    }

    public void sortContent(View v) {

        PlusClickGuard.doIt(v);
//정렬 구현!!
        PlusToaster.doIt(this, "준비중...");
    }

    public void addFriend(View v) {

        PlusClickGuard.doIt(v);
        if(LoginChecker.isLogIn(this)) showLoginAlertDialog();
        else showAddFriendAlertDialog();



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
        //구현!!
        PlusToaster.doIt(this, "준비중...");
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
                goToLogin();
            }
        });
        ab.show();
    }

    private void goToLogin() {


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
