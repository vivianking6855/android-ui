package com.ui.advanced.advanceddemo.coordinatorlayout.scroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ui.advanced.advanceddemo.R;

/**
 * @description two item, one follow another
 * 关于CoordinatorLayout与Behavior的一点分析: http://www.jianshu.com/p/a506ee4afecb
 * Behavior's most power is for scroll, just like CollapsingToolbarLayout
 * logic:
 * 1. child view set Behavior
 * 2. CoordinatorLayout will scoll if it contains NestedScrollingChild.
 * 3. if it scroll, it will invoke Behavior's scroll callback(onStartNestedScroll,onNestedScroll,onNestedFling)
 * 4. child view scroll logic will deal with in onNestedScroll
 */
public class ScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_scroll);
    }
}
