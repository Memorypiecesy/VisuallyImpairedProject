package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.visuallyimpairedproject.presenter.BrowsingHistoryPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.NewsAdapter;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.BrowsingHistoryContract;
import com.example.visuallyimpairedproject.interfaces.LoadMore;
import com.example.visuallyimpairedproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowsingHistoryActivity extends AppCompatActivity implements BrowsingHistoryContract.IBrowsingHistoryView {


    private static final String TAG = "BrowsingHistoryActivity";
    private SharedPreferences sp;
    private BrowsingHistoryContract.IBrowsingHistoryPresenter mIBrowsingHistoryPresenter;
    @BindView(R.id.browsing_history_recycle) RecyclerView browsing_history_recycle;
    private NewsAdapter mNewsAdapter;
    private Integer pageNo = 1; //当前是第几页
    private Integer pageSize = 10; //每次请求新闻的数目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);
        ButterKnife.bind(this);
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        mNewsAdapter = new NewsAdapter(this,new ArrayList<>(),mLoadMore);
        browsing_history_recycle.setAdapter(mNewsAdapter); //设置适配器
        browsing_history_recycle.setLayoutManager(new LinearLayoutManager(this)); //设置布局管理器
        mIBrowsingHistoryPresenter = new BrowsingHistoryPresenter(this); //得到P层对象
        mIBrowsingHistoryPresenter.getNewsHistory(pageNo,pageSize,sp.getString("token",""));
    }
    //得到新闻浏览历史成功
    @Override
    public void getNewsHistorySuccess(List<News> news) {
        Log.d(TAG, "BrowsingHistoryActivity处getNewsHistorySuccess方法调用，个数为-->"+news.size());
        mNewsAdapter.addNews(news);
    }
    //得到新闻浏览历史失败
    @Override
    public void getNewsHistoryFailed(Throwable throwable) {
        Log.d(TAG, "BrowsingHistoryActivity处getNewsHistoryFailed方法调用-->"+throwable);
    }

    //Adapter那边上拉刷新回调接口实现类对象，上拉刷新后回调到该方法
    private LoadMore mLoadMore = new LoadMore() {
        @Override
        public void loadMoreCallBack() {
            pageNo++;
            mIBrowsingHistoryPresenter.getNewsHistory(pageNo,pageSize,sp.getString("token",""));
        }
    };
}