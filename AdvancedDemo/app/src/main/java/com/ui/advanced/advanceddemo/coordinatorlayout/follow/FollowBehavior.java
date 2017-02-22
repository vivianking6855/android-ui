package com.ui.advanced.advanceddemo.coordinatorlayout.follow;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.ui.advanced.advanceddemo.R;

/**
 * Created by vivian on 2017/1/10.
 */

public class FollowBehavior extends CoordinatorLayout.Behavior{
    private int targetId;

    public FollowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        // get targetId
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Follow);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (a.getIndex(i) == R.styleable.Follow_target) {
                targetId = a.getResourceId(attr, -1);
            }
        }
        a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // child is thie view which set FollowBehavior
        return dependency.getId() == targetId;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setY(dependency.getY() + dependency.getHeight());
        return true;
    }
}
