package com.gntsoft.flagmon.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gntsoft.flagmon.FMCommonActivity;
import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;

/**
 * Created by johnny on 15. 4. 21.
 */
public class AlarmSettingActivity extends FMCommonActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        addListenerToCheckbox();
    }

    private void addListenerToCheckbox() {
        CheckBox flagAlarm = (CheckBox) findViewById(R.id.flagAlarm);
        flagAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setFlagAlarm(isChecked);
            }
        });

        CheckBox treasureFind = (CheckBox) findViewById(R.id.treasureFind);
        treasureFind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTreasureFind(isChecked);
            }
        });

        CheckBox treasureAlarm = (CheckBox) findViewById(R.id.treasureAlarm);
        treasureAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTreasureAlarm(isChecked);
            }
        });

        CheckBox replyAlarm = (CheckBox) findViewById(R.id.replyAlarm);
        replyAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setReplyAlarm(isChecked);
            }
        });

        CheckBox soundEnabled = (CheckBox) findViewById(R.id.soundEnabled);
        soundEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSoundEnabled(isChecked);
            }
        });

        CheckBox vibrationMode = (CheckBox) findViewById(R.id.vibrationMode);
        vibrationMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setVibrationMode(isChecked);
            }
        });

        CheckBox vibration = (CheckBox) findViewById(R.id.vibration);
        vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setVibrationEnabled(isChecked);
            }
        });




    }

    private void setTreasureAlarm(boolean isChecked) {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_TREASURE_ALARM, isChecked);
        e.commit();
    }

    private void setReplyAlarm(boolean isChecked) {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_REPLY_ALARM, isChecked);
        e.commit();
    }

    private void setSoundEnabled(boolean isChecked) {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_GCM_SOUND_ENABLED, isChecked);
        e.commit();
    }

    private void setVibrationMode(boolean isChecked) {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_VIBRATION_MODE, isChecked);
        e.commit();
    }

    private void setVibrationEnabled(boolean isChecked) {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_GCM_VIBRATION_ENABLED, isChecked);
        e.commit();
    }

    private void setTreasureFind(boolean isChecked) {
        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_FLAG_ALARM, isChecked);
        e.commit();
    }

    private void setFlagAlarm(boolean isChecked) {

        SharedPreferences sharedPreference = getSharedPreferences(
                FMConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreference.edit();
        e.putBoolean(FMConstants.KEY_FLAG_ALARM, isChecked);
        e.commit();
    }
}
