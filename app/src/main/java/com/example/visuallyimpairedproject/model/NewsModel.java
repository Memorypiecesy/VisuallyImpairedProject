package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.NewsContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import io.reactivex.rxjava3.core.Flowable;

public class NewsModel implements NewsContract.INewsModel {

    //得到首页搜索框热门搜索文字
    @Override
    public Flowable<BaseBean<News>> getHotTitle() {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getHotTitle();
    }
}
