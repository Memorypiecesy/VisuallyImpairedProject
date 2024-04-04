package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.CommentsToMe;
import com.example.visuallyimpairedproject.bean.LikeToMe;
import com.example.visuallyimpairedproject.interfaces.LikeAndCommentsToMeContract;
import com.example.visuallyimpairedproject.model.LikeAndCommentsToMeModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LikeAndCommentsToMePresenter implements LikeAndCommentsToMeContract.ILikeAndCommentsToMePresenter {

    private LikeAndCommentsToMeContract.ILikeAndCommentsToMeModel mILikeAndCommentsToMeModel;
    private LikeAndCommentsToMeContract.ILikeAndCommentsToMeView mILikeAndCommentsToMeView;

    public LikeAndCommentsToMePresenter(LikeAndCommentsToMeContract.ILikeAndCommentsToMeView iLikeAndCommentsToMeView){
        mILikeAndCommentsToMeView = iLikeAndCommentsToMeView;
        mILikeAndCommentsToMeModel = new LikeAndCommentsToMeModel();
    }

    //得到点赞我的人的列表
    @Override
    public void getLikeToMe(Integer pageNo, Integer pageSize, String token) {
        mILikeAndCommentsToMeModel.getLikeToMe(pageNo,pageSize,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<LikeToMe>>>() {
                    @Override
                    public void accept(BaseBean<List<LikeToMe>> listBaseBean) throws Throwable {
                        mILikeAndCommentsToMeView.getLikeToMeSuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mILikeAndCommentsToMeView.getLikeToMeFailed(throwable);
                    }
                });
    }
    //得到评论我的人的列表
    @Override
    public void getCommentsToMe(Integer pageNo, Integer pageSize, String token) {
        mILikeAndCommentsToMeModel.getCommentsToMe(pageNo,pageSize,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<List<CommentsToMe>>>() {
                    @Override
                    public void accept(BaseBean<List<CommentsToMe>> listBaseBean) throws Throwable {
                        mILikeAndCommentsToMeView.getCommentsToMeSuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mILikeAndCommentsToMeView.getCommentsToMeFailed(throwable);
                    }
                });
    }
}
