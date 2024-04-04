package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MsgNotificationActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.comments_to_me_image) ImageView comments_to_me_image; //"回复我的"ImageView
    @BindView(R.id.like_to_me_image) ImageView like_to_me_image; //"获得的赞"ImageView
    @BindView(R.id.system_msg_image) ImageView system_msg_image; //"系统消息"ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_notification);
        ButterKnife.bind(this);
        initOnClick();
    }
    //初始化点击事件
    private void initOnClick() {
        comments_to_me_image.setOnClickListener(this);
        like_to_me_image.setOnClickListener(this);
        system_msg_image.setOnClickListener(this);
    }
    //点击事件具体逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comments_to_me_image:
                Intent commentsToMeIntent = new Intent(MsgNotificationActivity.this,LikeAndCommentsToMeActivity.class);
                commentsToMeIntent.putExtra("title_name","回复我的");
                startActivity(commentsToMeIntent);
                break;
            case R.id.like_to_me_image:
                Intent likeToMeIntent = new Intent(MsgNotificationActivity.this,LikeAndCommentsToMeActivity.class);
                likeToMeIntent.putExtra("title_name","获得的赞");
                startActivity(likeToMeIntent);
                break;
        }

    }
}