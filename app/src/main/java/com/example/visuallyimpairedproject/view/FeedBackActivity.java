package com.example.visuallyimpairedproject.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

//意见反馈
public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FeedBackActivity";
    private String mFeedBackContent;
    @BindView(R.id.feed_back_page_edit)
    EditText feed_back_page_edit;
    @BindView(R.id.feed_back_page_text)
    TextView feed_back_page_text;
    @BindView(R.id.feed_back_page_card)
    MaterialCardView feed_back_page_card; //保存的CardView

    private int mLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this); //绑定黄油刀
        initView();
        initOnClick();
    }

    //初始化View
    private void initView() {
        //让"语音识别中"的进度框消失
        feed_back_page_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFeedBackContent = feed_back_page_edit.getText().toString(); //得到每次变化后EditText的内容
                //得到评论内容的字数
                mLength = mFeedBackContent.length();
                feed_back_page_text.setText(mLength + "/3000"); //设置TextView字数
                //如果字数3000,设置TextView的颜色为红色
                if (mLength > 3000) {
                    feed_back_page_text.setTextColor(getResources().getColor(R.color.comments_body_limit_text));
                } else {
                    feed_back_page_text.setTextColor(getResources().getColor(R.color.text_search_grey));
                }
                //如果EditText里面有字，CardView变为蓝色，否则变灰
                if (mLength > 0) {
                    feed_back_page_card.setCardBackgroundColor(getResources().getColor(R.color.blue));
                } else {
                    feed_back_page_card.setCardBackgroundColor(getResources().getColor(R.color.text_search_grey));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //初始化点击事件
    private void initOnClick() {
        feed_back_page_card.setOnClickListener(this);
    }

    //点击事件具体逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feed_back_page_card:
                //如果EditText里面有字，则可以保存反馈，否则弹出提示
                if (mLength > 0) {
                    feed_back_page_edit.setText("");
                    Toast.makeText(this, "意见反馈成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "意见反馈字数不能为0!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}