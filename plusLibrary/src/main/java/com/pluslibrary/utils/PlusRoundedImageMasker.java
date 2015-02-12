package com.pluslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.pluslibrary.R;

/**
 * 이미지 마스크 처리(둥근 사각형)
 * 
 * @author jeff
 * 
 */
public class PlusRoundedImageMasker {
	public static void doIt(Context context, ImageView imageView,
			Bitmap mainImage) {
		Canvas canvas = new Canvas();

		Bitmap tmpMask = BitmapFactory.decodeResource(context.getResources(),

		R.drawable.mask);

		Bitmap mask = Bitmap.createScaledBitmap(tmpMask, mainImage.getWidth(),
				mainImage.getHeight(), true);

		tmpMask.recycle();

		tmpMask = null;

		Bitmap result = Bitmap.createBitmap(mainImage.getWidth(),

		mainImage.getHeight(), Bitmap.Config.ARGB_8888);

		canvas.setBitmap(result);

		Paint paint = new Paint();

		paint.setFilterBitmap(false);

		canvas.drawBitmap(mainImage, 0, 0, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		// DST_IN, DST_OUT(mask reverse)

		canvas.drawBitmap(mask, 0, 0, paint);

		paint.setXfermode(null);

		Drawable d = new BitmapDrawable(context.getResources(), result);

		//imageView.setImageResource(decoId);
		//imageView.setBackgroundDrawable(d);
		imageView.setImageDrawable(d);
		imageView.invalidate();
	}

}
