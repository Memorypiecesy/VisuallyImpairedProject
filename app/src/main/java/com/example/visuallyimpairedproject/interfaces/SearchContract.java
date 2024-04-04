package com.example.visuallyimpairedproject.interfaces;

//SearchActivity的契约接口
public interface SearchContract {
    interface ISearchView {
        void showVoiceRecognitionRelative(); //让"语音识别中"的RelativeLayout可见
        void dismissVoiceRecognitionRelative(); //让"语音识别中"的RelativeLayout不可见
        void recordToTextSuccess(String result); //录音转文字成功后View层的操作
        void recordToTextFailure(String errorMsg); //录音转文字失败后View层的操作
    }
    interface ISearchPresenter {
        void recordToText(String filePath,double recordTime); //录音转文字
    }
    interface ISearchModel {
        void recordToText(String filePath,double recordTime,ISearchCallBack iSearchCallBack); //录音转文字
    }
    interface ISearchCallBack {
        void onRecordToTextSuccess(String result); //录音转文字成功的回调
        void onRecordToTextFailure(String errorMsg); //录音转文字失败的回调
    }
}
