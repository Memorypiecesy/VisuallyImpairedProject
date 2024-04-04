package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.MyFragmentVPTabAdapter;
import com.example.visuallyimpairedproject.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCollectionActivity extends AppCompatActivity {

    @BindView(R.id.my_collection_viewPager) ViewPager my_collection_viewPager;//ViewPager
    @BindView(R.id.my_collection_tabLayout) TabLayout my_collection_tabLayout;//TabLayout
    private List<Fragment> mFragments;
    private List<String> mStrings;
    private MyFragmentVPTabAdapter mMyFragmentVPTabAdapter;
    private NewsOfIRFragment mNewsOfIRFragment;
    private NewsOfMCFragment mNewsOfMCFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
        initViewPager();
        initView();
    }

    //初始化ViewPager
    private void initViewPager() {
        mFragments = new ArrayList<>();
        mNewsOfMCFragment = new NewsOfMCFragment();
        mNewsOfIRFragment = new NewsOfIRFragment();
        mFragments.add(mNewsOfMCFragment);
        mFragments.add(mNewsOfIRFragment);
        mStrings = new ArrayList<>();
        mStrings.add("新闻");
        mStrings.add("识别内容");
        mMyFragmentVPTabAdapter = new MyFragmentVPTabAdapter(getSupportFragmentManager(), mFragments, mStrings);
    }

    private void initView() {
        my_collection_viewPager.setAdapter(mMyFragmentVPTabAdapter); //给ViewPager设置适配器
        my_collection_tabLayout.setupWithViewPager(my_collection_viewPager); //把ViewPager和TabLayout绑定在一起
    }
}