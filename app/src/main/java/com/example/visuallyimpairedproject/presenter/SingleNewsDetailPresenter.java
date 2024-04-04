package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Comments;
import com.example.visuallyimpairedproject.bean.CommentsVo;
import com.example.visuallyimpairedproject.bean.SingleNewsDetail;
import com.example.visuallyimpairedproject.interfaces.SingleNewsDetailContract;
import com.example.visuallyimpairedproject.model.SingleNewsDetailModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SingleNewsDetailPresenter implements SingleNewsDetailContract.ISingleNewsDetailPresenter {

    private static final String TAG = "SingleNewsDetail";
    private SingleNewsDetailContract.ISingleNewsDetailView mISingleNewsDetailView;
    private SingleNewsDetailContract.ISingleNewsDetailModel mISingleNewsDetailModel;

    //构造方法中持有SingleNewsDetailActivity和SingleNewsDetailModel对象
    public SingleNewsDetailPresenter(SingleNewsDetailContract.ISingleNewsDetailView iSingleNewsDetailView) {
        mISingleNewsDetailView = iSingleNewsDetailView;
        mISingleNewsDetailModel = new SingleNewsDetailModel();
    }

    //调用Model层的方法获取单个新闻详情并处理，让View层展示
    @Override
    public void getSingleNewsDetail(Long id, String token) {
        mISingleNewsDetailModel.getSingleNewsDetail(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<SingleNewsDetail>>() {
                    @Override
                    public void accept(BaseBean<SingleNewsDetail> listBaseBean) throws Throwable {
                        Log.d(TAG, "accept-->Presenter处获取的SingleNewsDetail为" + listBaseBean.getData());
                        mISingleNewsDetailView.getSingleNewsDetailSuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "failed-->获取单个新闻详情失败" + throwable);
                        mISingleNewsDetailView.getSingleNewsDetailFailed(throwable);
                    }
                });
    }

    //调用Model层的方法获取单个新闻评论并处理，让View层展示
    @Override
    public void getSingleNewsComments(Long id) {
        mISingleNewsDetailModel.getSingleNewsComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<Comments>>>() {
                    @Override
                    public void accept(BaseBean<List<Comments>> listBaseBean) throws Throwable {
                        Log.d(TAG, "accept-->Presenter处获取的Comments为" + listBaseBean.getData());
                        mISingleNewsDetailView.getSingleNewsCommentsSuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "failed-->获取单个新闻评论失败" + throwable);
                        mISingleNewsDetailView.getSingleNewsCommentsFailed(throwable);
                    }
                });
    }

    //调用Model层的方法发送评论
    @Override
    public void articleComments(CommentsVo commentsVo, String token) {
        mISingleNewsDetailModel.articleComments(commentsVo, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<String>>() {
                    @Override
                    public void accept(BaseBean<String> stringBaseBean) throws Throwable {
                        mISingleNewsDetailView.articleCommentsSuccess(stringBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "articleComments方法处失败");
                    }
                });
    }

    //给文章点赞
    @Override
    public void postNewsLike(Long id, String token) {
        mISingleNewsDetailModel.postNewsLike(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<String>>() {
                    @Override
                    public void accept(BaseBean<String> stringBaseBean) throws Throwable {
                        mISingleNewsDetailView.postNewsLikeSuccess(stringBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mISingleNewsDetailView.postNewsLikeFailed(throwable);
                    }
                });
    }

    //给文章收藏
    @Override
    public void postNewsCollection(Long id, String token) {
        mISingleNewsDetailModel.postNewsCollection(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<String>>() {
                    @Override
                    public void accept(BaseBean<String> stringBaseBean) throws Throwable {
                        mISingleNewsDetailView.postNewsCollectionSuccess(stringBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mISingleNewsDetailView.postNewsCollectionFailed(throwable);
                    }
                });
    }
}

