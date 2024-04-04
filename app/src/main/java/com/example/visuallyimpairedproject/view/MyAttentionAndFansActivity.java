package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.visuallyimpairedproject.presenter.MyAttentionAndFansPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.MyAttentionAndFansAdapter;
import com.example.visuallyimpairedproject.bean.AttentionAndFans;
import com.example.visuallyimpairedproject.interfaces.MyAttentionAndFansContract;
import com.example.visuallyimpairedproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAttentionAndFansActivity extends AppCompatActivity implements MyAttentionAndFansContract.IMyAttentionAndFansView, View.OnClickListener {

    private static final String TAG = "MyAttentionAndFansV";
    private MyAttentionAndFansContract.IMyAttentionAndFansPresenter mIMyAttentionAndFansPresenter;
    private SharedPreferences sp;
    private MyAttentionAndFansAdapter mMyAttentionAndFansAdapter;
    @BindView(R.id.my_attention_and_fans_recycler) RecyclerView my_attention_and_fans_recycler;
    @BindView(R.id.return_back) ImageView return_back;
    @BindView(R.id.my_attention_and_fans_text) TextView my_attention_and_fans_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention_and_fans);
        ButterKnife.bind(this); //初始化黄油刀
        sp = getSharedPreferences("data", MODE_PRIVATE); //初始化SharedPreferences
        String token = sp.getString("token", ""); //得到token
        mMyAttentionAndFansAdapter = new MyAttentionAndFansAdapter(this,new ArrayList<>());
        mIMyAttentionAndFansPresenter = new MyAttentionAndFansPresenter(this); //得到P层对象
        String attentionOrFans = getIntent().getStringExtra("AttentionOrFans");
        if (attentionOrFans.equals("我的关注")){
            mIMyAttentionAndFansPresenter.getMyAttention(token);
        }else {
            mIMyAttentionAndFansPresenter.getMyAFans(token);
        }
        my_attention_and_fans_text.setText(attentionOrFans); //设置上面的Text是"我的粉丝"还是"我的关注"
        initView();
        initOnClick();
    }
    //初始化点击事件
    private void initOnClick() {
        return_back.setOnClickListener(this);
    }
    //初始化View
    private void initView() {
        my_attention_and_fans_recycler.setAdapter(mMyAttentionAndFansAdapter); //设置适配器
        my_attention_and_fans_recycler.setLayoutManager(new LinearLayoutManager(this)); //设置布局管理器

    }

    //得到关注和粉丝成功
    @Override
    public void getMyAttentionAndFansSuccess(List<AttentionAndFans> attentionAndFans) {
        Log.d(TAG, "MyAttentionAndFansActivity处获取关注和粉丝成功");
        mMyAttentionAndFansAdapter.addAttentionOrFans(attentionAndFans);
        for (AttentionAndFans attentionAndFan : attentionAndFans) {
            Log.d(TAG, "每个粉丝或关注-->"+attentionAndFan);
        }
    }
    //得到关注和粉丝失败
    @Override
    public void getMyAttentionAndFansFailed(Throwable throwable) {
        Log.d(TAG, "MyAttentionAndFansActivity处获取关注和粉丝失败-->"+throwable);
    }
    //初始化点击事件逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_back:
                finish();
                break;
        }
    }
}