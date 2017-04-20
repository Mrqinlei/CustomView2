package com.qinlei.customview2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by ql on 2017/4/20.
 */

public class CustomView2 extends View {
    //控件大小
    private int width;
    private int height;

    private Paint ringNormalPaint;//圆环小节点Paint
    private Paint ringNodePaint;//圆环大节点Paint
    private Paint timeTextPaint;//大的时间Paint
    private Paint millTextPaint;//毫秒时间Paint

    private int ringNormalColor;//圆环普通状态颜色
    private int ringRunningColor;//圆环运行状态颜色
    private int timeTextColor;//大的时间颜色
    private int millTextColor;//毫秒时间颜色

    private int value = -1;//毫秒时间 0-99
    private int time;//秒数

    public CustomView2(Context context) {
        super(context);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ringNormalColor = Color.GRAY;
        ringRunningColor = Color.YELLOW;
        timeTextColor = Color.BLACK;
        millTextColor = Color.GRAY;

        ringNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setColor(ringNormalColor);
        ringNormalPaint.setStrokeWidth(ScreenUtil.dpToPx(this.getContext(), 2));

        ringNodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringNodePaint.setStyle(Paint.Style.STROKE);
        ringNodePaint.setColor(ringNormalColor);
        ringNodePaint.setStrokeWidth(ScreenUtil.dpToPx(this.getContext(), 4));

        timeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timeTextPaint.setColor(timeTextColor);
        timeTextPaint.setStyle(Paint.Style.STROKE);
        timeTextPaint.setTextSize(ScreenUtil.spToPx(getContext(), 70));

        millTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        millTextPaint.setColor(millTextColor);
        millTextPaint.setStyle(Paint.Style.STROKE);
        millTextPaint.setTextSize(ScreenUtil.spToPx(getContext(), 30));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            this.width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            this.height = heightSize;
        }

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //处理value 为 0 往前数为负数的情况
        int value1 = value - 1;
        int value2 = value - 2;
        int value3 = value - 3;
        int value4 = value - 4;
        if (time > 0) {
            if (value == 0) {
                value1 = 99;
                value2 = 98;
                value3 = 97;
                value4 = 96;
            } else if (value == 1) {
                value1 = 99;
                value2 = 98;
                value3 = 97;
                value4 = 0;
            } else if (value == 2) {
                value1 = 99;
                value2 = 98;
                value3 = 0;
                value4 = 1;
            } else if (value == 3) {
                value1 = 99;
                value2 = 0;
                value3 = 1;
                value4 = 2;
            }
        }

        canvas.save();
        //绘制圆环
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                if (value == i || value1 == i || value2 == i || value3 == i || value4 == i) {
                    ringNodePaint.setColor(ringRunningColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 10), ringNodePaint);
                } else {
                    ringNodePaint.setColor(ringNormalColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 10), ringNodePaint);
                }
            } else {
                if (value == i || value1 == i || value2 == i || value3 == i || value4 == i) {
                    ringNormalPaint.setColor(ringRunningColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 6), ringNormalPaint);
                } else {
                    ringNormalPaint.setColor(ringNormalColor);
                    canvas.drawLine(width / 2, 0, width / 2, ScreenUtil.dpToPx(getContext(), 6), ringNormalPaint);
                }
            }
            canvas.rotate(360f / 100f, width / 2, height / 2);
        }
        canvas.restore();

        //定义文本
        String timeString = "00:00";
        String timeMillString = "00";
        //测量文本
        Rect timeRect = new Rect();
        timeTextPaint.getTextBounds("00:00", 0, timeString.length(), timeRect);
        Rect millTimeRect = new Rect();
        millTextPaint.getTextBounds("00", 0, timeMillString.length(), millTimeRect);
        //绘制文本
        int textTotalWidth = timeRect.width() + millTimeRect.width();
        int textTotalHeight = timeRect.height();
        timeString = TimeUtil.formatTime(time);
        canvas.drawText(timeString,
                (this.width - textTotalWidth) / 2,
                (this.height - textTotalHeight) / 2 + timeRect.height(),
                timeTextPaint);
        if (value < 10) {
            if (value > 0) {
                timeMillString = "0" + value;
            } else {
                timeMillString = "00";
            }
        } else {
            timeMillString = "" + value;
        }
        canvas.drawText(timeMillString,
                (this.width - textTotalWidth) / 2 + timeRect.width() + ScreenUtil.spToPx(getContext(), 6),
                (this.height - textTotalHeight) / 2 + timeRect.height(),
                millTextPaint);
    }


    private ValueAnimator valueAnimator;
    private boolean isReset;

    public void start() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, 99);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(1000);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置无限循环
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    value = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    time = time + 1;
                    invalidate();
                }
            });
            valueAnimator.start();
        } else {
            if (isReset) {
                valueAnimator.start();
                isReset = false;
            } else {
                valueAnimator.resume();
            }
        }
    }

    public void stop() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.pause();
        }
    }

    public void reset() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        value = -1;
        time = 0;
        isReset = true;
        invalidate();
    }
}
