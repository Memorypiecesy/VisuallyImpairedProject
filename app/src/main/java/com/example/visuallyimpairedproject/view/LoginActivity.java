package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visuallyimpairedproject.presenter.LoginPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.UserBody;
import com.example.visuallyimpairedproject.interfaces.LoginContract;
import com.example.visuallyimpairedproject.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.ILoginView {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_cardView) CardView login_cardView;
    @BindView(R.id.login_text) TextView login_text; //条款同意文本
    @BindView(R.id.login_circle) ImageView login_circle; //条款同意圈圈
    private LoginContract.ILoginPresenter mILoginPresenter;
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_VisuallyImpairedProject);
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //得到P层的对象
        mILoginPresenter = new LoginPresenter(this);
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        spe = sp.edit();
        String content="我已阅读并同意《服务条款》和《隐私协议》\n以及《中国移动认证服务协议》"; //文本内容在上面已经有了
        SpannableStringBuilder spannable = new SpannableStringBuilder(content);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0E42D2")), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0E42D2")), 14, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#0E42D2")), 23, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_text.setText(spannable);
    }

    @OnClick({R.id.login_cardView,R.id.login_circle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_cardView:
                mILoginPresenter.login(new UserBody("12345678900","123456"));
        }
    }
    //登陆成功
    @Override
    public void loginSuccess(String token) {
        Log.d(TAG, "loginSuccess-->token是"+token);
        spe.putString("token",token);
        spe.apply();
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
    //登陆失败
    @Override
    public void loginFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "loginFailed-->登陆失败，原因是"+error);
    }
}