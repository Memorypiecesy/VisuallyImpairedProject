package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;
import com.example.visuallyimpairedproject.interfaces.SearchResultContract;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SearchResultModel implements SearchResultContract.ISearchResultModel {

    //搜索新闻
    @Override
    public Flowable<BaseBean<List<PageNews>>> searchNews(Integer pageNo, Integer pageSize, News news) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getPageNews(pageNo,pageSize,news);
    }
}
