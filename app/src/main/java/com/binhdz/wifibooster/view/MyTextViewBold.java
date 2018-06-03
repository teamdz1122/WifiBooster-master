package com.binhdz.wifibooster.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.jar.Attributes;

/**
 * Created by admin on 12/14/2017.
 */

public class MyTextViewBold extends android.support.v7.widget.AppCompatTextView {
    public MyTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }
}
