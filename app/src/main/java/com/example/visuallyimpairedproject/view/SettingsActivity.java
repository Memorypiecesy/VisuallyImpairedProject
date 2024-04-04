package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.settings_user_feedback_relative) RelativeLayout settings_user_feedback_relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initOnClick();
    }
    //初始化点击事件
    private void initOnClick() {
        settings_user_feedback_relative.setOnClickListener(this);
    }
    //点击事件具体逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_user_feedback_relative:
                Intent feedBackActivityIntent = new Intent(SettingsActivity.this,FeedBackActivity.class);
                startActivity(feedBackActivityIntent);
                break;
        }
    }
}