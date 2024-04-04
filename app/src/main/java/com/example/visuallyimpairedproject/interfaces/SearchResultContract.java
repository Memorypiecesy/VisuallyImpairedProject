package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface SearchResultContract {
    interface ISearchResultView {
        void searchNewsSuccess(List<PageNews> pageNews); //搜索新闻成功
        void searchNewsFailed(Throwable throwable); //搜索新闻失败
    }
    interface ISearchResultPresenter {
        void searchNews(Integer pageNo, Integer pageSize, News news); //搜索新闻
    }
    interface ISearchResultModel {
        Flowable<BaseBean<List<PageNews>>> searchNews(Integer pageNo, Integer pageSize, News news); //搜索新闻
    }

}
