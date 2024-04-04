package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.NewsOfMCContract;
import com.example.visuallyimpairedproject.model.NewsOfMCModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.functions.Consumer;

public class NewsOfMCPresenter implements NewsOfMCContract.INewsOfMCPresenter {

    private NewsOfMCContract.INewsOfMCModel mINewsOfMCModel;
    private NewsOfMCContract.INewsOfMCView mINewsOfMCView;

    public NewsOfMCPresenter(NewsOfMCContract.INewsOfMCView iNewsOfMCView){
        mINewsOfMCView = iNewsOfMCView;
        mINewsOfMCModel = new NewsOfMCModel();
    }

    //得到收藏新闻历史
    @Override
    public void getNewsHistory(Integer pageNo, Integer pageSize, String token) {
        mINewsOfMCModel.getNewsHistory(pageNo,pageSize,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<News>>>() {
                    @Override
                    public void accept(BaseBean<List<News>> listBaseBean) throws Throwable {
                        mINewsOfMCView.getNewsHistorySuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mINewsOfMCView.getNewsHistoryFailed(throwable);
                    }
                });
    }
}
