package com.open.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vivian on 2017/6/19.
 */

public class CanvasBasicUsage extends View {
    private final static String TAG = CanvasBasicUsage.class.getSimpleName();

    private Context mContext;

    // view paints
    private Paint mTextPaint;
    private Paint mCirPaint;
    private Paint mLinePaint;
    private Paint mArcPaint;
    private RectF mArcRect = new RectF(150, 300, 300, 450);
    private Paint mShapePaint;
    private Path mShapePath;
    private RectF mRoundRect = new RectF(350, 300, 450, 400);
    private Paint mPointPaint;
    private Bitmap drawBitmap;
    private Paint mBezierPaint;
    private Path mBezierPath;

    public CanvasBasicUsage(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        initPaints();
    }

    private void initPaints() {
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.light_green));
            mTextPaint.setTextSize(50);
        }
        if (mCirPaint == null) {
            mCirPaint = new Paint();
            final int[] arcColor = new int[]{ContextCompat.getColor(mContext, R.color.firstColor),
                    ContextCompat.getColor(mContext, R.color.secondColor)};
            LinearGradient gradient = new LinearGradient(150, 300, 300, 450, arcColor, null, LinearGradient.TileMode.CLAMP);
            mCirPaint.setAntiAlias(true); // 消除锯齿
            mCirPaint.setShader(gradient);
        }
        if (mLinePaint == null) {
            mLinePaint = new Paint();
            mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.deep_green));
            mLinePaint.setStyle(Paint.Style.STROKE); // 设置空心
            mLinePaint.setStrokeWidth(5);
        }
        if (mArcPaint == null) {
            mArcPaint = new Paint();
            mArcPaint.setStyle(Paint.Style.STROKE); // 设置空心
            mArcPaint.setStrokeWidth(5);
            mArcPaint.setAntiAlias(true); // 消除锯齿
            final int[] arcColor = new int[]{ContextCompat.getColor(mContext, R.color.light_green),
                    ContextCompat.getColor(mContext, R.color.deep_green)};
            LinearGradient gradient = new LinearGradient(150, 300, 300, 450, arcColor, null, LinearGradient.TileMode.CLAMP);
            mArcPaint.setShader(gradient);
        }
        if (mShapePaint == null) {
            mShapePaint = new Paint();
            mShapePaint.setStyle(Paint.Style.STROKE); // 设置空心
            final int[] shapeColor = new int[]{ContextCompat.getColor(mContext, R.color.firstColor),
                    ContextCompat.getColor(mContext, R.color.deep_green)};
            LinearGradient gradient = new LinearGradient(150, 300, 300, 450, shapeColor, null, LinearGradient.TileMode.CLAMP);
            mShapePaint.setShader(gradient);
            mShapePaint.setStrokeWidth(5);
            // set path
            mShapePath = new Path();
            mShapePath.moveTo(500, 200);
            mShapePath.lineTo(600, 200);
            mShapePath.lineTo(650, 250);
            mShapePath.lineTo(650, 230);
            mShapePath.lineTo(500, 200);
            mShapePath.close();
        }
        if (mPointPaint == null) {
            mPointPaint = new Paint();
            mPointPaint.setStyle(Paint.Style.FILL);
            mPointPaint.setStrokeWidth(10);
            mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.deep_green));
        }
        if (drawBitmap == null) {
            drawBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        }
        if(mBezierPath == null){
            mBezierPaint = new Paint();
            mBezierPaint.setStyle(Paint.Style.STROKE);
            mBezierPaint.setStrokeWidth(5);
            mBezierPaint.setColor(ContextCompat.getColor(mContext, R.color.deep_green));
            mBezierPath=new Path();
            mBezierPath.moveTo(200, 580);// path start point
            //mBezierPath.quadTo(250, 420, 400, 500); // 二阶贝塞尔曲线
            mBezierPath.cubicTo(250, 420, 400, 650, 500, 500); // 三阶贝塞尔曲线，设置控制点和结束点（最后一个是结束点）
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw circle
        canvas.drawText("Circle", 10, 80, mTextPaint);
        canvas.drawText("Circle", 10, 80, mTextPaint);
        canvas.drawCircle(220, 70, 40, mTextPaint);
        canvas.drawCircle(400, 80, 80, mCirPaint);
        // draw points
        canvas.drawPoints(new float[]{500, 80, 550, 80, 600, 80}, mPointPaint);

        // draw line
        canvas.drawText("Line", 10, 220, mTextPaint);
        canvas.drawLine(150, 200, 250, 200, mLinePaint);
        canvas.drawLine(350, 200, 450, 200, mLinePaint);
        // multi-line shape
        canvas.drawPath(mShapePath, mShapePaint);

        // draw arc
        canvas.drawText("Arc", 10, 350, mTextPaint);
        canvas.drawArc(mArcRect, 180, 180, false, mArcPaint);
        // draw round rect
        canvas.drawRoundRect(mRoundRect, 20, 15, mCirPaint);
        // draw bitmap
        canvas.drawBitmap(drawBitmap, 500, 300, mPointPaint);

        // bezier path
        canvas.drawText("Bezier", 10, 500, mTextPaint);
        canvas.drawPath(mBezierPath, mBezierPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (drawBitmap != null && !drawBitmap.isRecycled()) {
            drawBitmap.recycle();
            drawBitmap = null;
        }
    }
}
