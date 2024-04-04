package com.example.visuallyimpairedproject.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.visuallyimpairedproject.presenter.BasePresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.PageNewsRecyclerAdapter;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.BaseContract;
import com.example.visuallyimpairedproject.interfaces.LoadMore;
import com.example.visuallyimpairedproject.interfaces.NewsItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseContract.IBaseView {

    private static final String TAG = "BaseFragment";
    private String name;
    private PageNewsRecyclerAdapter mPageNewsRecyclerAdapter;
    private BasePresenter mBasePresenter;
    private Integer pageNo = 1; //当前是第几页
    private Integer pageSize = 10; //每次请求新闻的数目
    private boolean isSwipe = true;

    //Adapter那边上拉刷新回调接口实现类对象，上拉刷新后回调到该方法
    private LoadMore mLoadMore = new LoadMore() {
        @Override
        public void loadMoreCallBack() {
            pageNo++;
            isSwipe=false;
            Log.d(TAG, "onRefresh-->"+name+"Fragment上拉刷新");
            if (name.equals("推荐")){
                mBasePresenter.getHotNews(pageNo,pageSize);
            }else{
                News news = new News();
                Log.d(TAG, "loadMoreCallBack-->名字是151256156156156"+name);
                news.setCategory(name);
                mBasePresenter.getPageNews(pageNo,pageSize,news);
            }
        }
    };

    //新闻item点击回调接口实现类对象，点击新闻item后回调到该方法，得到点击的item的对应PageNews的id，跳转到新闻详情Activity，
    //把id放在intent里传过去，由那边请求服务器获取单个新闻数据
    private NewsItemClickListener mNewsItemClickListener = new NewsItemClickListener() {
        @Override
        public void newsItemOnClick(int id) {
            Intent intent = new Intent(getActivity(), SingleNewsDetailActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }
    };

    public BaseFragment(){

    }

    public BaseFragment(String name) {
        this.name = name;
    }

    public RecyclerView mBaseFragment_recycle;
    public SwipeRefreshLayout mBaseFragment_Swipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter = new BasePresenter(BaseFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Log.d(TAG, "onCreateView-->"+name+"初始化了");
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    //初始化BaseFragment的控件
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBaseFragment_recycle = view.findViewById(R.id.baseFragment_recycle);
        mBaseFragment_Swipe = view.findViewById(R.id.baseFragment_swipe);
        //创建具体每个新闻Fragment的适配器
        mPageNewsRecyclerAdapter = new PageNewsRecyclerAdapter(getActivity(), new ArrayList<>(),mLoadMore, mNewsItemClickListener);
        //给RecyclerView对象设置适配器与布局
        mBaseFragment_recycle.setAdapter(mPageNewsRecyclerAdapter);
        mBaseFragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        initListener();
        super.onViewCreated(view, savedInstanceState);
    }

    //初始化控件的监听事件
    private void initListener(){
        mBaseFragment_Swipe.setOnRefreshListener(this);
    }



    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh-->得到新闻");
        pageNo=1;
        isSwipe = true;
        Log.d(TAG, "onRefresh-->"+name+"Fragment下拉刷新");
        Log.d(TAG, "该Fragment的名字-->"+name);
        if (name.equals("推荐")){
            mBasePresenter.getHotNews(pageNo,pageSize);
        }else{
            News news = new News();
            Log.d(TAG, "loadMoreCallBack-->名字是"+name);
            news.setCategory(name);
            mBasePresenter.getPageNews(pageNo,pageSize,news);
        }
    }
    //获取推荐新闻成功
    @Override
    public void getHotNewsSuccess(List<PageNews> pageNews) {
        mBaseFragment_Swipe.setRefreshing(false);
        Log.d(TAG, "getHotNewsSuccess-->获取新闻成功");
        //看标志，决定是把请求到的数组传入Adapter中的哪个方法(上拉刷新还是下拉刷新方法)
        if (isSwipe){
            mPageNewsRecyclerAdapter.setFrontPageNews(pageNews);
        }else {
            mPageNewsRecyclerAdapter.setBehindPageNews(pageNews);
        }

    }
    //获取推荐新闻失败
    @Override
    public void getHotNewsFailed(Throwable throwable) {
//        Toast.makeText(getActivity(), "获取新闻失败", Toast.LENGTH_SHORT).show();
        mBaseFragment_Swipe.setRefreshing(false);
    }
    //获取分页新闻成功
    @Override
    public void getPageNewsSuccess(List<PageNews> pageNews) {
        mBaseFragment_Swipe.setRefreshing(false);
        Log.d(TAG, "getHotNewsSuccess-->获取新闻成功");
        //看标志，决定是把请求到的数组传入Adapter中的哪个方法(上拉刷新还是下拉刷新方法)
        if (isSwipe){
            mPageNewsRecyclerAdapter.setFrontPageNews(pageNews);
        }else {
            mPageNewsRecyclerAdapter.setBehindPageNews(pageNews);
        }
    }
    //获取分页新闻失败
    @Override
    public void getPageNewsFailed(Throwable throwable) {
//        Toast.makeText(getActivity(), "获取新闻失败", Toast.LENGTH_SHORT).show();
        mBaseFragment_Swipe.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}