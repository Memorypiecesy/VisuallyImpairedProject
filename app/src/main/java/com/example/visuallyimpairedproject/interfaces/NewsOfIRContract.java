package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface    NewsOfIRContract {
    interface INewsOfIRView {
        void getOcrResultCollectionSuccess(List<OcrHistory> ocrHistories); //得到收藏的OCR识别结果列表成功
        void getOcrResultCollectionFailed(Throwable throwable); //得到收藏的OCR识别结果列表失败
    }
    interface INewsOfIRPresenter {
        void getOcrResultCollection(String token); //得到收藏的OCR识别结果列表
    }
    interface INewsOfIRModel {
        Flowable<BaseBean<List<OcrHistory>>> getOcrResultCollection(String token); //得到收藏的OCR识别结果列表
    }
}
