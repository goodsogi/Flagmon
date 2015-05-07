package com.gntsoft.flagmon.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.GcmBroadcastReceiver;
import com.gntsoft.flagmon.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * 푸시 받은 메시지 처리
 *
 * @author jeff
 */
public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();
    private static final String LOG_TAG = "FMPush";
    private static final int NOTIFICATION_ID = 11;
    /**
     * 친구 플래그 발견 : 푸쉬 target = 01
     보물상자 발견 :    푸쉬 target = 05
     보물상자 알림 :    푸쉬 target = 02
     덧글 알림     :    푸쉬 target = 03
     */
    private static final String FIND_FRIEND_FLAG = "01";
    private static final String FIND_TREASURE = "05";
    private static final String TREASURE_ALARM = "02";
    private static final String REPLAY_ALARM = "03";
    // id로 생성자를 만들 필요 없음
    public static final String PROJECT_ID = "268246742383";// 이전 버전과 달리 project
    private boolean mSoundEnabled;

    private boolean mVibrationEnabled;

    private Vibrator mVibrator;

    private MediaPlayer mMediaPlayer;
    private boolean mFlagAlarmEnabled;
    private boolean mTreasureFindAlarmEnabled;
    private boolean mReplayAlarmEnabled;
    private boolean mTreasureAlarmEnabled;

    public GcmIntentService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 생성자에서는 getApplicationContext()가 null pointer 오류 발생
        SharedPreferences sharedPreference = getApplicationContext()
                .getSharedPreferences(FMConstants.PREF_NAME,
                        Context.MODE_PRIVATE);
        // 푸시를 사용하지 않는다고 설정되어 있는 경우
        if (!sharedPreference.getBoolean(FMConstants.KEY_PUSH_ENABLE, true))
            return;
        mSoundEnabled = sharedPreference.getBoolean(
                FMConstants.KEY_GCM_SOUND_ENABLED, true);
        mVibrationEnabled = sharedPreference.getBoolean(
                FMConstants.KEY_GCM_VIBRATION_ENABLED, true);


        mTreasureAlarmEnabled = sharedPreference.getBoolean(
                FMConstants.KEY_TREASURE_ALARM, true);

        mReplayAlarmEnabled = sharedPreference.getBoolean(
                FMConstants.KEY_REPLY_ALARM, true);

        mTreasureFindAlarmEnabled = sharedPreference.getBoolean(
                FMConstants.KEY_TREASURE_FIND_ALARM, true);

        mFlagAlarmEnabled = sharedPreference.getBoolean(
                FMConstants.KEY_FLAG_ALARM, true);


        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(),
                R.raw.push_sound);

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            /*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                Log.w(LOG_TAG,
                        "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                Log.w(LOG_TAG, "Deleted messages on server: "
                        + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {


                //푸시 종류 처리


                // 푸시로 받은 쿠폰 인덱스 처리
                selectPopup(extras);
                // showNotification(getApplicationContext(), encodedTitle);
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void selectPopup(Bundle extras) {

        String target = extras.get("target").toString();
        if(target.equals(FIND_FRIEND_FLAG) && !mFlagAlarmEnabled) return;
        if(target.equals(FIND_TREASURE) && !mTreasureFindAlarmEnabled) return;
        if(target.equals(TREASURE_ALARM) && !mTreasureAlarmEnabled) return;
        if(target.equals(REPLAY_ALARM) && !mReplayAlarmEnabled) return;

        if(target.equals(FIND_FRIEND_FLAG)) launchGcmPopup(extras);
        if(target.equals(FIND_TREASURE)) launchTreasurePopup(extras);
        if(target.equals(TREASURE_ALARM)) launchGcmPopup(extras);
        if(target.equals(REPLAY_ALARM)) launchGcmPopup(extras);


    }


    private void launchTreasurePopup(Bundle extras) {


        String postIdx = extras.get("index").toString();

        //디코딩 필요!!
        String message = extras.get("message").toString();


        Intent intent = new Intent(getApplicationContext(),
                GcmTreasurePopupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);
        intent.putExtra(FMConstants.KEY_GCM_MSG, message);

        getApplicationContext().startActivity(intent);

    }

    private void launchGcmPopup(Bundle extras) {


        String postIdx = extras.get("index").toString();

        //디코딩 필요!!
        String message = extras.get("message").toString();


        Intent intent = new Intent(getApplicationContext(),
                SimpleMessageDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);
        intent.putExtra(FMConstants.KEY_GCM_MSG, message);

        getApplicationContext().startActivity(intent);

    }



//	private void showNotification(Context context, String message) {
//		// 인텐트 수정 필요!!
//		Intent notificationIntent = new Intent(context, MainActivity.class);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//				notificationIntent, 0);
//
//		NotificationManager nm = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(
//				this).setSmallIcon(R.drawable.ic_launcher)
//				.setContentTitle(context.getString(R.string.app_name))
//				.setContentText(message).setContentIntent(contentIntent);
//		Notification n = builder.build();
//
//		// 알림 소리와 진동 처리
//		playSoundVibration();
//
//		nm.notify(NOTIFICATION_ID, n);
//
//	}

    /**
     * 푸시 수신시 알림음이나 진동
     */
    private void playSoundVibration() {
        // 진동
        if (mVibrationEnabled) {
            mVibrator.vibrate(new long[]{200, 200, 200, 200}, -1);
        }

        // 소리
        if (mSoundEnabled) {
            mMediaPlayer.start();
        }

    }

}
