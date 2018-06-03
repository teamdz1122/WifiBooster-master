package com.binhdz.wifibooster.view.loading;

import android.content.Context;

/**
 * Created by admin on 12/18/2017.
 */

public class DensityUtil {
    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
