package com.binhdz.wifibooster.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.util.Constants;
import com.binhdz.wifibooster.util.PreferenceUtil;
import com.binhdz.wifibooster.view.BigWindowView;
import com.binhdz.wifibooster.view.WindowView;

import java.text.DecimalFormat;

/**
 * Created by admin on 1/6/2018.
 */

public class MyWindowManager implements Constants {

    private static MyWindowManager instance;
    private WindowManager mWindowManager;
    private WindowView mBigWindowView;
    private WindowManager.LayoutParams windowParams;

    private TextView tvWlanTx;
    private TextView tvWlanRx;
    private TextView  tvW_1, tvW_2;

    private long rxtxTotal = 0;
    private long mobileRecvSum = 0;
    private long mobileSendSum = 0;
    private long wlanRecvSum = 0;
    private long wlanSendSum = 0;
    private long exitTime = 0;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");

    public static MyWindowManager getInstance() {
        if (instance == null) {
            instance = new MyWindowManager();
        }
        return instance;
    }


    @SuppressLint("CutPasteId")
    public void createWindow(final Context context) {
        final WindowManager windowManager = getWindowManager(context);
        if (windowParams == null) {
            windowParams = getWindowParams(context);
        }


        if (mBigWindowView == null) {
            mBigWindowView = new BigWindowView(context);
            Drawable background = getCurrentBgDrawable(context);
            setViewBg(background);
            if (PreferenceUtil.getSingleton(context).getBoolean(SP_LOC)) {
                setOnTouchListener(context, mBigWindowView);
            } else {
                setOnTouchListener(windowManager, context, mBigWindowView);
            }

            windowManager.addView(mBigWindowView, windowParams);
        }
      /*  tvMobileRx = (TextView) mBigWindowView.findViewById(R.id.tvMobileRx);
        tvMobileTx = (TextView) mBigWindowView.findViewById(R.id.tvMobileTx);*/
        tvWlanRx = (TextView) mBigWindowView.findViewById(R.id.tvWlanRx);
        tvWlanTx = (TextView) mBigWindowView.findViewById(R.id.tvWlanTx);

        tvW_1 = (TextView) mBigWindowView.findViewById(R.id.tv_w_l);
        tvW_2 = (TextView) mBigWindowView.findViewById(R.id.tv_w_2);
    }

    private Drawable getCurrentBgDrawable(Context context) {
        Drawable background;
        int bgId;
        if (PreferenceUtil.getSingleton(context).getBoolean(SP_BG, false)) {
            bgId = R.drawable.trans_bg;
        } else {
            bgId = R.drawable.float_bg;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = context.getDrawable(bgId);
        } else {
            background = context.getResources().getDrawable(bgId);
        }
        return background;
    }

    public void initData() {
        mobileRecvSum = TrafficStats.getMobileRxBytes();
        mobileSendSum = TrafficStats.getMobileTxBytes();
        wlanRecvSum = TrafficStats.getTotalRxBytes() - mobileRecvSum;
        wlanSendSum = TrafficStats.getTotalTxBytes() - mobileSendSum;
        rxtxTotal = TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes();
    }

    private WindowManager.LayoutParams getWindowParams(Context context) {
        final WindowManager windowManager = getWindowManager(context);
        Point sizePoint = new Point();
        windowManager.getDefaultDisplay().getSize(sizePoint);
        int screenWidth = sizePoint.x;
        int screenHeight = sizePoint.y;
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        windowParams.gravity = Gravity.START | Gravity.TOP;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        int x = PreferenceUtil.getSingleton(context).getInt(SP_X, -1);
        int y = PreferenceUtil.getSingleton(context).getInt(SP_Y, -1);
        if (x == -1 || y == -1) {
            x = screenWidth;
            y =  80;
        }
        windowParams.x = x;
        windowParams.y = y;
        return windowParams;
    }

