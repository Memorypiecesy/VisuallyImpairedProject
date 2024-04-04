package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.NewsContract;
import com.example.visuallyimpairedproject.model.NewsModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsPresenter implements NewsContract.INewsPresenter {

    private NewsContract.INewsView mINewsView;
    private NewsContract.INewsModel mINewsModel;

    //构造方法中持有NewsFragment和NewsModel对象
    public NewsPresenter(NewsContract.INewsView iNewsView){
        mINewsView = iNewsView;
        mINewsModel = new NewsModel();
    }

    @Override
    public void getHotTitle() {
        mINewsModel.getHotTitle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<News>>() {
                    @Override
                    public void accept(BaseBean<News> newsBaseBean) throws Throwable {
                        mINewsView.getHotTitleSuccess(newsBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mINewsView.getHotTitleFailed(throwable);
                    }
                });
    }
}
