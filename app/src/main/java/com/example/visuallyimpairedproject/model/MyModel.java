package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.User;
import com.example.visuallyimpairedproject.interfaces.MyContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import io.reactivex.rxjava3.core.Flowable;

public class MyModel implements MyContract.IMyModel {
    @Override
    public Flowable<BaseBean<User>> getUserInfo(String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getUserInfo(token);
    }
}
