package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;

import io.reactivex.rxjava3.core.Flowable;

//NewsFragment的契约接口
public interface NewsContract {
    interface INewsView {
        void getHotTitleSuccess(News news); //得到首页搜索框热门搜索文字成功
        void getHotTitleFailed(Throwable throwable); //得到首页搜索框热门搜索文字失败
    }
    interface INewsPresenter {
        void getHotTitle(); //得到首页搜索框热门搜索文字
    }
    interface INewsModel {
        Flowable<BaseBean<News>> getHotTitle(); //得到首页搜索框热门搜索文字
    }

}
