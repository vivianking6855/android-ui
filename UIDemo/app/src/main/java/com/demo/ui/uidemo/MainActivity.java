package com.demo.ui.uidemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements CusomizedSurfaceViewFragment.OnFragmentInteractionListener,
        CustomizedViewFragment.OnFragmentInteractionListener, MenuFragment.OnFragmentInteractionListener, NotifyFragment.OnFragmentInteractionListener {
    private BottomNavigationView bottomNavigationView;

    private Context mContext;

    // demo fragments
    private CustomizedViewFragment mCusomizedFragment;
    private CusomizedSurfaceViewFragment mCusomizedSurfaceFragment;
    private MenuFragment mMenuFragment;
    private NotifyFragment mNotifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        initViews();
    }

    private void initViews() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        changeFragment(item.getItemId());
                        return true;
                    }
                });

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mCusomizedFragment = new CustomizedViewFragment();
        transaction.replace(R.id.id_content, mCusomizedFragment);
        transaction.commit();
    }

    private void changeFragment(int itemId) {
        FragmentManager fm = getSupportFragmentManager();
        //  start Fragment transaction
        FragmentTransaction transaction = fm.beginTransaction();

        switch (itemId) {
            case R.id.customize_surface:
                if (mCusomizedSurfaceFragment == null) {
                    mCusomizedSurfaceFragment = new CusomizedSurfaceViewFragment();
                }
                transaction.replace(R.id.id_content, mCusomizedSurfaceFragment);
                break;
            case R.id.menudemo:
                if (mMenuFragment == null) {
                    mMenuFragment = new MenuFragment();
                }
                transaction.replace(R.id.id_content, mMenuFragment);
                break;
            case R.id.notify:
                if (mNotifyFragment == null) {
                    mNotifyFragment = new NotifyFragment();
                }
                transaction.replace(R.id.id_content, mNotifyFragment);
                break;
            default:
                if (mCusomizedFragment == null) {
                    mCusomizedFragment = new CustomizedViewFragment();
                }
                transaction.replace(R.id.id_content, mCusomizedFragment);
                break;
        }

        //  commit
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
