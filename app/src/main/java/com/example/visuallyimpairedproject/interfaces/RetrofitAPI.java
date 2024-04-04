package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.AttentionAndFans;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Comments;
import com.example.visuallyimpairedproject.bean.CommentsToMe;
import com.example.visuallyimpairedproject.bean.CommentsVo;
import com.example.visuallyimpairedproject.bean.LikeToMe;
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.bean.SingleNewsDetail;
import com.example.visuallyimpairedproject.bean.Token;
import com.example.visuallyimpairedproject.bean.User;
import com.example.visuallyimpairedproject.bean.UserBody;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    //首页的推荐Tab查询
    @GET("/newsController/hot/{pageNo}/{pageSize}")
    Flowable<BaseBean<List<PageNews>>> getHotNews(@Path("pageNo") Integer pageNo, @Path("pageSize") Integer pageSize);

    //首页的搜索框中的热门文字
    @GET("/newsController/searchHot")
    Flowable<BaseBean<News>> getHotTitle();

    //分页查询(首页的新闻查询)
    @POST("/newsController/getNewsByOpr/{pageNo}/{pageSize}")
    Flowable<BaseBean<List<PageNews>>> getPageNews(@Path("pageNo") Integer pageNo, @Path("pageSize") Integer pageSize, @Body News news);

    //单个新闻的详情
    @GET("/newsController/selectNewsFans/{id}")
    Flowable<BaseBean<SingleNewsDetail>> getSingleNewsDetail(@Path("id") Long id, @Header("token") String token);

    //单个新闻评论
    @GET("/comments/article/{id}")
    Flowable<BaseBean<List<Comments>>> getSingleNewsComments(@Path("id") Long id);

    //单个新闻点赞
    @POST("/newsController/likeNews/{id}")
    Flowable<BaseBean<String>> postNewsLike(@Path("id") Long id, @Header("token") String token);

    //单个新闻收藏
    @POST("/newsController/colCollectNews/{id}")
    Flowable<BaseBean<String>> postNewsCollection(@Path("id") Long id, @Header("token") String token);

    //获取用户个人信息
    @GET("/UserController/getAppInfo")
    Flowable<BaseBean<User>> getUserInfo(@Header("token") String token);

    //给文章评论
    @POST("/comments/create")
    Flowable<BaseBean<String>> articleComments(@Body CommentsVo commentsVo, @Header("token") String token);

    //获取粉丝
    @GET("/fans/getAllFans")
    Flowable<BaseBean<List<AttentionAndFans>>> getFans(@Header("token") String token);

    //获取关注
    @GET("/fans/getAllFollower")
    Flowable<BaseBean<List<AttentionAndFans>>> getAttention(@Header("token") String token);

    //上传图片
    @Multipart
    @POST("/system/headerImgUploadApp")
    Flowable<BaseBean<String>> postImage(@Part MultipartBody.Part file);

    //OCR文字识别
    @GET("/OCR/getText")
    Flowable<BaseBean<OcrHistory>> getIdentificationResult(@Query("path") String imagePath, @Header("token") String token);

    //OCR识别历史
    @GET("/OCR/getTextHistory")
    Flowable<BaseBean<List<OcrHistory>>> getOcrHistory(@Header("token") String token);

    //OCR识别结果收藏上传
    @POST("OCR/collect/{id}")
    Flowable<BaseBean<String>> postCollection(@Path("id") Long id, @Header("token") String token);

    //点赞我的人的列表
    @GET("message/likes/{pageNo}/{pageSize}")
    Flowable<BaseBean<List<LikeToMe>>> getLikeToMe(@Path("pageNo") Integer pageNo, @Path("pageSize") Integer pageSize,@Header("token") String token);

    //评论我的人的列表
    @GET("message/comments/{pageNo}/{pageSize}")
    Flowable<BaseBean<List<CommentsToMe>>> getCommentsToMe(@Path("pageNo") Integer pageNo, @Path("pageSize") Integer pageSize, @Header("token") String token);

    //登录
    @POST("/system/login")
    Flowable<BaseBean<Token>> login(@Body UserBody userBody);

    //浏览过的新闻
    @POST("/newsController/historyNews/{pageNo}/{pageSize}")
    Flowable<BaseBean<List<News>>> getNewsHistory(@Path("pageNo") Integer pageNo, @Path("pageSize") Integer pageSize, @Header("token") String token);

    //收藏过的新闻
    @GET("/newsController/getCollectNews/{pageNo}/{pageSize}")
    Flowable<BaseBean<List<News>>> getNewsCollectionHistory(@Path("pageNo") Integer pageNo, @Path("pageSize") Integer pageSize, @Header("token") String token);

}
