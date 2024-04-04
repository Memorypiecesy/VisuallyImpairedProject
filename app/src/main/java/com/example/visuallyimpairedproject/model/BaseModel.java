package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.BaseContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class BaseModel implements BaseContract.IBaseModel {

    private static final String TAG = "BaseModel";
    //请求推荐新闻的方法
    @Override
    public Flowable<BaseBean<List<PageNews>>> getHotNews(Integer pageNo, Integer pageSize) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getHotNews(pageNo,pageSize);
    }
    //请求分页新闻的方法
    @Override
    public Flowable<BaseBean<List<PageNews>>> getPageNews(Integer pageNo, Integer pageSize, News news) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getPageNews(pageNo,pageSize,news);
    }

}
