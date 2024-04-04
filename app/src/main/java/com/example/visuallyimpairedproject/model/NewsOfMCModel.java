package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.NewsOfMCContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class NewsOfMCModel implements NewsOfMCContract.INewsOfMCModel {
    //得到新闻收藏历史
    @Override
    public Flowable<BaseBean<List<News>>> getNewsHistory(Integer pageNo, Integer pageSize, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getNewsCollectionHistory(pageNo,pageSize,token);
    }
}
