package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.AttentionAndFans;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.interfaces.MyAttentionAndFansContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MyAttentionAndFansModel implements MyAttentionAndFansContract.IMyAttentionAndFansModel {
    //得到关注列表
    @Override
    public Flowable<BaseBean<List<AttentionAndFans>>> getMyAttention(String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getAttention(token);
    }
    //得到粉丝
    @Override
    public Flowable<BaseBean<List<AttentionAndFans>>> getMyFans(String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getFans(token);
    }
}
