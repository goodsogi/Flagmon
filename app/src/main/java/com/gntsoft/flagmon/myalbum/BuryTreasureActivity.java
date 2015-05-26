package com.gntsoft.flagmon.myalbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.server.FMApiConstants;
import com.gntsoft.flagmon.server.ServerResultModel;
import com.gntsoft.flagmon.server.ServerResultParser;
import com.gntsoft.flagmon.server.UserMonParser;
import com.gntsoft.flagmon.utils.LoginChecker;
import com.pluslibrary.server.PlusHttpClient;
import com.pluslibrary.server.PlusInputStreamStringConverter;
import com.pluslibrary.server.PlusOnGetDataListener;
import com.pluslibrary.utils.PlusClickGuard;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusOnClickListener;
import com.pluslibrary.utils.PlusToaster;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

/**
 * Created by johnny on 15. 4. 20.
 */
public class BuryTreasureActivity extends FMCommonActivity implements
        PlusOnGetDataListener {

    private final int BURY_TREASURE = 33;
    private final int GET_USER_MON = 133;
    private int mUserMonInt;
    private int mTotalMon;

    public void showChooseShareTypeBar(View v) {
        PlusClickGuard.doIt(v);

        LinearLayout barChooseShareType = (LinearLayout) findViewById(R.id.barChooseShareType);
        barChooseShareType.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        LinearLayout barChooseShareType = (LinearLayout) findViewById(R.id.barChooseShareType);
        if (barChooseShareType.isShown()) barChooseShareType.setVisibility(View.GONE);
        else super.onBackPressed();
    }

    @Override
    public void onSuccess(Integer from, Object datas) {
        if (datas == null)
            return;
        switch (from) {
            case BURY_TREASURE:
                ServerResultModel model = new ServerResultParser().doIt((String) datas);
                PlusToaster.doIt(this, model.getResult().equals("success") ? "보물을 묻었습니다" : "보물을 묻지 못했습니다");
                if (model.getResult().equals("success")) {
                    //추가 액션??
                    finish();
                }
                break;
            case GET_USER_MON:
                String mon = new UserMonParser().doIt((String) datas);
                mUserMonInt = Integer.parseInt(mon);
                showUserMon(mon);
        }

    }

    public String getShareType() {
        CheckBox checkboxShareAll = (CheckBox) findViewById(R.id.checkboxShareAll);
        CheckBox checkboxShareFriend = (CheckBox) findViewById(R.id.checkboxShareFriend);
        if (checkboxShareAll.isChecked()) return FMConstants.DATA_TAB_NEIGHBOR;
        if (checkboxShareFriend.isChecked()) return FMConstants.DATA_TAB_FRIEND;
        return FMConstants.DATA_TAB_NEIGHBOR;
    }

    public void completeBuryTreasure(View v) {

        if (mTotalMon > mUserMonInt) {
            showOverMonAlertdialog();
            return;

        }

        String idx = getIntent().getStringExtra(FMConstants.KEY_POST_IDX);

        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        //postParams.add(new BasicNameValuePair("list_menu", FMConstants.DATA_TAB_FRIEND));
        postParams.add(new BasicNameValuePair("photo_idx", idx));
        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }
        postParams.add(new BasicNameValuePair("list_menu", getShareType()));
        postParams.add(new BasicNameValuePair("coin", String.valueOf(mTotalMon)));


        new PlusHttpClient(this, this, false).execute(BURY_TREASURE,
                FMApiConstants.BURY_TREASURE, new PlusInputStreamStringConverter(),
                postParams);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bury_treasure);

        initNumberWheel();

        initShareType();

        getUserMonOnServer();
    }

    private void showUserMon(String mon) {
        TextView userMon = (TextView) findViewById(R.id.userMon);
        userMon.setText(mon);
    }

    private void showOverMonAlertdialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        ab.setTitle("보유한 mon 개수를 초과했습니다. 다시 선택해주세요");
        ab.setNeutralButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        ab.show();
    }

    private void getUserMonOnServer() {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        if (LoginChecker.isLogIn(this)) {
            postParams.add(new BasicNameValuePair("key", getUserAuthKey()));
        }


        new PlusHttpClient(this, this, false).execute(GET_USER_MON,
                FMApiConstants.GET_USER_MON, new PlusInputStreamStringConverter(),
                postParams);

    }

    private void initShareType() {
        final Button barShareBtn = (Button) findViewById(R.id.barShareBtn);

        final Button completeBuryTreasure = (Button) findViewById(R.id.completeBuryTreasure);

        final CheckBox checkboxShareAll = (CheckBox) findViewById(R.id.checkboxShareAll);
        final CheckBox checkboxShareFriend = (CheckBox) findViewById(R.id.checkboxShareFriend);


        final LinearLayout barCheckboxShareAll = (LinearLayout) findViewById(R.id.barCheckboxShareAll);
        final LinearLayout barCheckboxShareFriend = (LinearLayout) findViewById(R.id.barCheckboxShareFriend);

        final LinearLayout barChooseShareType = (LinearLayout) findViewById(R.id.barChooseShareType);


        barCheckboxShareAll.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                checkboxShareAll.setChecked(true);
                barChooseShareType.setVisibility(View.GONE);
            }
        });

        barCheckboxShareFriend.setOnClickListener(new PlusOnClickListener() {
            @Override
            protected void doIt() {
                checkboxShareFriend.setChecked(true);
                barChooseShareType.setVisibility(View.GONE);
            }
        });

        checkboxShareAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    barShareBtn.setBackgroundResource(R.drawable.p32_bt_0001);
                    if (checkboxShareFriend.isChecked()) checkboxShareFriend.setChecked(false);
                    if (!completeBuryTreasure.isEnabled()) completeBuryTreasure.setEnabled(true);


                }
            }
        });

        checkboxShareFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    barShareBtn.setBackgroundResource(R.drawable.p32_bt_0003);
                    if (checkboxShareAll.isChecked()) checkboxShareAll.setChecked(false);
                    if (!completeBuryTreasure.isEnabled()) completeBuryTreasure.setEnabled(true);

                }
            }
        });


        checkboxShareAll.setChecked(true);


    }

    private void initNumberWheel() {
        final WheelView monCountWheel = (WheelView) findViewById(R.id.monCountWheel);
        monCountWheel.setViewAdapter(new NumericWheelAdapter(this, 1, 23));
        monCountWheel.setCyclic(true);

        final WheelView boxCountWeel = (WheelView) findViewById(R.id.boxCountWeel);
        boxCountWeel.setViewAdapter(new NumericWheelAdapter(this, 1, 23));
        boxCountWeel.setCyclic(true);

        monCountWheel.setCurrentItem(0);
        boxCountWeel.setCurrentItem(0);

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                //처리!!
                mTotalMon = (monCountWheel.getCurrentItem() + 1) * (boxCountWeel.getCurrentItem() + 1);
                PlusLogger.doIt("monCountWheel: " + monCountWheel.getCurrentItem() + " boxCountWeel: " + boxCountWeel.getCurrentItem());
                setMinusMon();

            }
        };


        monCountWheel.addScrollingListener(scrollListener);
        boxCountWeel.addScrollingListener(scrollListener);

        mTotalMon = (monCountWheel.getCurrentItem() + 1) * (boxCountWeel.getCurrentItem() + 1);
        PlusLogger.doIt("monCountWheel: " + monCountWheel.getCurrentItem() + " boxCountWeel: " + boxCountWeel.getCurrentItem());
        setMinusMon();


    }

    private void setMinusMon() {
        TextView minusMon = (TextView) findViewById(R.id.minusMon);
        minusMon.setText("몬 " + mTotalMon + "개 차감");
    }


}
