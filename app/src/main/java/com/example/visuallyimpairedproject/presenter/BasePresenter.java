package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.BaseContract;
import com.example.visuallyimpairedproject.model.BaseModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BasePresenter implements BaseContract.IBasePresenter {
    private BaseContract.IBaseView mIBaseView;
    private BaseContract.IBaseModel mIBaseModel;
    private static final String TAG = "BasePresenter";

    //构造方法中持有BaseFragment和BaseModel对象
    public BasePresenter(BaseContract.IBaseView iBaseView){
        mIBaseView = iBaseView;
        mIBaseModel = new BaseModel();
    }
    //调用Model层的方法获取推荐新闻并处理，让View层展示
    @Override
    public void getHotNews(Integer pageNo, Integer pageSize) {
        mIBaseModel.getHotNews(pageNo, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<PageNews>>>() {
                    @Override
                    public void accept(BaseBean<List<PageNews>> listBaseBean) throws Throwable {
                        mIBaseView.getHotNewsSuccess(listBaseBean.getData());
                        Log.d(TAG, "accept-->Presenter处的推荐新闻个数-->"+listBaseBean.getData().size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "failed-->获取推荐新闻失败"+throwable);
                        mIBaseView.getHotNewsFailed(throwable);
                    }
                });

    }
    //调用Model层的方法获取分页新闻并处理，让View层展示
    @Override
    public void getPageNews(Integer pageNo, Integer pageSize, News news) {
        mIBaseModel.getPageNews(pageNo,pageSize,news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<PageNews>>>() {
                    @Override
                    public void accept(BaseBean<List<PageNews>> listBaseBean) throws Throwable {
                        mIBaseView.getPageNewsSuccess(listBaseBean.getData());
                        Log.d(TAG, "accept-->Presenter处的分页新闻个数-->"+listBaseBean.getData().size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "failed-->获取分页新闻失败"+throwable);
                        mIBaseView.getPageNewsFailed(throwable);
                    }
                });
    }
}
