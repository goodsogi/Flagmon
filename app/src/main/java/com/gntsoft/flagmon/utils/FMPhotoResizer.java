package com.gntsoft.flagmon.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.gntsoft.flagmon.FMConstants;
import com.pluslibrary.utils.PlusDpPixelConverter;

/**
 * 이미지 크기 축소
 *
 * @author user
 */
public class FMPhotoResizer {

    static private int IMAGE_ALLOW_WIDTH = 142;
    static private int IMAGE_ALLOW_HEIGHT = 142;


    /**
     * 이미지 축소
     *
     * @param imgUrl
     * @return
     */
    public static Bitmap doIt(Activity activity, String imgUrl) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, options);

        final int srcWidth = options.outWidth;
        final int srcHeight = options.outHeight;

        int requiredWidth = PlusDpPixelConverter.doIt(activity, FMConstants.REQUIRED_IMAGE_WIDTH_DP);
        int requiredHeight = PlusDpPixelConverter.doIt(activity, FMConstants.REQUIRED_IMAGE_HEIGHT_DP);

        int inSampleSize = 1;

        if (srcHeight > requiredHeight || srcWidth > requiredWidth) {

            final int halfHeight = srcHeight / 2;
            final int halfWidth = srcWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > requiredHeight
                    && (halfWidth / inSampleSize) > requiredWidth) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        Bitmap sampleSizedBitmap = BitmapFactory.decodeFile(imgUrl, options);

        int dimension = 0;

        //If the bitmap is wider than it is tall
        //use the height as the square crop dimension
        if (sampleSizedBitmap.getWidth() >= sampleSizedBitmap.getHeight()) {
            dimension = sampleSizedBitmap.getHeight();
        }
        //If the bitmap is taller than it is wide
        //use the width as the square crop dimension
        else {
            dimension = sampleSizedBitmap.getWidth();
        }

        return ThumbnailUtils.extractThumbnail(sampleSizedBitmap, dimension, dimension, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);


    }

    public static Bitmap doIt(Activity activity,Bitmap originalBitmap) {

        int requiredWidth = PlusDpPixelConverter.doIt(activity, FMConstants.REQUIRED_IMAGE_WIDTH_DP);
        int requiredHeight = PlusDpPixelConverter.doIt(activity, FMConstants.REQUIRED_IMAGE_HEIGHT_DP);

        int dimension = 0;

        //If the bitmap is wider than it is tall
        //use the height as the square crop dimension
        if (originalBitmap.getWidth() >= originalBitmap.getHeight()) {
            dimension = originalBitmap.getHeight();
        }
        //If the bitmap is taller than it is wide
        //use the width as the square crop dimension
        else {
            dimension = originalBitmap.getWidth();
        }

        Bitmap croppedBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, dimension, dimension, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return Bitmap.createScaledBitmap(croppedBitmap, requiredWidth, requiredHeight, false);

    }


}
