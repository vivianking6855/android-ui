package com.ui.advanced.advanceddemo.coordinatorlayout.start;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ui.advanced.advanceddemo.R;

/**
 * @description start learn demo of CoordinatorLayout
 * CoordinatorLayout的使用如此简单: http://www.jianshu.com/p/72d45d1f7d55
 * logic:
 * 1. child view set Behavior
 * 2. child view will move/change position when dependency view changed
 * 3. child view change logic and core thinking in Behavior
 * 4. Behavior check if child depends on dependency with instanceof DependencyView condition;
 */
public class HomeActivity extends AppCompatActivity {
    private final static String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_start);
    }
}
