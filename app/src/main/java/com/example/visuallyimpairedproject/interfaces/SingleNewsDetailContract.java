package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Comments;
import com.example.visuallyimpairedproject.bean.CommentsVo;
import com.example.visuallyimpairedproject.bean.SingleNewsDetail;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface SingleNewsDetailContract {
    interface ISingleNewsDetailView{
        void getSingleNewsDetailSuccess(SingleNewsDetail singleNewsDetail); //得到单个新闻详情成功
        void getSingleNewsDetailFailed(Throwable throwable); //得到单个新闻详情失败
        void getSingleNewsCommentsSuccess(List<Comments> comments); //得到单个新闻评论成功
        void getSingleNewsCommentsFailed(Throwable throwable); //得到单个新闻评论失败
        void articleCommentsSuccess(String success); //给文章评论成功
        void postNewsLikeSuccess(String success); //给文章点赞，上传点赞结果成功
        void postNewsLikeFailed(Throwable throwable); //给文章点赞，上传点赞结果失败
        void postNewsCollectionSuccess(String success); //收藏文章成功
        void postNewsCollectionFailed(Throwable throwable); //收藏文章失败
    }
    interface ISingleNewsDetailPresenter{
        void getSingleNewsDetail(Long id,String token); //得到单个新闻详情
        void getSingleNewsComments(Long id); //得到单个新闻评论
        void articleComments(CommentsVo commentsVo,String token); //给文章评论
        void postNewsLike(Long id,String token); //给文章点赞
        void postNewsCollection(Long id,String token); //收藏文章
    }
    interface ISingleNewsDetailModel{
        Flowable<BaseBean<SingleNewsDetail>> getSingleNewsDetail(Long id,String token); //得到单个新闻详情
        Flowable<BaseBean<List<Comments>>> getSingleNewsComments(Long id); //得到单个新闻评论
        Flowable<BaseBean<String>> articleComments(CommentsVo commentsVo,String token); //给文章评论
        Flowable<BaseBean<String>> postNewsLike(Long id,String token); //给文章点赞
        Flowable<BaseBean<String>> postNewsCollection(Long id,String token); //收藏文章
    }
}
