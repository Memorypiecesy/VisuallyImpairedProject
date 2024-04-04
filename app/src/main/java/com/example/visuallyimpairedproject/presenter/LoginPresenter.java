package com.example.visuallyimpairedproject.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Token;
import com.example.visuallyimpairedproject.bean.UserBody;
import com.example.visuallyimpairedproject.interfaces.LoginContract;
import com.example.visuallyimpairedproject.model.LoginModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.ILoginPresenter {

    private static final String TAG = "LoginPresenter";
    private LoginContract.ILoginView mILoginView;
    private LoginContract.ILoginModel mILoginModel;

    public LoginPresenter(LoginContract.ILoginView iLoginView){
        mILoginView=iLoginView;
        mILoginModel = new LoginModel();
    }

    //调用Model层的login方法
    @Override
    public void login(UserBody userBody) {
        mILoginModel.login(userBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<Token>>() {
                    @Override
                    public void accept(BaseBean<Token> listBaseBean) throws Throwable {
                        String token = listBaseBean.getData().getToken();
                        if (TextUtils.isEmpty(token)){
                            mILoginView.loginFailed(listBaseBean.getMessage());
                        }else {
                            mILoginView.loginSuccess(token);
                        }
                        Log.d(TAG, "accept-->Presenter处的token-->"+listBaseBean.getData().getToken());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        mILoginView.loginFailed(throwable.toString());
                    }
                });
    }
}
