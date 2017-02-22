package com.demo.ui.customized;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.demo.ui.uidemo.R;

/**
 * Created by Venjee_Shen on 2016/11/4.
 */

public class ModeButtonView extends View {
    private final String TAG = "ModeButtonView";
    public static final int SMALL_BUTTON = 1;
    public static final int BIG_BUTTON = 2;

    public static final int MODE_UNKNOWN = -1;

    private int edgeColor, textColor, bgColor;

    private float centerX;
    private float circleRadius = 0;
    private float arc1Stroke;
    private float arc2Stroke;
    private Paint mPaint = null;
    private RectF mRectF1 = null;
    private RectF mRectF2 = null;
    private float mTextSize;
    private int paddingTop;
    private int paddingCircleText;
    private float mScale = 1;

    private int mMode = MODE_UNKNOWN;
    private int mType = SMALL_BUTTON;
    private boolean isSelected = false;
    private int mImageRes = -1;

    public ModeButtonView(Context context) {
        this(context, null);
    }

    public ModeButtonView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ModeButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.type);
        mType = typedArray.getInt(R.styleable.type_buttonType, SMALL_BUTTON);
        typedArray.recycle();
        initDrawParameter(context);
    }

    private void initDrawParameter(Context context) {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        if (mRectF1 == null) {
            mRectF1 = new RectF(0, 0, 0, 0);
        }
        if (mRectF2 == null) {
            mRectF2 = new RectF(0, 0, 0, 0);
        }
    }

    public void setParameter(int mode) {
        this.mMode = mode;
    }

    public void setButtonType(int type) {
        mType = type;
    }

    public void setButtonScale(float scale) {
        mScale = scale;
        invalidate();
    }

    public void setSelected(boolean isselected) {
        this.isSelected = isselected;
        if (mScale < 1) {
            mImageRes = getImageRes(mMode, true, isEnabled());
        } else {
            mImageRes = getImageRes(mMode, false, isEnabled());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setDrawParameter();
        drawBackground(canvas);
    }

    private void setDrawParameter() {
        if (mType == BIG_BUTTON)
            setBigButtonParameter();
        else
            setSmallButtonParameter();
        centerX = this.getMeasuredWidth() / 2.0f;
        arc1Stroke = 1;
        arc2Stroke = 4;
        float arc1Radius = circleRadius - arc1Stroke / 2;

        if (isEnabled()) {
            if (isSelected) {
                bgColor = getResources().getColor(R.color.mode_selected_bg);
                edgeColor = getResources().getColor(R.color.mode_selected);
                textColor = getResources().getColor(R.color.mode_selected);
            } else {
                bgColor = getResources().getColor(R.color.mode_not_selected_bg);
                edgeColor = getResources().getColor(R.color.mode_not_selected);
                textColor = getResources().getColor(R.color.mode_not_selected);
            }
        } else {
            bgColor = Color.TRANSPARENT;
            edgeColor = getResources().getColor(R.color.disable);
            textColor = getResources().getColor(R.color.disable);
        }

        if (mRectF1 != null && mRectF2 != null) {
            mRectF1.set(centerX - arc1Radius, paddingTop + arc1Stroke / 2, centerX + arc1Radius, paddingTop + arc1Stroke / 2 + arc1Radius * 2);
            mRectF2.set(mRectF1.left + (arc2Stroke - arc1Stroke), mRectF1.top + (arc2Stroke - arc1Stroke), mRectF1.right - (arc2Stroke - arc1Stroke), mRectF1.bottom - (arc2Stroke - arc1Stroke));
        }
    }

    private void setBigButtonParameter() {
        circleRadius = getResources().getDimension(
                R.dimen.mode_big_btn_circleRadius);
        mTextSize = getResources().getDimension(R.dimen.mode_small_btn_text);
        paddingTop = (int) getResources().getDimension(
                R.dimen.mode_big_btn_paddingTop);
        paddingCircleText = (int) getResources().getDimension(
                R.dimen.mode_btn_paddingCircleText);
    }

    private void setSmallButtonParameter() {
        circleRadius = getResources().getDimension(
                R.dimen.mode_small_btn_circleRadius) * mScale;
        mTextSize = getResources().getDimension(R.dimen.mode_small_btn_text);
        paddingTop = (int) getResources().getDimension(
                R.dimen.mode_small_btn_paddingTop);
        paddingCircleText = (int) getResources().getDimension(
                R.dimen.mode_btn_paddingCircleText);
    }

    private void drawBackground(Canvas canvas) {
        if (mPaint == null)
            mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(bgColor);
        canvas.drawArc(mRectF1, 0, 360, false, mPaint);

        if (mImageRes != -1) {
            Bitmap bm = BitmapFactory.decodeResource(this.getResources(), mImageRes);
            canvas.drawBitmap(bm, mRectF2.left + mRectF2.width() / 2 - bm.getWidth() / 2, mRectF2.top + mRectF2.height() / 2 - bm.getHeight() / 2, null);
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(edgeColor);
        mPaint.setStrokeWidth(arc2Stroke);
        canvas.drawArc(mRectF1, 0, 360, false, mPaint);
    }

    public int getImageRes(int mode, boolean smallSize, boolean enable) {
        if (smallSize) {
            if (enable)
                return R.drawable.asus_audio_wizard_btn_movie_small_p;
            else
                return R.drawable.asus_audio_wizard_btn_movie_small_d;
        } else if (enable) {
            return R.drawable.asus_audio_wizard_btn_movie_p;
        } else {
            return R.drawable.asus_audio_wizard_btn_movie_n;
        }
    }

}

