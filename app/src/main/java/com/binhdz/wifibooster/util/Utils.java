package com.binhdz.wifibooster.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Created by admin on 4/9/2018.
 */

public class Utils {
    private static final String FONT_OPEN_SANS_BOLD = "fonts/opensans_semibold.ttf";
    public static void setHTMLTextView(TextView tv, String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(html));
        }
    }

    public static String convertPrice(String price) {
        if (price != null && !price.equals("null") && price.length() > 0) {
            String cleanString = price.toString().replaceAll("[.,]", "");
            double parsed = 0;
            try {
                parsed = Double.parseDouble(cleanString.trim());
            } catch (Exception e) {
            }

            String formated = NumberFormat.getInstance().format((parsed));
            return formated;
        } else {
            return "";
        }
    }

    public static String convertPrice(int pri) {
        String price = pri + "";
        return convertPrice(price);
    }

    public static void setListViewHeightBasedOnChildrenforList(Context ctx, ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void removeDoubleClick(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, 1000);
        v.setEnabled(false);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();
        if (v.isShown()) {
            collapse(v);
        } else {
            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int) (targtetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            a.setDuration((int) (targtetHeight + 200));
            v.startAnimation(a);
        }

    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (v.getLayoutParams().height + 200));
        v.startAnimation(a);
    }

    public static void setFontFamily(Activity activity, TextView tv) {
        Typeface type = Typeface.createFromAsset(activity.getAssets(), FONT_OPEN_SANS_BOLD);

        tv.setTypeface(type);
    }
}
