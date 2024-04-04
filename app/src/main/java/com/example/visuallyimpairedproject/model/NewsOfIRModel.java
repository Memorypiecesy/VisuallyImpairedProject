package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.NewsOfIRContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class NewsOfIRModel implements NewsOfIRContract.INewsOfIRModel {
    @Override
    public Flowable<BaseBean<List<OcrHistory>>> getOcrResultCollection(String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getOcrHistory(token);
    }
}
