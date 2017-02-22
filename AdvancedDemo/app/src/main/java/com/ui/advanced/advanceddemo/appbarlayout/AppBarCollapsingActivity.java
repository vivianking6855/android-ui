package com.ui.advanced.advanceddemo.appbarlayout;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ui.advanced.advanceddemo.R;

/**
 * @description 玩转AppBarLayout，更酷炫的顶部栏: http://www.jianshu.com/p/d159f0176576
 * reference github code : https://github.com/ffuujian/CoordinatorLayoutDemo
 * demo for co-work of CoordinatorLayout,AppBarLayout,CollapsingToolbarLayout,NestedScrollView
 * logic:
 * 1. AppBarLayout include CollapsingToolbarLayout
 * 2. NestedScrollView set layout_behavior="@string/appbar_scrolling_view_behavior, when scoll will call AppBarLayout behavior
 */
public class AppBarCollapsingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar_collapsing_layout);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 4.0以上Navigation默认false
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Title默认true
            actionBar.setDisplayShowTitleEnabled(true);
            // Logo默认true
            actionBar.setDisplayUseLogoEnabled(true);
        }
    }
}
