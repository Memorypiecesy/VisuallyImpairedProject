package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.interfaces.IdentificationResultContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import io.reactivex.rxjava3.core.Flowable;

public class IdentificationResultModel implements IdentificationResultContract.IdentificationResultModel {
    //上传OCR收藏结果
    @Override
    public Flowable<BaseBean<String>> postCollection(Long id, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).postCollection(id,token);
    }
}
