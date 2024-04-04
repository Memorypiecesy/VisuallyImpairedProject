package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.presenter.SearchResultPresenter;
import com.example.visuallyimpairedproject.adapters.PageNewsRecyclerAdapter;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.LoadMore;
import com.example.visuallyimpairedproject.interfaces.NewsItemClickListener;
import com.example.visuallyimpairedproject.interfaces.SearchResultContract;
import com.example.visuallyimpairedproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends AppCompatActivity implements SearchResultContract.ISearchResultView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = "SearchResultActivity";
    private SearchResultContract.ISearchResultPresenter mISearchResultPresenter;
    @BindView(R.id.search_result_recycle) RecyclerView search_result_recycle;
    @BindView(R.id.search_result_cardView) CardView search_result_cardView; //搜索框CardView
    @BindView(R.id.search_result_swipe) SwipeRefreshLayout search_result_swipe;
    @BindView(R.id.return_back) ImageView return_back; //返回箭头
    @BindView(R.id.text_search) TextView text_search; //上面搜索框中的文字
    private Integer pageNo = 1; //当前是第几页
    private Integer pageSize = 10; //每次请求新闻的数目
    private boolean isSwipe = true;
    private News mNews; //news对象
    private PageNewsRecyclerAdapter mPageNewsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this); //绑定黄油刀
        mISearchResultPresenter = new SearchResultPresenter(this);
        initOnClick();
        initListener();
        Intent searchActivityIntent = getIntent();
        String search_text = searchActivityIntent.getStringExtra("search_text");
        Log.d(TAG, "要搜索的文字为-->"+search_text);
        text_search.setText(search_text);
        //创建News对象，设置标题和内容
        mNews = new News();
        mNews.setTitle(search_text);
        mISearchResultPresenter.searchNews(pageNo, pageSize, mNews);
        //创建搜索新闻处的适配器
        mPageNewsRecyclerAdapter = new PageNewsRecyclerAdapter(SearchResultActivity.this, new ArrayList<>(), mLoadMore, mNewsItemClickListener);
        //给RecyclerView设置适配器和布局管理器
        search_result_recycle.setAdapter(mPageNewsRecyclerAdapter);
        search_result_recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    //初始化点击事件
    private void initOnClick() {
        return_back.setOnClickListener(this);
    }

    //点击事件具体逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_result_cardView:
            case R.id.return_back:
                finish();
                break;
        }
    }

    //搜索新闻成功
    @Override
    public void searchNewsSuccess(List<PageNews> pageNews) {
        Log.d(TAG, "SearchResultActivity处searchNewsFailed调用，搜索新闻成功，个数-->" + pageNews.size());
        //看标志，决定是把请求到的数组传入Adapter中的哪个方法(上拉刷新还是下拉刷新方法)
        if (isSwipe) {
            mPageNewsRecyclerAdapter.setFrontPageNews(pageNews);
        } else {
            mPageNewsRecyclerAdapter.setBehindPageNews(pageNews);
        }
        search_result_swipe.setRefreshing(false);
    }

    //搜索新闻失败
    @Override
    public void searchNewsFailed(Throwable throwable) {
        Log.d(TAG, "SearchResultActivity处searchNewsFailed调用，搜索新闻失败");
        search_result_swipe.setRefreshing(false);
    }

    //初始化控件的监听事件
    private void initListener() {
        search_result_swipe.setOnRefreshListener(this);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        pageNo = 1;
        isSwipe = true;
        Log.d(TAG, "SearchResultActivity处swipe下拉刷新");
        mISearchResultPresenter.searchNews(pageNo, pageSize, mNews);
    }

    //Adapter那边上拉刷新回调接口实现类对象，上拉刷新后回调到该方法
    private LoadMore mLoadMore = new LoadMore() {
        @Override
        public void loadMoreCallBack() {
            pageNo++;
            isSwipe = false;
            Log.d(TAG, "SearchResultActivity处上拉到底刷新");
            mISearchResultPresenter.searchNews(pageNo, pageSize, mNews);
        }
    };

    //新闻item点击回调接口实现类对象，点击新闻item后回调到该方法，得到点击的item的对应PageNews的id，跳转到新闻详情Activity，
    //把id放在intent里传过去，由那边请求服务器获取单个新闻数据
    private NewsItemClickListener mNewsItemClickListener = new NewsItemClickListener() {
        @Override
        public void newsItemOnClick(int id) {
            Intent intent = new Intent(SearchResultActivity.this, SingleNewsDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    };

}