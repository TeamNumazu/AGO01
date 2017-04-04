package com.example.silve.ago01.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.silve.ago01.fragments.PageFragment;

import java.util.ArrayList;

/**
 * タブ切り替えのビューのフラグメント表示のアダプターやつ
 */
public class AgostickPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mCategoryNameList;
    private ArrayList<Long> mCategoryIdList;

    public AgostickPagerAdapter(FragmentManager fm, ArrayList<String> categoryNameList, ArrayList<Long> categoryIdList) {
        super(fm);

        this.mCategoryNameList = categoryNameList;
        this.mCategoryIdList = categoryIdList;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1, this.mCategoryIdList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mCategoryNameList.get(position);
    }

    @Override
    public int getCount() {
        return this.mCategoryNameList.size();
    }
}
