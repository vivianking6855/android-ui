package com.ui.advanced.advanceddemo.coordinatorlayout.start;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * http://www.jianshu.com/p/72d45d1f7d55
 */
public class DependencyView extends TextView {
    private final String TAG = DependencyView.class.getSimpleName();
    int x, y, lastX, lastY;

    public DependencyView(Context context) {
        this(context, null);
    }

    public DependencyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependencyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // set last, if not it will jump when start
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                // get LayoutParams of margin or use CoordinatorLayout.MarginLayoutParams
                if (getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) getLayoutParams();
                    params.leftMargin = params.leftMargin + x - lastX;
                    params.topMargin = params.topMargin + y - lastY;
                    setLayoutParams(params);
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        // consume event
        return true;
    }
}
