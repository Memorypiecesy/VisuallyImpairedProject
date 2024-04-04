package com.example.visuallyimpairedproject.model;

import android.util.Log;

import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.CameraContract;
import com.example.visuallyimpairedproject.interfaces.RetrofitAPI;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.MultipartBody;

public class CameraModel implements CameraContract.ICameraModel {
    private static final String TAG = "CameraModel";

    //调用移动云接口识别图片
    @Override
    public Flowable<BaseBean<OcrHistory>> getIdentificationResult(String imagePath, String token) {
        Log.d(TAG, "getIdentificationResult要识别的图片地址-->"+imagePath);
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).getIdentificationResult(imagePath,token);
    }
    //上传图片
    @Override
    public Flowable<BaseBean<String>> postImage(MultipartBody.Part part) {
        return RetrofitClient.getInstance().getService(RetrofitAPI.class).postImage(part);
    }
}
