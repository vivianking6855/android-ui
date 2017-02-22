package com.ui.advanced.advanceddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ui.advanced.advanceddemo.appbarlayout.AppBarCollapsingActivity;
import com.ui.advanced.advanceddemo.appbarlayout.AppBarLayoutActivity;
import com.ui.advanced.advanceddemo.coordinatorlayout.follow.FollowActivity;
import com.ui.advanced.advanceddemo.coordinatorlayout.scroll.ScrollActivity;
import com.ui.advanced.advanceddemo.coordinatorlayout.start.HomeActivity;


public class MainActivity extends AppCompatActivity implements HomeAdapter.OnItemClickLitener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

    // all layouts for each item in home list
    private final static int[] LAYOUTS = new int[]{
            // constraint layouts
            R.layout.constraint_around,
            R.layout.constraint_autoconnect,
            R.layout.constraint_inference,
            // recycler layouts
            R.layout.activity_recycler,
            R.layout.activity_recycler,
            R.layout.activity_recycler,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init recycler view
        recyclerView = (RecyclerView) findViewById(R.id.homelist);
        homeAdapter = new HomeAdapter(this, Const.TYPE_NORML);
        homeAdapter.setOnItemClickLitener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(homeAdapter);
    }

    @Override
    public void onItemClick(View view, int position, String[] info) {
        // info must has {id, name}
        if (info == null || info.length != 2) {
            return;
        }

        // get info
        String id = info[0];
        String title = info[1];

        // get index
        int index = Integer.parseInt(id);
        switch (index) {
            case 6: // CoordinatorLayout start
                startActivity(HomeActivity.class);
                break;
            case 7: // CoordinatorLayout follow
                startActivity(FollowActivity.class);
                break;
            case 8: // CoordinatorLayout scroll
                startActivity(ScrollActivity.class);
                break;
            case 9: // AppBarLayout
                startActivity(AppBarLayoutActivity.class);
                break;
            case 10: // CollapsingActivity
                startActivity(AppBarCollapsingActivity.class);
                break;
            default:
                startTemplateActivity(index, title);
                break;
        }
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, cls);
        startActivity(intent);
    }

    private void startTemplateActivity(int index, String title) {
        if (index < 0 || index >= LAYOUTS.length) {
            index = 0;
        }

        int layout = LAYOUTS[index];
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.BUNDLE_LAYOUT, layout);
        bundle.putInt(Const.BUNDLE_ID, index);
        bundle.putString(Const.BUNDLE_TITLE, title);
        intent.setClass(MainActivity.this, TemplateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
