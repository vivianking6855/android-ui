package com.ui.advanced.advanceddemo.appbarlayout;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ui.advanced.advanceddemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 玩转AppBarLayout，更酷炫的顶部栏: http://www.jianshu.com/p/d159f0176576
 * reference github code : https://github.com/ffuujian/CoordinatorLayoutDemo
 * demo for AppBarLayout,TabLayout,Toolbar (AppBarLayout must co-work with CoordinatorLayout, otherwise scroll action will not work)
 * logic:
 * 1. AppBarLayout include Toolbar and TabLayout
 * 2. TabLayout will call setupWithViewPager to add content ViewPager in it.
 * 3. TabLayout did not layout_scrollFlags to scroll, so it will not scroll out, but in the top of screen.
 */
public class AppBarLayoutActivity extends AppCompatActivity implements ItemFragment.OnFragmentInteractionListener {

    private TabLayout tab;
    private ViewPager viewpager;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar_layout);

        initView();
        initData();
    }

    private void initView() {
        initToolbar();
        initTabLayout();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 设置toolbar，注意theme不能使用带有actionbar的样式，否则会报错
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 4.0以上Navigation默认false
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_1:
                    case R.id.menu_item_2:
                    case R.id.menu_item_3:
                        Toast.makeText(AppBarLayoutActivity.this, "ToolBar " + item.getItemId(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void initTabLayout() {
        tab = (TabLayout) findViewById(R.id.tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void initData() {
        if (mList.size() > 0) {
            mList.clear();
        }

        for (int i = 1; i < 5; i++) {
            mList.add("Tab " + i);
        }

        // 设置ViewPager的Adapter
        viewpager.setAdapter(new UserFragmentPagerAdapter(this, getSupportFragmentManager(), mList));
        // 关键一行代码，将TabLayout与ViewPager关联
        tab.setupWithViewPager(viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    // 注意：如果toolbar.setOnMenuItemClickListener了，那么这里就收不到MenuItem的点击事件了
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_1:
            case R.id.menu_item_2:
            case R.id.menu_item_3:
                Toast.makeText(this, "onOptionsItemSelected " + item.getItemId(), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
    }
}
