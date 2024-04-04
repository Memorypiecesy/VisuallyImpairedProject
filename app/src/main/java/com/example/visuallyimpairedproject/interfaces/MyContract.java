package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.User;

import io.reactivex.rxjava3.core.Flowable;

public interface MyContract {
    interface IMyView {
        void getUserInfoSuccess(User user);
        void getUserInfoFailure(Throwable throwable);

    }
    interface IMyPresenter {
        void getUserInfo(String token);
    }
    interface IMyModel {
        Flowable<BaseBean<User>> getUserInfo(String token);
    }
}
