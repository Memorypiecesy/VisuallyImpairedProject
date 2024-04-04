package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.visuallyimpairedproject.presenter.HomePresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.interfaces.HomeContract;
import com.example.visuallyimpairedproject.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, HomeContract.IHomeView {
    private HomeContract.IHomePresenter mMainPresenter;
    private ImageView mImage_Temp;
    private TextView mText_Temp;
    @BindView(R.id.photograph_relative) RelativeLayout photograph_relative; //拍照识图的RelativeLayout
    @BindView(R.id.home_viewPager2) ViewPager2 home_viewPager2; //ViewPager2
    @BindView(R.id.image_see_news) ImageView image_see_news; //"看新闻"的图标
    @BindView(R.id.image_user) ImageView image_user; //"我的"图标
    @BindView(R.id.text_see_news) TextView text_see_news; //"看新闻"文字
    @BindView(R.id.text_user) TextView text_user; //"我的"文字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }
    //初始化控件等
    private void initView() {
        mMainPresenter = new HomePresenter(HomeActivity.this); //持有HomePresenter
        mMainPresenter.initViewPager2(home_viewPager2,getSupportFragmentManager(),getLifecycle()); //调用持有的HomePresenter层的方法初始化ViewPager
        initOnClick();
        //一开始设置"看新闻"图标被选中、文字为蓝色
        image_see_news.setSelected(true);
        text_see_news.setTextColor(getResources().getColor(R.color.blue));
        //将看新闻图标和看新闻文字赋值给临时保存的成员变量
        mImage_Temp = image_see_news;
        mText_Temp = text_see_news;
    }

    //初始化事件点击事件
    private void initOnClick() {
        photograph_relative.setOnClickListener(this);
        image_see_news.setOnClickListener(this);
        image_user.setOnClickListener(this);
    }

    //事件点击事件的具体实现
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_see_news: //点击了"看新闻"icon
                onBottomNavigationChange(R.id.image_see_news);
                break;
            case R.id.image_user: //点击了"我的"icon
                onBottomNavigationChange(R.id.image_user);
                break;
            case R.id.photograph_relative: //点击了"拍照识图"RelativeLayout，跳转拍照识图Activity
                Intent CameraIntent = new Intent(HomeActivity.this,CameraActivity.class);
                startActivity(CameraIntent);
                break;
        }
    }
    //改变底部导航栏
    @Override
    public void onBottomNavigationChange(int position) {
        mImage_Temp.setSelected(false);
        mText_Temp.setTextColor(getResources().getColor(R.color.common_text_black));
        switch (position) {
            case R.id.image_see_news:
                //点击底下图标跳转到对应的Fragment，第二个参数true表示滑动速度慢，false表示快速滑动
                home_viewPager2.setCurrentItem(0,false);
            case 0:
                image_see_news.setSelected(true);
                text_see_news.setTextColor(getResources().getColor(R.color.blue));
                mImage_Temp = image_see_news;
                mText_Temp = text_see_news;
                break;
            case R.id.image_user:
                home_viewPager2.setCurrentItem(1,false);
            case 1:
                image_user.setSelected(true);
                text_user.setTextColor(getResources().getColor(R.color.blue));
                mImage_Temp = image_user;
                mText_Temp = text_user;
                break;
        }
    }
}