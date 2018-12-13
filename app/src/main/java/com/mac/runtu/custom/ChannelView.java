package com.mac.runtu.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mac.runtu.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class ChannelView extends View {
    Paint textPaint;
    Paint rectPaint;
    List<String> datas = null;
    int startX = 0;
    int startY = 0;
    int rectColor = Color.BLACK;
    int rectRound = 5;
    int textColor = Color.BLACK;
    float textSize = 16;
    String TAG = "ChannelView";
    //左边框和文字之间的间隔
    int rectTextPaddingLeft = 0;
    //上边框和文字之间的间隔
    int rectTextPaddingTop = 0;
    //下边框和文字之间的间隔
    int rectTextPaddingBottom = 0;
    //右边框和文字之间的间隔
    int rectTextPaddingRight = 0;

    //边框和文字之间的间隔
    int rectTextPadding = 10;
    //箭头两侧与边框线的间距
    int arrowSpaing = 5;
    String arrowString = "→";
    int correction = 10;
    Rect arrow_Rect = new Rect();

    public ChannelView(Context context) {
        super(context);
    }

    public ChannelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initParam(context, attrs);
        initPaint();
    }

    public ChannelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParam(context, attrs);
        initPaint();
    }


    void initParam(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChannelView);
        rectColor = a.getColor(R.styleable.ChannelView_rectColor, Color.BLACK);
        rectRound = a.getInt(R.styleable.ChannelView_rectRound, 5);
        textColor = a.getColor(R.styleable.ChannelView_textColor, Color.BLACK);
        textSize = a.getDimension(R.styleable.ChannelView_textSize, 16);
        rectTextPadding = a.getInt(R.styleable.ChannelView_rectTextPadding, 0);
        if (rectTextPadding == 0) {
            rectTextPaddingTop = a.getInt(R.styleable.ChannelView_rectTextPaddingTop, 0);
            rectTextPaddingRight = a.getInt(R.styleable.ChannelView_rectTextPaddingRight, 0);
            rectTextPaddingBottom = a.getInt(R.styleable.ChannelView_rectTextPaddingBottom, 0);
            rectTextPaddingLeft = a.getInt(R.styleable.ChannelView_rectTextPaddingLeft, 0);
        }
        a.recycle();
    }


    void initPaint() {
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        rectPaint = new Paint();
        rectPaint.setColor(rectColor);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setAntiAlias(true);//设置线条等图形的抗锯齿
    }

    public void setData(List<String> datas) {
        this.datas = datas;
        Log.i(TAG, "setData");
        startX = 0;
        startY = 0;
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (datas != null) {
            startX = getPaddingLeft();
            startY = getPaddingTop();
            textPaint.getTextBounds(arrowString, 0, arrowString.length(), arrow_Rect);
            for (int i = 0; i < datas.size(); i++) {
                String data = datas.get(i);
                Rect rect = new Rect();
                RectF rectF = new RectF();
                textPaint.getTextBounds(data, 0, data.length(), rect);
                //Log.i(TAG, startX + "," + rect.width() + "," + rect.height());
                rectF.set(startX, startY, startX + rect.width() + rectTextPaddingLeft + rectTextPaddingRight, startY + rect.height() + rectTextPaddingTop + rectTextPaddingBottom);
                canvas.drawRoundRect(rectF, rectRound, rectRound, rectPaint);
                canvas.drawText(data, startX + rectTextPaddingLeft - rect.left, startY + rectTextPaddingTop - rect.top, textPaint);
                if (datas.size() - 1 != i) {
                    canvas.drawText(arrowString,
                            startX + rectTextPaddingLeft + rectTextPaddingRight + rect.width() + arrowSpaing - arrow_Rect.left,
                            startY + rectTextPaddingTop - rect.top + arrow_Rect.top, textPaint);
                }
                startX += rectF.width() + arrow_Rect.width() + arrowSpaing * 2;
               // Log.i(TAG, startX + "");
            }
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (datas != null) {
            Log.i(TAG, "onMeasure");
            int width;
            int height;
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            String dataStr = Arrays.toString(datas.toArray());
            int length = dataStr.length();
            Rect rect = new Rect();
            textPaint.getTextBounds(dataStr, 0, length, rect);
            textPaint.getTextBounds(arrowString, 0, arrowString.length(), arrow_Rect);
            if (widthMode == MeasureSpec.EXACTLY) {
                width = widthSize;
            } else {
                float textWidth = rect.width();
                int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
                width = desired + arrowSpaing * 2 * (datas.size() - 1);
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else {
                float textHeight = rect.height();
                int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
                height = desired;
            }
            setMeasuredDimension(width + (rectTextPaddingLeft + rectTextPaddingRight) * datas.size() + (arrow_Rect.width() + arrowSpaing * 2) * (datas.size() - 1), height + rectTextPaddingTop + rectTextPaddingBottom);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
