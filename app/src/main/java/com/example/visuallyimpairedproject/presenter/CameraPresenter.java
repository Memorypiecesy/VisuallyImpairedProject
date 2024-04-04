package com.example.visuallyimpairedproject.presenter;

import android.util.Log;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.CameraContract;
import com.example.visuallyimpairedproject.model.CameraModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class CameraPresenter implements CameraContract.ICameraPresenter {

    private static final String TAG = "CameraPresenter";
    private CameraContract.ICameraModel mICameraModel;
    private CameraContract.ICameraView mICameraView;

    public CameraPresenter(CameraContract.ICameraView iCameraView){
        mICameraView = iCameraView;
        mICameraModel = new CameraModel();
    }
    //得到识别结果
    @Override
    public void getIdentificationResult(String imagePath,String token) {
        mICameraModel.getIdentificationResult(imagePath,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<OcrHistory>>() {
                    @Override
                    public void accept(BaseBean<OcrHistory> listBaseBean) throws Throwable {
                        Log.d(TAG, "CameraPresenter处获取识别结果方法成功-->");
                        mICameraView.identifySuccess(listBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "CameraPresenter处获取识别结果方法失败-->"+throwable);
                        mICameraView.identifyFailed(throwable);
                    }
                });

    }
    //上传图片
    @Override
    public void postImage(MultipartBody.Part part) {
        mICameraModel.postImage(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseBean<String>>() {
                    @Override
                    public void accept(BaseBean<String> stringBaseBean) throws Throwable {
                        Log.d(TAG, "CameraPresenter上传图片方法成功");
                        mICameraView.postImageSuccess(stringBaseBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "CameraPresenter上传图片方法失败-->"+throwable);
                        mICameraView.postImageFailed(throwable);
                    }
                });
    }
}
