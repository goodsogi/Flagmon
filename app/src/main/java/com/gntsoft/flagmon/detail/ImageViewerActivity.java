package com.gntsoft.flagmon.detail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gntsoft.flagmon.FMConstants;
import com.gntsoft.flagmon.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pluslibrary.utils.PlusLogger;
import com.pluslibrary.utils.PlusScreenHeightFinder;
import com.pluslibrary.utils.PlusScreenMeasureTool;

/**
 * 이미지뷰어, 핀치줌 처리(원본보다 작아지지는 않음)
 * 
 * @author jeff
 * 
 */
public class ImageViewerActivity extends Activity implements OnTouchListener {

	// 이미지 다운로드
	protected ImageLoader mImageLoader;
	protected DisplayImageOptions mOption;

	@SuppressWarnings("unused")
	private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

	// These matrices will be used to scale points of the image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// The 3 states (events) which the user is trying to perform
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	private int mScreenHeight;
	private int mScreenWidth;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_viewer);
		mScreenHeight = PlusScreenHeightFinder.doIt(this);
		// 화면크기 계산
		mScreenWidth = PlusScreenMeasureTool.getScreenWidth(this);
		showImage();
	}

	private void showImage() {
		// UIL 초기화
		mImageLoader = ImageLoader.getInstance();

		mOption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(false)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		// 이미지
		final ImageView img = (ImageView) findViewById(R.id.img);

        //샘플처리
        img.setImageBitmap(getScaledRatioBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.sandarapark)));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        img.setLayoutParams(params);
        img.setOnTouchListener(ImageViewerActivity.this);


        //!!주석해제
//		mImageLoader.loadImage(
//				getIntent().getStringExtra(FMConstants.KEY_IMAGE_URL),
//				mOption, new ImageLoadingListener() {
//
//					@Override
//					public void onLoadingStarted(String arg0, View arg1) {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					public void onLoadingFailed(String arg0, View arg1,
//							FailReason arg2) {
//
//					}
//
//					@Override
//					public void onLoadingComplete(String arg0, View arg1,
//							Bitmap bitmap) {
//
//						img.setImageBitmap(getScaledRatioBitmap(bitmap));
//
//						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//								FrameLayout.LayoutParams.MATCH_PARENT,
//								FrameLayout.LayoutParams.MATCH_PARENT);
//
//						img.setLayoutParams(params);
//						img.setOnTouchListener(ImageViewerActivity.this);
//
//					}
//
//					@Override
//					public void onLoadingCancelled(String arg0, View arg1) {
//						// TODO Auto-generated method stub
//
//					}
//				});

	}

	protected Bitmap getScaledRatioBitmap(Bitmap bitmap) {

		return Bitmap.createScaledBitmap(bitmap, mScreenWidth, mScreenHeight,
				false);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.MATRIX);
		float scale;

		dumpEvent(event);
		// Handle touch events here...

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: // first finger down only
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			Log.d("today", "mode=DRAG"); // write to LogCat
			mode = DRAG;
			break;

		case MotionEvent.ACTION_UP: // first finger lifted

		case MotionEvent.ACTION_POINTER_UP: // second finger lifted

			mode = NONE;
			Log.d("today", "mode=NONE");
			break;

		case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

			oldDist = spacing(event);
			Log.d("today", "oldDist=" + oldDist);
			if (oldDist > 5f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d("today", "mode=ZOOM");
			}
			break;

		case MotionEvent.ACTION_MOVE:

			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y); // create the transformation in the matrix
									// of points
			} else if (mode == ZOOM) {
				// pinch zooming
				float newDist = spacing(event);

				// 스케일된 이미지가 원래 이미지보다 작은 경우 스케일 중단
				float[] values = new float[9];
				matrix.getValues(values);
				Log.d("today", "x:" + values[Matrix.MSCALE_X] + " y:"
						+ values[Matrix.MSCALE_Y]);

				if (values[Matrix.MSCALE_X] < MIN_ZOOM) {
					matrix.setScale(1, 1);
					return false;
				}

				if (newDist > 5f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist; // setting the scaling of the
												// matrix...if scale > 1 means
												// zoom in...if scale < 1 means
												// zoom out

					PlusLogger.doIt(String.valueOf(scale));
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		view.setImageMatrix(matrix); // display the transformation on screen

		return true; // indicate event was handled
	}

	/*
	 * --------------------------------------------------------------------------
	 * Method: spacing Parameters: MotionEvent Returns: float Description:
	 * checks the spacing between the two fingers on touch
	 * ----------------------------------------------------
	 */

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/*
	 * --------------------------------------------------------------------------
	 * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
	 * Description: calculates the midpoint between the two fingers
	 * ------------------------------------------------------------
	 */

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
				"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);

		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}

		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";");
		}

		sb.append("]");
		Log.d("Touch Events ---------", sb.toString());
	}
}
