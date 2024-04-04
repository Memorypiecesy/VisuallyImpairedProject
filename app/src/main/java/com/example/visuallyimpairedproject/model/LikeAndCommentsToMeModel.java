package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.CommentsToMe;
import com.example.visuallyimpairedproject.bean.LikeToMe;
import com.example.visuallyimpairedproject.interfaces.LikeAndCommentsToMeContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class LikeAndCommentsToMeModel implements LikeAndCommentsToMeContract.ILikeAndCommentsToMeModel {
    //得到点赞我的人的列表
    @Override
    public Flowable<BaseBean<List<LikeToMe>>> getLikeToMe(Integer pageNo, Integer pageSize, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getLikeToMe(pageNo,pageSize,token);
    }
    //得到评论我的人的列表
    @Override
    public Flowable<BaseBean<List<CommentsToMe>>> getCommentsToMe(Integer pageNo, Integer pageSize, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getCommentsToMe(pageNo,pageSize,token);
    }
}
