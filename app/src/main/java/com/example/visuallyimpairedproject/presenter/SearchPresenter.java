package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.interfaces.SearchContract;
import com.example.visuallyimpairedproject.model.SearchModel;

public class SearchPresenter implements SearchContract.ISearchPresenter, SearchContract.ISearchCallBack {

    private SearchContract.ISearchView mISearchView;
    private SearchContract.ISearchModel mISearchModel;

    public SearchPresenter(SearchContract.ISearchView iSearchView) {
        mISearchView = iSearchView;
        mISearchModel = new SearchModel();
    }

    //录音转文字
    @Override
    public void recordToText(String filePath,double recordTime) {
        mISearchView.showVoiceRecognitionRelative(); //通知V层展示"语音识别中"的进度框
        mISearchModel.recordToText(filePath,recordTime,this);
    }

    //录音转文字成功的回调
    @Override
    public void onRecordToTextSuccess(String result) {
        mISearchView.dismissVoiceRecognitionRelative(); //无论成功与否，都通知V层关闭"语音识别中"的进度框
        mISearchView.recordToTextSuccess(result);
    }

    //录音转文字失败的回调
    @Override
    public void onRecordToTextFailure(String errorMsg) {
        mISearchView.dismissVoiceRecognitionRelative(); //无论成功与否，都通知V层关闭"语音识别中"的进度框
        mISearchView.recordToTextFailure(errorMsg);
    }
}
