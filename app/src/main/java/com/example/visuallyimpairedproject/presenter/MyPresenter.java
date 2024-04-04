package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.User;
import com.example.visuallyimpairedproject.interfaces.MyContract;
import com.example.visuallyimpairedproject.model.MyModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyPresenter implements MyContract.IMyPresenter {

    private static final String TAG = "MyPresenter";
    private MyContract.IMyView mIMyView;
    private MyContract.IMyModel mIMyModel;

    public MyPresenter(MyContract.IMyView myView){
        mIMyView = myView;
        mIMyModel = new MyModel();
    }

    @Override
    public void getUserInfo(String token) {
        mIMyModel.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<User>>() {
                    @Override
                    public void accept(BaseBean<User> userBaseBean) throws Throwable {
                        Log.d(TAG, "MyPresenter接收User成功-->"+userBaseBean.getData());
                        mIMyView.getUserInfoSuccess(userBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "MyPresenter接收User失败-->"+throwable);
                    }
                });
    }
}
