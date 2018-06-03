package com.binhdz.wifibooster.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.binhdz.wifibooster.R;

/**
 * Created by admin on 12/19/2017.
 */

public class BinhdzProgressBar extends View {
    private Paint paint;

    private int width;

    private int height;

    private int result = 0;

    private int padding = 0;

    private int ringColor;

    private int ringProgressColor;

    private int textColor, textColorValue;

    private float textSize, textSizeValue, textSizeTitle;

    private float ringWidth;

    private int max;

    private int progress;

    private int positionStart;

    private boolean textIsShow, isShowTitle, isShowValue, isPrgCPU;

    private String textValue, textTitle;

    public float getTextSizeValue() {
        return textSizeValue;
    }

    public void setTextSizeValue(float textSizeValue) {
        this.textSizeValue = textSizeValue;
    }

    public float getTextSizeTitle() {
        return textSizeTitle;
    }

    public void setTextSizeTitle(float textSizeTitle) {
        this.textSizeTitle = textSizeTitle;
    }

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public boolean isShowValue() {
        return isShowValue;
    }

    public void setShowValue(boolean showValue) {
        isShowValue = showValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    private int style;

    public static final int STROKE = 0;

    public static final int FILL = 1;

    private OnProgressListener mOnProgressListener;

    private int centre;

    private int radius;


    public BinhdzProgressBar(Context context) {

        this(context, null);
    }

    public BinhdzProgressBar(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public BinhdzProgressBar(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        paint = new Paint();

        result = dp2px(100);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);

        ringColor = mTypedArray.getColor(R.styleable.RingProgressBar_ringColor, Color.BLACK);
        ringProgressColor = mTypedArray.getColor(R.styleable.RingProgressBar_ringProgressColor, Color.WHITE);
        textColor = mTypedArray.getColor(R.styleable.RingProgressBar_textColor, Color.BLACK);
        textColorValue = mTypedArray.getColor(R.styleable.RingProgressBar_colorTextValue, Color.BLACK);

        textSize = mTypedArray.getDimension(R.styleable.RingProgressBar_textSize, 16);
        textSizeTitle = mTypedArray.getDimension(R.styleable.RingProgressBar_textSizeTitle, 18);
        textSizeValue = mTypedArray.getDimension(R.styleable.RingProgressBar_textSizeValue, 16);

        textValue = mTypedArray.getString(R.styleable.RingProgressBar_textValue);
        textTitle = mTypedArray.getString(R.styleable.RingProgressBar_textTitle);

        ringWidth = mTypedArray.getDimension(R.styleable.RingProgressBar_ringWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RingProgressBar_max, 100);

        positionStart = mTypedArray.getInteger(R.styleable.RingProgressBar_positionStart, -90);

        textIsShow = mTypedArray.getBoolean(R.styleable.RingProgressBar_textIsShow, true);
        isShowValue = mTypedArray.getBoolean(R.styleable.RingProgressBar_isShowValue, false);
        isShowTitle = mTypedArray.getBoolean(R.styleable.RingProgressBar_isShowTitle, true);
        isPrgCPU = mTypedArray.getBoolean(R.styleable.RingProgressBar_isPrgCPU, false);

        style = mTypedArray.getInt(R.styleable.RingProgressBar_style, 0);

        mTypedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        centre = (getWidth() / 2 );
        radius = (int) (centre - ringWidth / 2);

        drawCircle(canvas);
        drawTextContent(canvas);
        drawProgress(canvas);
    }


    /**
     * Binhdz
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {


        paint.setColor(Color.parseColor("#1A619B"));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(centre, centre, radius, paint);

        paint.setColor(ringColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(centre, centre, radius, paint);
    }

    /**
     * Binhdz
     *
     * @param canvas
     */
    private void drawTextContent(Canvas canvas) {

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT);

        int percent = (int) (((float) progress / (float) max) * 100);
        float textWidth = paint.measureText(percent + "%");

        if (textIsShow && percent != 0 && style == STROKE) {
            if(isPrgCPU){
                canvas.drawText(percent + "°C", centre - textWidth / 2, centre - centre / 7, paint);
            }else
                canvas.drawText(percent + "%", centre - textWidth / 2, centre , paint);
        }

        // text value
        paint.setTextSize(textSizeValue);
        Rect bounds = new Rect();
        paint.setColor(textColorValue);
        paint.getTextBounds(textValue, (int) ringWidth, textValue.length(), bounds);
        float textWidthValue = paint.measureText(textValue);
        float heightTextvalue = radius /5;


        if (isShowValue) {
            canvas.drawText(textValue, (float) (Math.sqrt(centre * centre - heightTextvalue * heightTextvalue) - textWidthValue / 2), centre + heightTextvalue, paint);

        }

        // text Title

        paint.setTextSize(textSizeTitle);
        paint.setColor(textColor);
        float textWidthTitle = paint.measureText(textTitle);
        float heightTitle = radius /2;
        if (isShowTitle()) {
            canvas.drawText(textTitle, (float) (Math.sqrt(centre * centre - heightTitle*heightTitle)+2*ringWidth - textWidthTitle / 2), centre + heightTitle, paint);

        }
    }


    /**
     * Binhdz
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        paint.setStrokeWidth(ringWidth);
        paint.setColor(ringProgressColor);

        RectF strokeOval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        RectF fillOval = new RectF(centre - radius + ringWidth + padding, centre - radius + ringWidth + padding, centre + radius - ringWidth - padding, centre + radius - ringWidth - padding);

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                canvas.drawArc(strokeOval, positionStart, 360 * progress / max, false, paint);
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                if (progress != 0)
                    canvas.drawArc(fillOval, -90, 360 * progress / max, true, paint);
                break;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST)
            width = result;
        else
            width = widthSize;

        if (heightMode == MeasureSpec.AT_MOST)
            height = result;
        else
            height = heightSize;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        padding = dp2px(5);
    }

    public synchronized int getMax() {

        return max;
    }

    public synchronized void setMax(int max) {

        if (max < 0) {
            throw new IllegalArgumentException("The max progress of 0");
        }
        this.max = max;
    }


    public synchronized int getProgress() {

        return progress;
    }


    public synchronized void setProgress(int progress) {

        if (progress < 0) {
            throw new IllegalArgumentException("The progress of 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
        if (progress == max) {
            if (mOnProgressListener != null) {
                mOnProgressListener.progressToComplete();
            }
        }
    }


    public int getRingColor() {

        return ringColor;
    }


    public void setRingColor(int ringColor) {

        this.ringColor = ringColor;
    }


    public int getRingProgressColor() {

        return ringProgressColor;
    }


    public void setRingProgressColor(int ringProgressColor) {

        this.ringProgressColor = ringProgressColor;
    }


    public int getTextColor() {

        return textColor;
    }


    public void setTextColor(int textColor) {

        this.textColor = textColor;
    }

    public float getTextSize() {

        return textSize;
    }


    public void setTextSize(float textSize) {

        this.textSize = textSize;
    }


    public float getRingWidth() {

        return ringWidth;
    }


    public void setRingWidth(float ringWidth) {

        this.ringWidth = ringWidth;
    }


    public int dp2px(int dp) {

        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }


    public interface OnProgressListener {

        void progressToComplete();
    }

    public void setOnProgressListener(OnProgressListener mOnProgressListener) {

        this.mOnProgressListener = mOnProgressListener;
    }
}
