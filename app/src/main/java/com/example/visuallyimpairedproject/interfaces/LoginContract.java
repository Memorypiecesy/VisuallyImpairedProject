package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.Token;
import com.example.visuallyimpairedproject.bean.UserBody;

import io.reactivex.rxjava3.core.Flowable;

public interface LoginContract {
    interface ILoginView {
        void loginSuccess(String token);
        void loginFailed(String error);
    }

    interface ILoginPresenter {
        //         void login(Strin username, String password);
        void login(UserBody userBody);
    }

    interface ILoginModel {
        //        Flowable<BaseBean<String>> login(String username, String password);
        Flowable<BaseBean<Token>> login(UserBody userBody);
    }
}
