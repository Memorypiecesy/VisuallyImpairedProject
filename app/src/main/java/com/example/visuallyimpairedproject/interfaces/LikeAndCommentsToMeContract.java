package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.CommentsToMe;
import com.example.visuallyimpairedproject.bean.LikeToMe;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface LikeAndCommentsToMeContract {
    interface ILikeAndCommentsToMeView {
        void getLikeToMeSuccess(List<LikeToMe> likeToMes); //得到点赞我的人的列表成功
        void getLikeToMeFailed(Throwable throwable); //得到点赞我的人的列表失败
        void getCommentsToMeSuccess(List<CommentsToMe> commentsToMes); //得到评论我的人的列表成功
        void getCommentsToMeFailed(Throwable throwable); //得到评论我的人的列表失败
    }
    interface ILikeAndCommentsToMePresenter {
        void getLikeToMe(Integer pageNo, Integer pageSize, String token); //得到点赞我的人的列表
        void getCommentsToMe(Integer pageNo, Integer pageSize, String token); //得到评论我的人的列表
    }
    interface ILikeAndCommentsToMeModel {
        Flowable<BaseBean<List<LikeToMe>>> getLikeToMe(Integer pageNo, Integer pageSize, String token); //得到点赞我的人的列表
        Flowable<BaseBean<List<CommentsToMe>>> getCommentsToMe(Integer pageNo, Integer pageSize, String token); //得到评论我的人的列表
    }

}
