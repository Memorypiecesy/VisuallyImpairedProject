package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.RecognitionHistoryContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class RecognitionHistoryModel implements RecognitionHistoryContract.IRecognitionHistoryModel {
    //得到OCR识别结果历史
    @Override
    public Flowable<BaseBean<List<OcrHistory>>> getRecognitionHistory(String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getOcrHistory(token);
    }
}
