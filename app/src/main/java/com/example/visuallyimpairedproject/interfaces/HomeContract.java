package com.example.visuallyimpairedproject.interfaces;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

public interface HomeContract {
    interface IHomeView {
        void onBottomNavigationChange(int position);
    }
    interface IHomePresenter {
        void initViewPager2(ViewPager2 viewPager2, FragmentManager fragmentManager, Lifecycle lifecycle);
    }
    interface IHomeModel {
        void initViewPager2(ViewPager2 viewPager2, FragmentManager fragmentManager, Lifecycle lifecycle, IHomeCallBack homeCallBack);
    }
    interface IHomeCallBack {
        void onInitViewPager2Success(int position);
    }
}
