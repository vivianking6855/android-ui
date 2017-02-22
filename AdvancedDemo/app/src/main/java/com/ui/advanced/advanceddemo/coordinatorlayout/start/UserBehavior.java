package com.ui.advanced.advanceddemo.coordinatorlayout.start;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vivian on 2017/1/5.
 */

public class UserBehavior extends CoordinatorLayout.Behavior<TextView> {
    private int screenWidth;

    public UserBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // get screen resolution
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
    }

    /**
     * check if child layout depend on dependency view
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        // child is the view which set this Behavior
        // if dependency is instance of CoordinatorStartView, means it is the dependency that we need
        return dependency instanceof DependencyView;
    }

    /**
     * when dependency changed (position, width, height), call this method
     * if true, means child's position, width, height will change
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        // set child(TextView) according to dependency
        int left = dependency.getLeft();
        int top = dependency.getTop();

        int x = screenWidth - left - child.getWidth();
        int y = top;

        setPosition(child, x, y);
        return true;
    }

    /**
     * @param child child view that will change position
     * @param x     leftMargin
     * @param y     topMargin
     * @ description set position of child
     */
    private void setPosition(View child, int x, int y) {
        if (child.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            params.leftMargin = x;
            params.topMargin = y;
            child.setLayoutParams(params);
        }
    }
}
