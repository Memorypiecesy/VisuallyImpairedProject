package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface BaseContract {
    interface IBaseView {
        void getHotNewsSuccess(List<PageNews> pageNews);
        void getHotNewsFailed(Throwable throwable);
        void getPageNewsSuccess(List<PageNews> pageNews);
        void getPageNewsFailed(Throwable throwable);
    }
    interface IBasePresenter {
        void getHotNews(Integer pageNo, Integer pageSize);
        void getPageNews(Integer pageNo, Integer pageSize, News news);
    }
    interface IBaseModel {
        Flowable<BaseBean<List<PageNews>>> getHotNews(Integer pageNo, Integer pageSize);
        Flowable<BaseBean<List<PageNews>>> getPageNews(Integer pageNo, Integer pageSize, News news);
    }
}
