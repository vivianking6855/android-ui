package com.ui.advanced.advanceddemo.appbarlayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ui.advanced.advanceddemo.R;

import java.util.ArrayList;
import java.util.List;

public class UserFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<String> mList = new ArrayList<>();
    private Context mContext;

    public UserFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public UserFragmentPagerAdapter(Context context, FragmentManager fm, List<String> list) {
        super(fm);
        mList = list;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemFragment.newInstance( position + "\r\n" + mContext.getString(R.string.text_hint));
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * 重写与TabLayout配合
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position);
    }
}
