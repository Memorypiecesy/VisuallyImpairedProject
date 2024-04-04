package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Token;
import com.example.visuallyimpairedproject.bean.UserBody;
import com.example.visuallyimpairedproject.interfaces.LoginContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import io.reactivex.rxjava3.core.Flowable;

public class LoginModel implements LoginContract.ILoginModel {

    //请求登录的方法
    @Override
    public Flowable<BaseBean<Token>> login(UserBody userBody) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).login(userBody);
    }
}
