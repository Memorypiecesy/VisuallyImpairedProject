package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.NewsOfIRContract;
import com.example.visuallyimpairedproject.model.NewsOfIRModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsOfIRPresenter implements NewsOfIRContract.INewsOfIRPresenter {

    private static final String TAG = "NewsOfIRPresenter";
    private NewsOfIRContract.INewsOfIRView mINewsOfIRView;
    private NewsOfIRContract.INewsOfIRModel mINewsOfIRModel;

    public NewsOfIRPresenter(NewsOfIRContract.INewsOfIRView iNewsOfIRView){
        mINewsOfIRView = iNewsOfIRView;
        mINewsOfIRModel = new NewsOfIRModel();
    }

    //得到收藏的OCR识别结果列表
    @Override
    public void getOcrResultCollection(String token) {
        mINewsOfIRModel.getOcrResultCollection(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<OcrHistory>>>() {
                    @Override
                    public void accept(BaseBean<List<OcrHistory>> listBaseBean) throws Throwable {
                        mINewsOfIRView.getOcrResultCollectionSuccess(listBaseBean.getData());
                        Log.d(TAG, "NewsOfIRPresenter处的getOcrResultCollection方法成功");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "NewsOfIRPresenter处的getOcrResultCollection方法失败");
                        mINewsOfIRView.getOcrResultCollectionFailed(throwable);
                    }
                });
    }
}
