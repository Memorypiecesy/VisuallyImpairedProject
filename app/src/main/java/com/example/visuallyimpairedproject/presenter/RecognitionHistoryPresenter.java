package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.RecognitionHistoryContract;
import com.example.visuallyimpairedproject.model.RecognitionHistoryModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecognitionHistoryPresenter implements RecognitionHistoryContract.IRecognitionHistoryPresenter {

    private static final String TAG = "RecognitionHistoryP";
    private RecognitionHistoryContract.IRecognitionHistoryView mIRecognitionHistoryView;
    private RecognitionHistoryContract.IRecognitionHistoryModel mIRecognitionHistoryModel;

    public RecognitionHistoryPresenter(RecognitionHistoryContract.IRecognitionHistoryView iRecognitionHistoryView){
        mIRecognitionHistoryView = iRecognitionHistoryView;
        mIRecognitionHistoryModel = new RecognitionHistoryModel();
    }

    //得到OCR识别结果历史
    @Override
    public void getRecognitionHistory(String token) {
        mIRecognitionHistoryModel.getRecognitionHistory(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<OcrHistory>>>() {
                    @Override
                    public void accept(BaseBean<List<OcrHistory>> listBaseBean) throws Throwable {
                        Log.d(TAG, "RecognitionHistoryPresenter处的getRecognitionHistory方法调用成功");
                        mIRecognitionHistoryView.getRecognitionHistorySuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "RecognitionHistoryPresenter处的getRecognitionHistory方法调用失败"+throwable);
                        mIRecognitionHistoryView.getRecognitionHistoryFailed(throwable);
                    }
                });
    }
}
