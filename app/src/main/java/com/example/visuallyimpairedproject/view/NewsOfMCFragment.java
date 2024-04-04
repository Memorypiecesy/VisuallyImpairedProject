package com.example.visuallyimpairedproject.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.visuallyimpairedproject.presenter.NewsOfMCPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.NewsAdapter;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.LoadMore;
import com.example.visuallyimpairedproject.interfaces.NewsOfMCContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsOfMCFragment extends Fragment implements NewsOfMCContract.INewsOfMCView {

    private static final String TAG = "NewsOfMCFragment";
    private NewsAdapter mNewsAdapter;
    private SharedPreferences sp;
    private Integer pageNo = 1; //当前是第几页
    private Integer pageSize = 10; //每次请求新闻的数目
    private NewsOfMCContract.INewsOfMCPresenter mINewsOfMCPresenter;
    @BindView(R.id.news_of_mc_fragment_recycle) RecyclerView news_of_mc_fragment_recycle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化SharedPreferences
        sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mNewsAdapter = new NewsAdapter(getActivity(),new ArrayList<>(),mLoadMore);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View newsOfMyMCFragment = inflater.inflate(R.layout.fragment_news_of_m_c, container, false);
        ButterKnife.bind(this, newsOfMyMCFragment);
        news_of_mc_fragment_recycle.setAdapter(mNewsAdapter);
        news_of_mc_fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mINewsOfMCPresenter = new NewsOfMCPresenter(this); //得到P层对象
        mINewsOfMCPresenter.getNewsHistory(pageNo,pageSize,sp.getString("token",""));
        return newsOfMyMCFragment;
    }

    //Adapter那边上拉刷新回调接口实现类对象，上拉刷新后回调到该方法
    private LoadMore mLoadMore = new LoadMore() {
        @Override
        public void loadMoreCallBack() {
            pageNo++;
            mINewsOfMCPresenter.getNewsHistory(pageNo,pageSize,sp.getString("token",""));
        }
    };

    //得到新闻收藏历史成功
    @Override
    public void getNewsHistorySuccess(List<News> news) {
        Log.d(TAG, "BrowsingHistoryActivity处getNewsHistorySuccess方法调用，个数为-->"+news.size());
        mNewsAdapter.addNews(news);
    }
    //得到新闻收藏历史失败
    @Override
    public void getNewsHistoryFailed(Throwable throwable) {
        Log.d(TAG, "BrowsingHistoryActivity处getNewsHistoryFailed方法调用-->"+throwable);
    }
}