package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.AttentionAndFans;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.interfaces.MyAttentionAndFansContract;
import com.example.visuallyimpairedproject.model.MyAttentionAndFansModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyAttentionAndFansPresenter implements MyAttentionAndFansContract.IMyAttentionAndFansPresenter {

    private static final String TAG = "MyAttentionAndFansP";
    private MyAttentionAndFansContract.IMyAttentionAndFansModel mIMyAttentionAndFansModel;
    private MyAttentionAndFansContract.IMyAttentionAndFansView mIMyAttentionAndFansView;

    public MyAttentionAndFansPresenter(MyAttentionAndFansContract.IMyAttentionAndFansView myAttentionAndFansView) {
        mIMyAttentionAndFansView = myAttentionAndFansView;
        mIMyAttentionAndFansModel = new MyAttentionAndFansModel();
    }

    //得到关注
    @Override
    public void getMyAttention(String token) {
        mIMyAttentionAndFansModel.getMyAttention(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<AttentionAndFans>>>() {
                    @Override
                    public void accept(BaseBean<List<AttentionAndFans>> attentionAndFansBaseBean) throws Throwable {
                        mIMyAttentionAndFansView.getMyAttentionAndFansSuccess(attentionAndFansBaseBean.getData());
                        Log.d(TAG, "MyAttentionAndFansPresenter处获取关注列表成功，个数-->" + attentionAndFansBaseBean.getData().size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "MyAttentionAndFansPresenter处获取关注列表失败-->" + throwable);
                        mIMyAttentionAndFansView.getMyAttentionAndFansFailed(throwable);
                    }
                });
    }

    //得到粉丝
    @Override
    public void getMyAFans(String token) {
        mIMyAttentionAndFansModel.getMyFans(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<AttentionAndFans>>>() {
                    @Override
                    public void accept(BaseBean<List<AttentionAndFans>> attentionAndFansBaseBean) throws Throwable {
                        mIMyAttentionAndFansView.getMyAttentionAndFansSuccess(attentionAndFansBaseBean.getData());
                        Log.d(TAG, "MyAttentionAndFansPresenter处获取粉丝列表成功，个数-->" + attentionAndFansBaseBean.getData().size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "MyAttentionAndFansPresenter处获取粉丝列表失败-->" + throwable);
                        mIMyAttentionAndFansView.getMyAttentionAndFansFailed(throwable);
                    }
                });
    }
}
