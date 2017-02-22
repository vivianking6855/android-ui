package com.ui.advanced.advanceddemo.coordinatorlayout.follow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ui.advanced.advanceddemo.R;

/**
 * @description two item, one follow another
 * 关于CoordinatorLayout与Behavior的一点分析: http://www.jianshu.com/p/a506ee4afecb
 * logic:
 * 1. child view set Behavior
 * 2. child view will move/change position when dependency view changed
 * 3. child view change logic and core thinking in Behavior
 * 4. Behavior check if child depends on dependency with target id (customized property)
 */
public class FollowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_follow);
    }
}
