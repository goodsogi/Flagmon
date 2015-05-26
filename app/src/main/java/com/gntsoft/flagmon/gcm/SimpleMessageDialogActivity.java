package com.gntsoft.flagmon.gcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.gntsoft.flagmon.main.MainActivity;


/**
 * 푸시 알림창
 * 
 * @author jeff
 * 
 */
public class SimpleMessageDialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_dialog);

		showMessage();

	}

	/**
	 * 메시지 표시
	 */
	private void showMessage() {
		String message = getIntent().getStringExtra(FMConstants.KEY_GCM_MSG);
		TextView messageView = (TextView) findViewById(R.id.message);
		messageView.setText(message);

	}

	public void closePopup(View v) {
		finish();
	}

	public void doConfirm(View v) {
        moveToRightPlace();
		finish();

	}

	private void moveToRightPlace() {
		String postIdx = getIntent().getStringExtra(FMConstants.KEY_POST_IDX);
		String target = getIntent().getStringExtra(FMConstants.KEY_PUSH_TARGET);


		Intent intent = new Intent(getApplicationContext(),
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(FMConstants.KEY_PUSH_TARGET, target);
		intent.putExtra(FMConstants.KEY_POST_IDX, postIdx);

		getApplicationContext().startActivity(intent);




	}

}
