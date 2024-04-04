package com.example.visuallyimpairedproject.model;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Comments;
import com.example.visuallyimpairedproject.bean.CommentsVo;
import com.example.visuallyimpairedproject.bean.SingleNewsDetail;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;
import com.example.visuallyimpairedproject.interfaces.SingleNewsDetailContract;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class SingleNewsDetailModel implements SingleNewsDetailContract.ISingleNewsDetailModel {
    //请求单个新闻详情的方法
    @Override
    public Flowable<BaseBean<SingleNewsDetail>> getSingleNewsDetail(Long id,String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getSingleNewsDetail(id,token);
    }
    //请求单个新闻评论的方法
    @Override
    public Flowable<BaseBean<List<Comments>>> getSingleNewsComments(Long id) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getSingleNewsComments(id);
    }
    //发送评论
    @Override
    public Flowable<BaseBean<String>> articleComments(CommentsVo commentsVo, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).articleComments(commentsVo,token);
    }
    //给文章点赞
    @Override
    public Flowable<BaseBean<String>> postNewsLike(Long id, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).postNewsLike(id,token);
    }
    //收藏文章
    @Override
    public Flowable<BaseBean<String>> postNewsCollection(Long id, String token) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).postNewsCollection(id,token);
    }
}
