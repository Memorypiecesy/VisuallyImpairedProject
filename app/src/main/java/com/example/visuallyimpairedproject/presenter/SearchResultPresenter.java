package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.SearchResultContract;
import com.example.visuallyimpairedproject.model.SearchResultModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchResultPresenter implements SearchResultContract.ISearchResultPresenter {

    private SearchResultContract.ISearchResultModel mISearchResultModel;
    private SearchResultContract.ISearchResultView mISearchResultView;

    public SearchResultPresenter(SearchResultContract.ISearchResultView iSearchResultView){
        mISearchResultView = iSearchResultView;
        mISearchResultModel = new SearchResultModel();
    }

    //搜索新闻
    @Override
    public void searchNews(Integer pageNo, Integer pageSize, News news) {
        mISearchResultModel.searchNews(pageNo, pageSize, news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<PageNews>>>() {
                    @Override
                    public void accept(BaseBean<List<PageNews>> listBaseBean) throws Throwable {
                        mISearchResultView.searchNewsSuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mISearchResultView.searchNewsFailed(throwable);
                    }
                });
    }
}

