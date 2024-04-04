package com.example.visuallyimpairedproject.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentVPTabAdapter extends FragmentStatePagerAdapter {
    /**
     * 这是看新闻页的Adapter
     */
    private List<Fragment> mFragments;
    private List<String> mStrings; //TabLayout的标题列表

    public MyFragmentVPTabAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList,List<String> stringList) {
        super(fm);
        mFragments = fragmentList;
        mStrings = stringList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments==null?null:mFragments.get(position); //判空
    }

    @Override
    public int getCount() {
        return mFragments==null?0:mFragments.size(); //判空
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mStrings==null?"":mStrings.get(position); //判空
    }
}
