package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;

import io.reactivex.rxjava3.core.Flowable;

public interface IdentificationResultContract {
    interface IdentificationResultView {
        void postCollectionSuccess(String postResult); //上传OCR收藏结果成功
        void postCollectionFailed(Throwable throwable); //上传OCR收藏结果失败
    }
    interface IdentificationResultPresenter {
        void postCollection(Long id, String token); //上传OCR收藏结果
    }
    interface IdentificationResultModel {
        Flowable<BaseBean<String>> postCollection(Long id, String token); //上传OCR收藏结果
    }
}
