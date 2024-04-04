package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface RecognitionHistoryContract {
    interface IRecognitionHistoryView {
        void getRecognitionHistorySuccess(List<OcrHistory> ocrHistories);
        void getRecognitionHistoryFailed(Throwable throwable);
    }
    interface IRecognitionHistoryPresenter {
        void getRecognitionHistory(String token);
    }
    interface IRecognitionHistoryModel {
        Flowable<BaseBean<List<OcrHistory>>> getRecognitionHistory(String token);
    }
}
