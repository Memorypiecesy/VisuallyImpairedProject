package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.BrowsingHistoryContract;
import com.example.visuallyimpairedproject.model.BrowsingHistoryModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class BrowsingHistoryPresenter implements BrowsingHistoryContract.IBrowsingHistoryPresenter {

    private BrowsingHistoryContract.IBrowsingHistoryView mIBrowsingHistoryView;
    private BrowsingHistoryContract.IBrowsingHistoryModel mIBrowsingHistoryModel;

    public BrowsingHistoryPresenter(BrowsingHistoryContract.IBrowsingHistoryView iBrowsingHistoryView){
        mIBrowsingHistoryView = iBrowsingHistoryView;
        mIBrowsingHistoryModel = new BrowsingHistoryModel();
    }

    //得到新闻浏览历史
    @Override
    public void getNewsHistory(Integer pageNo,Integer pageSize,String token) {
        mIBrowsingHistoryModel.getNewsHistory(pageNo,pageSize,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<News>>>() {
                    @Override
                    public void accept(BaseBean<List<News>> listBaseBean) throws Throwable {
                        mIBrowsingHistoryView.getNewsHistorySuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mIBrowsingHistoryView.getNewsHistoryFailed(throwable);
                    }
                });

    }
}
