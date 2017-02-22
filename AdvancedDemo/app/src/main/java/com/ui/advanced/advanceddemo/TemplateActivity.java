package com.ui.advanced.advanceddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class TemplateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get layout id
        Bundle bundle = getIntent().getExtras();
        int layout = bundle.getInt(Const.BUNDLE_LAYOUT);
        int id = bundle.getInt(Const.BUNDLE_ID);
        String title = bundle.getString(Const.BUNDLE_TITLE);
        setContentView(layout);
        getSupportActionBar().setTitle(title);

        initData(id);
    }

    private void initData(int id) {
        switch (id) {
            case 0: // constraint layout start
            case 1: // constraint layout auto connect
            case 2: // constraint layout inference
                break;
            case 3: // recycler grid
            case 4: // recycler stagger horizon
            case 5: // recycler stagger vertical
                initRecyclerView(id);
                break;
            default:
                break;
        }
    }

    private void initRecyclerView(int type) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.homelist);
        switch (type) {
            case 3: // recycler grid
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                recyclerView.setAdapter(new HomeAdapter(this, Const.TYPE_GRID));
                break;
            case 4: // recycler stagger horizon
                recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
                recyclerView.setAdapter(new HomeAdapter(this, Const.TYPE_HORIZON));
                break;
            case 5: // recycler stagger vertical
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.setAdapter(new HomeAdapter(this, Const.TYPE_VERTICAL));
                break;
            default:
                break;
        }

    }


}
