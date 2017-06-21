package com.open.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.open.utislib.base.SizeUtils;

/**
 * Created by vivian on 2017/6/16.
 */

public class CircleProgress extends View {
    private final static String TAG = CircleProgress.class.getSimpleName();

    private Context mContext;

    // view paints
    private Paint firstPaint; // first circle paint
    private Paint secondPaint; // second circle paint
    private int firstColor; // first circle color
    private int secondColor;// second circle color
    private int circleWidth; // 设置圆环的宽度
    int centre; // 圆心
    int radius; // 半径
    RectF ovalRect;

    // progress 0-360
    private int progress = 0;

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setAttrs(attrs);
        initPaints();
    }

    private void setAttrs(AttributeSet attrs) {
        try {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
            firstColor = typedArray.getColor(R.styleable.CircleProgress_firstColor, Color.GREEN);
            secondColor = typedArray.getColor(R.styleable.CircleProgress_secondColor, Color.GREEN);
            circleWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgress_circleWidth, SizeUtils.dp2px(mContext, 30));
            typedArray.recycle();
        } catch (Exception ex) {
            Log.w(TAG, "setAttrs ex", ex);
        }
    }

    private void initPaints() {
        if (firstPaint == null) {
            firstPaint = new Paint();
            firstPaint.setColor(firstColor);
            firstPaint.setStrokeWidth(circleWidth);
            firstPaint.setAntiAlias(true); // 消除锯齿
            firstPaint.setStyle(Paint.Style.STROKE); // 设置空心
        }

        if (secondPaint == null) {
            secondPaint = new Paint();
            secondPaint.setColor(secondColor);
            secondPaint.setStrokeWidth(circleWidth); // 设置圆环的宽度
            secondPaint.setAntiAlias(true); // 消除锯齿
            secondPaint.setStyle(Paint.Style.STROKE); // 设置空心
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        setPositionData();
    }

    private void setPositionData() {
        centre = getWidth() / 2; // 获取圆心的x坐标
        radius = centre - circleWidth / 2;// 半径
        // 用于定义的圆弧的形状和大小的界限
        ovalRect = new RectF(centre - radius,
                centre - radius,
                centre + radius,
                centre + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(centre, centre, radius, firstPaint);
        canvas.drawArc(ovalRect, -90, progress, false, secondPaint);
        //Log.d(TAG,"onDraw: centre = " + centre + "; radius = " + radius);
    }

    /**
     * it must run in ui thread
     *
     * @param value
     */
    public void setProgress(int value) {
        try {
            if (progress == value) {
                Log.w(TAG, "setProgress not changed, since same value");
                return;
            }

            progress = value;
            if (progress >= 360) {
                progress = 0;
            }

            invalidate();
        } catch (Exception ex) {
            Log.w(TAG, "setProgress ex", ex);
        }
    }

}
