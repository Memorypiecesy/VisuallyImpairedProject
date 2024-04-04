package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;

public interface CameraContract {
    interface ICameraView {
        void identifySuccess(OcrHistory ocrHistory); //识别成功
        void identifyFailed(Throwable throwable); //识别失败
        void postImageSuccess(String imagePath); //上传图片成功
        void postImageFailed(Throwable throwable); //上传图片失败
    }
    interface ICameraPresenter {
        void getIdentificationResult(String imagePath,String token);
        void postImage(MultipartBody.Part part);
    }
    interface ICameraModel {
        Flowable<BaseBean<OcrHistory>> getIdentificationResult(String imagePath, String token);
        Flowable<BaseBean<String>> postImage(MultipartBody.Part part);
    }
}