    private void setOnTouchListener(final WindowManager windowManager, final Context context, final WindowView windowView) {
        windowView.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = windowParams.x;
                        paramY = windowParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        windowParams.x = paramX + dx;
                        windowParams.y = paramY + dy;
                        // update vitri
                        windowManager.updateViewLayout(windowView, windowParams);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if ((System.currentTimeMillis() - exitTime) < CHANGE_DELAY) {
                            createWindow(context);
                            return true;
                        } else {
                            exitTime = System.currentTimeMillis();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void setOnTouchListener(final Context context, final WindowView windowView) {
        windowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if ((System.currentTimeMillis() - exitTime) < CHANGE_DELAY) {
                            createWindow(context);
                            return true;
                        } else {
                            exitTime = System.currentTimeMillis();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public void setViewBg(Drawable background) {
        if (mBigWindowView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mBigWindowView.setBackground(background);
            } else {
                mBigWindowView.setBackgroundDrawable(background);
            }
        }

    }

    private void removeWindow(Context context, WindowView windowView) {
        if (windowView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(windowView);
        }
    }

    public void removeAllWindow(Context context) {
        removeWindow(context, mBigWindowView);

        mBigWindowView = null;

    }

    public void updateViewData(Context context) {

        long tempSum = TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes();
        long rxtxLast = tempSum - rxtxTotal;
        double totalSpeed = rxtxLast * 1000 / TIME_SPAN;
        rxtxTotal = tempSum;
        long tempMobileRx = TrafficStats.getMobileRxBytes();
        long tempMobileTx = TrafficStats.getMobileTxBytes();
        long tempWlanRx = TrafficStats.getTotalRxBytes() - tempMobileRx;
        long tempWlanTx = TrafficStats.getTotalTxBytes() - tempMobileTx;
        long mobileLastRecv = tempMobileRx - mobileRecvSum;
        long mobileLastSend = tempMobileTx - mobileSendSum;
        long wlanLastRecv = tempWlanRx - wlanRecvSum;
        long wlanLastSend = tempWlanTx - wlanSendSum;
        double mobileRecvSpeed = mobileLastRecv * 1000 / TIME_SPAN;
        double mobileSendSpeed = mobileLastSend * 1000 / TIME_SPAN;
        double wlanRecvSpeed = wlanLastRecv * 1000 / TIME_SPAN;
        double wlanSendSpeed = wlanLastSend * 1000 / TIME_SPAN;
        mobileRecvSum = tempMobileRx;
        mobileSendSum = tempMobileTx;
        wlanRecvSum = tempWlanRx;
        wlanSendSum = tempWlanTx;
        if (mBigWindowView != null) {
            if (wlanRecvSpeed >= 0d && wlanRecvSpeed > mobileRecvSpeed) {
                tvWlanRx.setText(showSpeed(wlanRecvSpeed));
            } else if (mobileRecvSpeed >= 0d && wlanRecvSpeed < mobileRecvSpeed) {
                tvWlanRx.setText(showSpeed(mobileRecvSpeed));
            }

            if (wlanSendSpeed >= 0d &&  wlanSendSpeed> mobileSendSpeed) {
                tvWlanTx.setText(showSpeed(wlanSendSpeed));
            } else if (mobileSendSpeed >= 0d &&  wlanSendSpeed<mobileSendSpeed) {
                tvWlanTx.setText(showSpeed(mobileSendSpeed));
            }
        }


    }

    private String showSpeed(double speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB/s";
        } else {
            speedString = showFloatFormat.format(speed / 1024d) + "KB/s";
        }
        return speedString;
    }

    public boolean isWindowShowing() {
        return mBigWindowView != null; /*|| mSmallWindowView != null;*/
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public int getWindowX() {
        return windowParams.x;
    }

    public int getWindowY() {
        return windowParams.y;
    }

  /*  public void fixWindow(Context context, boolean yes) {
        if (yes) {
            setOnTouchListener(context, mSmallWindowView == null ? mBigWindowView : mSmallWindowView, mSmallWindowView == null ? SMALL_WINDOW_TYPE : BIG_WINDOW_TYPE);
        } else {
            setOnTouchListener(getWindowManager(context), context, mSmallWindowView == null ? mBigWindowView : mSmallWindowView, mSmallWindowView == null ? SMALL_WINDOW_TYPE : BIG_WINDOW_TYPE);
        }
    }*/

}
