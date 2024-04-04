package com.example.visuallyimpairedproject.presenter;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.visuallyimpairedproject.interfaces.HomeContract;
import com.example.visuallyimpairedproject.model.HomeModel;

public class HomePresenter implements HomeContract.IHomePresenter, HomeContract.IHomeCallBack {

    private HomeContract.IHomeModel mMainModel;
    private HomeContract.IHomeView mMainView;

    public HomePresenter(HomeContract.IHomeView iHomeView){
        mMainView = iHomeView;   //持有HomeView
        mMainModel = new HomeModel(); //持有HomeModel
    }

    @Override
    public void initViewPager2(ViewPager2 viewPager2, FragmentManager fragmentManager, Lifecycle lifecycle) {
        mMainModel.initViewPager2(viewPager2,fragmentManager,lifecycle,this);
    }

    @Override
    public void onInitViewPager2Success(int position) {
        mMainView.onBottomNavigationChange(position);
    }
}
