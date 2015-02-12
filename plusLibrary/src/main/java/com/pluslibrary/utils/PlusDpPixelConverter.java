package com.pluslibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
/**
 * dp를 픽셀로 변환 
 * @author user
 *
 */
public class PlusDpPixelConverter {
    

    public static int doIt(Context context, float value) {
        Resources r = context.getResources();
        float pix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                r.getDisplayMetrics());
        return (int) pix;
    }
}
