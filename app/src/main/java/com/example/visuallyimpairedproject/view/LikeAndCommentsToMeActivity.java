package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.visuallyimpairedproject.presenter.LikeAndCommentsToMePresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.LikeAndCommentsToMeAdapter;
import com.example.visuallyimpairedproject.bean.CommentsToMe;
import com.example.visuallyimpairedproject.bean.LikeToMe;
import com.example.visuallyimpairedproject.interfaces.LikeAndCommentsToMeContract;
import com.example.visuallyimpairedproject.interfaces.LoadMore;
import com.example.visuallyimpairedproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LikeAndCommentsToMeActivity extends AppCompatActivity implements View.OnClickListener, LikeAndCommentsToMeContract.ILikeAndCommentsToMeView {

    private static final String TAG = "LikeAndCommentsToMeV";
    @BindView(R.id.return_back) ImageView return_back; //返回箭头
    @BindView(R.id.like_and_comments_to_me_recycle) RecyclerView like_and_comments_to_me_recycle; //RecyclerView
    @BindView(R.id.like_and_comments_to_me_text) TextView like_and_comments_to_me_text; //"获得的赞或回复我的"TextView
    private String mTitle_name;
    private LikeAndCommentsToMeAdapter mLikeAndCommentsToMeAdapter;
    private LikeAndCommentsToMeContract.ILikeAndCommentsToMePresenter mILikeAndCommentsToMePresenter;
    private Integer pageNo = 1; //当前是第几页
    private Integer pageSize = 10; //每次请求新闻的数目
    private SharedPreferences sp;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_and_comments_to_me);
        ButterKnife.bind(this);
        initOnClick();
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        mToken = sp.getString("token", "");
        mILikeAndCommentsToMePresenter = new LikeAndCommentsToMePresenter(this); //初始化P层对象
        //根据不同的界面
        Intent intent = getIntent();
        mTitle_name = intent.getStringExtra("title_name");
        like_and_comments_to_me_text.setText(mTitle_name);
        ArrayList<Object> objects = new ArrayList<>();
        mLikeAndCommentsToMeAdapter = new LikeAndCommentsToMeAdapter(this,objects);
        like_and_comments_to_me_recycle.setAdapter(mLikeAndCommentsToMeAdapter);
        like_and_comments_to_me_recycle.setLayoutManager(new LinearLayoutManager(this));
        if (mTitle_name.equals("回复我的")){
            mILikeAndCommentsToMePresenter.getCommentsToMe(pageNo,pageSize,mToken);
        }else if(mTitle_name.equals("获得的赞")){
            mILikeAndCommentsToMePresenter.getLikeToMe(pageNo,pageSize,mToken);
        }
//        else {
//            mILikeAndCommentsToMePresenter.getCommentsToMe(pageNo,pageSize,mToken);
//        }
    }

    //Adapter那边上拉刷新回调接口实现类对象，上拉刷新后回调到该方法
    private LoadMore mLoadMore = new LoadMore() {
        @Override
        public void loadMoreCallBack() {
            pageNo++;
            Log.d(TAG, "LikeAndCommentsToMeActivity处的上拉到底刷新方法调用");
            if (mTitle_name.equals("回复我的")){
                mILikeAndCommentsToMePresenter.getCommentsToMe(pageNo,pageSize,mToken);
            }else if(mTitle_name.equals("获得的赞")){
                mILikeAndCommentsToMePresenter.getLikeToMe(pageNo,pageSize,mToken);
            }
        }
    };

    //初始化点击事件
    private void initOnClick() {
        return_back.setOnClickListener(this);
    }
    //点击事件具体逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_back:
                finish();
                break;
        }
    }
    //得到点赞我的人的列表成功
    @Override
    public void getLikeToMeSuccess(List<LikeToMe> likeToMes) {
        Log.d(TAG, "LikeAndCommentsToMeActivity处获得点赞我的人的列表成功，个数-->"+likeToMes.size());
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(likeToMes);
        mLikeAndCommentsToMeAdapter.addLikeAndCommentsToMe(objects);
    }
    //得到点赞我的人的列表失败
    @Override
    public void getLikeToMeFailed(Throwable throwable) {
        Log.d(TAG, "LikeAndCommentsToMeActivity处获得点赞我的人的列表失败-->"+throwable);
    }
    //得到评论我的人的列表成功
    @Override
    public void getCommentsToMeSuccess(List<CommentsToMe> commentsToMes) {
        Log.d(TAG, "LikeAndCommentsToMeActivity处获得评论我的人的列表成功，个数-->"+commentsToMes.size());
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(commentsToMes);
        mLikeAndCommentsToMeAdapter.addLikeAndCommentsToMe(objects);
    }
    //得到评论我的人的列表成功
    @Override
    public void getCommentsToMeFailed(Throwable throwable) {
        Log.d(TAG, "LikeAndCommentsToMeActivity处获得评论我的人的列表失败-->"+throwable);
    }
}