package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.AttentionAndFans;
import com.example.visuallyimpairedproject.bean.BaseBean;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface MyAttentionAndFansContract {
    interface IMyAttentionAndFansView {
        void getMyAttentionAndFansSuccess(List<AttentionAndFans> attentionAndFans);
        void getMyAttentionAndFansFailed(Throwable throwable);
    }

    interface IMyAttentionAndFansPresenter {
        void getMyAttention(String token);
        void getMyAFans(String token);
    }

    interface IMyAttentionAndFansModel {
        Flowable<BaseBean<List<AttentionAndFans>>> getMyAttention(String token);
        Flowable<BaseBean<List<AttentionAndFans>>> getMyFans(String token);
    }
}
