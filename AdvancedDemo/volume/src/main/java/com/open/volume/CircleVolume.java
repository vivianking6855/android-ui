package com.open.volume;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.open.applib.activity.BasicView;
import com.open.utislib.base.SizeUtils;

/**
 * Created by vivian on 2017/6/20.
 */

public class CircleVolume extends BasicView {
    private final static String TAG = CircleVolume.class.getSimpleName();

    private int unitCount; // unit count in circle
    private int unitSplit; // each unit split
    private int unitSize; // unit size, 360/unitCount - unitSplit
    private int normalColor; // normal color
    private int focusColor; // focus color
    private int strokeWidth; // stroke width
    private Paint arcPaint; // arc paint
    private RectF arcRect;
    private RectF iconRect;
    private int iconRadius;
    private int radius; // radius of circle
    private Bitmap centerIcon;
    private int centerIconMagin;

    private int mVolume = 3;

    public CircleVolume(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setAttrs(AttributeSet attrs) {
        try {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleVolume);
            unitCount = typedArray.getInteger(R.styleable.CircleVolume_unitCount, 10);
            unitSplit = typedArray.getInteger(R.styleable.CircleVolume_unitSplit, 5);
            if (unitSplit >= 360 / unitCount) {
                unitSplit = 360 / (unitCount * 5);
                Log.w(TAG, "unitSplit two large, reset it to 1/5 of unitSize");
            }
            unitSize = 360 / unitCount - unitSplit;
            normalColor = typedArray.getColor(R.styleable.CircleVolume_normalColor, Color.BLACK);
            focusColor = typedArray.getColor(R.styleable.CircleVolume_focusColor, Color.GREEN);
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleVolume_strokeWidth, SizeUtils.dp2px(mContext, 10));
            centerIcon = BitmapFactory.decodeResource(getResources(),
                    typedArray.getResourceId(R.styleable.CircleVolume_centerIcon, R.mipmap.ic_launcher_round));
            centerIconMagin = typedArray.getDimensionPixelSize(R.styleable.CircleVolume_centerIconMargin, SizeUtils.dp2px(mContext, 10));
            typedArray.recycle();
            Log.d(TAG, "setAttrs strokeWidth = " + strokeWidth);
        } catch (Exception ex) {
            Log.w(TAG, "setAttrs ex", ex);
        }
    }

    @Override
    protected void initView() {
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true); // 消除锯齿
        arcPaint.setStrokeWidth(strokeWidth);
        arcPaint.setStrokeCap(Paint.Cap.ROUND); // 设置圆头
        arcPaint.setAntiAlias(true); // 消除锯齿
        arcPaint.setStyle(Paint.Style.STROKE); // 设置空心
    }

    @Override
    protected void initSize() {
        radius = Math.min(halfWidth, halfHeight) - strokeWidth;// 获得外圆的半径
        arcRect = new RectF(-radius, -radius, radius, radius);
        iconRadius = radius - strokeWidth - centerIconMagin;// 获得内圆的半径
        iconRect = new RectF(-iconRadius, -iconRadius, iconRadius, iconRadius);
        //如果图片比较小，那么根据图片的尺寸放置到正中心
        if (Math.max(centerIcon.getWidth(), centerIcon.getHeight()) < iconRadius) {
            iconRect.left = -(int) (centerIcon.getWidth() * 1.0f / 2);
            iconRect.top = -(int) (centerIcon.getHeight() * 1.0f / 2);
            iconRect.right = (int) (centerIcon.getWidth() * 1.0f / 2);
            iconRect.bottom = (int) (centerIcon.getHeight() * 1.0f / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 0,0 move to (halfWidth, halfHeight)
        canvas.translate(halfWidth, halfHeight);
        // draw arcs
        drawArcs(canvas);
        // draw center image
        canvas.drawBitmap(centerIcon, null, iconRect, arcPaint);
    }

    private void drawArcs(Canvas canvas) {
        // draw normal arc
        arcPaint.setColor(normalColor);
        for (int i = mVolume; i < unitCount; i++) {
            canvas.drawArc(arcRect, i * (unitSize + unitSplit) - 90, unitSize, false, arcPaint);
        }
        // draw focus arc
        arcPaint.setColor(focusColor);
        for (int i = 0; i < mVolume; i++) {
            canvas.drawArc(arcRect, i * (unitSize + unitSplit) - 90, unitSize, false, arcPaint);
        }
    }

    /**
     * it must run in ui thread
     *
     * @param value
     */
    public void setVolume(int value) {
        try {
            if (mVolume == value) {
                Log.w(TAG, "setProgress not changed, since same value");
                return;
            }

            mVolume = value;
            if (mVolume >= unitCount) {
                mVolume = 0;
            }

            invalidate();
        } catch (Exception ex) {
            Log.w(TAG, "setVolume ex", ex);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (centerIcon != null && !centerIcon.isRecycled()) {
            centerIcon.recycle();
            centerIcon = null;
        }
    }
}
