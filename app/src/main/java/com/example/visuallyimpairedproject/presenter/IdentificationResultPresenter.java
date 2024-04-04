package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.interfaces.IdentificationResultContract;
import com.example.visuallyimpairedproject.model.IdentificationResultModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IdentificationResultPresenter implements IdentificationResultContract.IdentificationResultPresenter {

    private static final String TAG = "IdentificationResultP";
    private IdentificationResultContract.IdentificationResultView mIdentificationResultView;
    private IdentificationResultContract.IdentificationResultModel mIdentificationResultModel;

    public IdentificationResultPresenter(IdentificationResultContract.IdentificationResultView identificationResultView){
        mIdentificationResultView = identificationResultView;
        mIdentificationResultModel= new IdentificationResultModel();
    }

    //上传OCR收藏结果
    @Override
    public void postCollection(Long id, String token) {
        mIdentificationResultModel.postCollection(id,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<String>>() {
                    @Override
                    public void accept(BaseBean<String> stringBaseBean) throws Throwable {
                        Log.d(TAG, "IdentificationResultPresenter处postCollection成功-->"+stringBaseBean.getData());
                        mIdentificationResultView.postCollectionSuccess(stringBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "IdentificationResultPresenter处postCollection出错-->"+throwable);
                        mIdentificationResultView.postCollectionFailed(throwable);
                    }
                });
    }
}
