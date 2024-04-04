package com.example.visuallyimpairedproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.visuallyimpairedproject.presenter.NewsPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.MyFragmentVPTabAdapter;
import com.example.visuallyimpairedproject.adapters.PageNewsRecyclerAdapter;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.NewsContract;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment implements View.OnClickListener, NewsContract.INewsView {

    private static final String TAG = "NewsFragment";
    private NewsContract.INewsPresenter mNewsPresenter;
    private TabLayout mNews_TabLayout;
    private ViewPager mNews_ViewPager;
    private CardView mNews_CardView;
    private TextView text_search; //搜索框中的文字
    private List<Fragment> mFragments;
    private List<String> mStrings;
    private MyFragmentVPTabAdapter mMyFragmentVPTabAdapter;
    private PageNewsRecyclerAdapter mPageNewsRecyclerAdapter;
    private TextView search_text;
    private BaseFragment mRecommendFragment;
    private BaseFragment mFinanceFragment;
    private BaseFragment mMedicalAndHealthFragment;
    private BaseFragment mEducationAndTrainingFragment;
    private BaseFragment mTechnologyFragment;
    private BaseFragment mEnergyAndMineralFragment;
    private BaseFragment mTrafficAndTransportFragment;
    private BaseFragment mPropertyAndBuildingFragment;
    private BaseFragment mPEFragment;
    private BaseFragment mMilitaryDefenseFragment;
    private BaseFragment mManufacturingIndustryFragment;
    private BaseFragment mEnvironmentFragment;
    private BaseFragment mTravelFragment;
    private BaseFragment mAccommodationCateringFragment;
    private BaseFragment mAFAHFFragment;
    private BaseFragment mCurrentPoliticsFragment;
    private BaseFragment mSocietyAndPeopleLivelihoodFragment;
    private BaseFragment mCulturalEntertainmentFragment;
    private BaseFragment mInformationIndustryFragment;
    private BaseFragment mTrafficSafetyFragment;
    private BaseFragment mSocialSecurityFragment;
    private BaseFragment mDisasterDangerFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter = new NewsPresenter(NewsFragment.this);
        mNewsPresenter.getHotTitle(); //调用P层方法得到搜索框的热门文字
    }

    //将xml文件转换为View对象
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    //该方法用于上面的onCreateView调用完后得到返回的View来实例化控件
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNews_CardView = view.findViewById(R.id.news_cardView); //"看新闻"Fragment中的cardView
        //得到TabLayout和ViewPager的实例
        mNews_TabLayout = view.findViewById(R.id.news_tabLayout);
        mNews_ViewPager = view.findViewById(R.id.news_viewPager);
        text_search = view.findViewById(R.id.text_search); //搜索框中的文字
        search_text = view.findViewById(R.id.search_text); //右上角的搜索文字
        mNews_ViewPager.setOffscreenPageLimit(21); //设置缓存所有页面
        //点击事件
        initOnClick();
        //调用NewsPresenter的方法初始化ViewPager
        initViewPager();
        //让TabLayout绑定ViewPager
        mNews_TabLayout.setupWithViewPager(mNews_ViewPager);
    }

    private void initOnClick() {
        mNews_CardView.setOnClickListener(this);
        search_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_cardView: //点击了"看新闻"页的cardView,跳转到SearchActivity
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.search_text: //点击了"搜索"文字
                Intent SearchResultActivityIntent = new Intent(getActivity(), SearchResultActivity.class);
                SearchResultActivityIntent.putExtra("search_text", text_search.getText().toString().substring(0,4));
                startActivity(SearchResultActivityIntent);
                break;
        }
    }

    //初始化ViewPager
    private void initViewPager() {
        mFragments = new ArrayList<>();
        mRecommendFragment = new BaseFragment("推荐");
        mFinanceFragment = new BaseFragment("金融");
        mMedicalAndHealthFragment = new BaseFragment("医疗卫生");
        mEducationAndTrainingFragment = new BaseFragment("教育培训");
        mTechnologyFragment = new BaseFragment("科技");
        mEnergyAndMineralFragment = new BaseFragment("能源矿产");
        mTrafficAndTransportFragment = new BaseFragment("交通运输");
        mPropertyAndBuildingFragment = new BaseFragment("房产建筑");
        mPEFragment = new BaseFragment("体育");
        mMilitaryDefenseFragment = new BaseFragment("军事国防");
        mManufacturingIndustryFragment = new BaseFragment("制造业");
        mEnvironmentFragment = new BaseFragment("生态坏境");
        mTravelFragment = new BaseFragment("旅游");
        mAccommodationCateringFragment = new BaseFragment("住宿餐饮");
        mAFAHFFragment = new BaseFragment("农林牧渔");
        mCurrentPoliticsFragment = new BaseFragment("时政");
        mSocietyAndPeopleLivelihoodFragment = new BaseFragment("社会民生");
        mCulturalEntertainmentFragment = new BaseFragment("文化娱乐");
        mInformationIndustryFragment = new BaseFragment("信息产业");
        mTrafficSafetyFragment = new BaseFragment("交通安全");
        mSocialSecurityFragment = new BaseFragment("社会治安");
        mDisasterDangerFragment = new BaseFragment("灾害险情");
        mFragments.add(mRecommendFragment);
        mFragments.add(mFinanceFragment);
        mFragments.add(mMedicalAndHealthFragment);
        mFragments.add(mEducationAndTrainingFragment);
        mFragments.add(mTechnologyFragment);
        mFragments.add(mEnergyAndMineralFragment);
        mFragments.add(mTrafficAndTransportFragment);
        mFragments.add(mPropertyAndBuildingFragment);
        mFragments.add(mPEFragment);
        mFragments.add(mMilitaryDefenseFragment);
        mFragments.add(mManufacturingIndustryFragment);
        mFragments.add(mEnvironmentFragment);
        mFragments.add(mTravelFragment);
        mFragments.add(mAccommodationCateringFragment);
        mFragments.add(mAFAHFFragment);
        mFragments.add(mCurrentPoliticsFragment);
        mFragments.add(mSocietyAndPeopleLivelihoodFragment);
        mFragments.add(mCulturalEntertainmentFragment);
        mFragments.add(mInformationIndustryFragment);
        mFragments.add(mTrafficSafetyFragment);
        mFragments.add(mSocialSecurityFragment);
        mFragments.add(mDisasterDangerFragment);
        mStrings = new ArrayList<>();
        mStrings.add("推荐");
        mStrings.add("体育");
        mStrings.add("医疗卫生");
        mStrings.add("教育培训");
        mStrings.add("科技");
        mStrings.add("能源矿产");
        mStrings.add("交通运输");
        mStrings.add("房产建筑");
        mStrings.add("体育");
        mStrings.add("军事国防");
        mStrings.add("制造业");
        mStrings.add("生态坏境");
        mStrings.add("旅游");
        mStrings.add("住宿餐饮");
        mStrings.add("农林牧渔");
        mStrings.add("时政");
        mStrings.add("社会民生");
        mStrings.add("文化娱乐");
        mStrings.add("信息产业");
        mStrings.add("交通安全");
        mStrings.add("社会治安");
        mStrings.add("灾害险情");
        mMyFragmentVPTabAdapter = new MyFragmentVPTabAdapter(getChildFragmentManager(), mFragments, mStrings); //创建"看新闻"页独有的适配器
        //设置适配器
        mNews_ViewPager.setAdapter(mMyFragmentVPTabAdapter);

    }

    //恢复的时候请求热门标题放在搜索框里
    @Override
    public void onResume() {

        super.onResume();
    }

    //得到首页搜索框热门搜索文字成功
    @Override
    public void getHotTitleSuccess(News news) {
        Log.d(TAG, "热门搜索文字为-->" + news.getTitle());
        Log.d(TAG, "NewsFragment处的getHotTitleSuccess方法调用，获取热门搜索文字成功");
        text_search.setText(news.getTitle());

    }

    //得到首页搜索框热门搜索文字失败
    @Override
    public void getHotTitleFailed(Throwable throwable) {
        Log.d(TAG, "NewsFragment处的getHotTitleFailed方法调用-->" + throwable);
    }
}