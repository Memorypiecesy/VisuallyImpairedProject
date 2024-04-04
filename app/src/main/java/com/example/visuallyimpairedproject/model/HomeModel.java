package com.example.visuallyimpairedproject.model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.visuallyimpairedproject.adapters.MyFragmentPagerAdapter;
import com.example.visuallyimpairedproject.interfaces.HomeContract;
import com.example.visuallyimpairedproject.view.MyFragment;
import com.example.visuallyimpairedproject.view.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeModel implements HomeContract.IHomeModel {

    private List<Fragment> mFragments = new ArrayList<>();
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;

    //初始化HomeActivity中的ViewPager2
    @Override
    public void initViewPager2(ViewPager2 viewPager2, FragmentManager fragmentManager, Lifecycle lifecycle, HomeContract.IHomeCallBack homeCallBack) {
        viewPager2.setUserInputEnabled(false); //禁止用户滑动
        mFragments.add(new NewsFragment()); //往Fragments集合中添加Fragment
        mFragments.add(new MyFragment());
        //这里是在Activity中创建适配器，第一个参数传getSupportFragmentManager，表示Activity管理这些Fragments
        mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager,lifecycle,mFragments); //创建适配器
        viewPager2.setAdapter(mMyFragmentPagerAdapter); //给ViewPager设置适配器
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { //给ViewPager设置页面切换监听事件
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //调用回调接口的方法来利用MainPresenter对MainActivity中的导航栏进行更新
                homeCallBack.onInitViewPager2Success(position);
            }
        });
    }
}
