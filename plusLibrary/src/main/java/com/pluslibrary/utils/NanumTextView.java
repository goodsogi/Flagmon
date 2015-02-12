package com.pluslibrary.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class NanumTextView extends TextView {

    public NanumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "nanum.ttf");
        this.setTypeface(tf);
    }

    public NanumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "nanum.ttf");
        this.setTypeface(tf);
    }

    public NanumTextView(Context context) {
        super(context);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "nanum.ttf");
        this.setTypeface(tf);
    }

}