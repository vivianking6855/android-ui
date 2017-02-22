package com.ui.advanced.advanceddemo.coordinatorlayout.follow;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @description dependency view
 * it move vertically with user finger
 */

public class DependencyView extends View {

    private int y, lastY;

    public DependencyView(Context context) {
        this(context, null);
    }

    public DependencyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependencyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                y = (int) event.getRawY();
                // get margin of LayoutParams or use CoordinatorLayout.MarginLayoutParams
                if (getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
                    params.topMargin = params.topMargin + y - lastY;
                    setLayoutParams(params);
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return true;
    }
}
