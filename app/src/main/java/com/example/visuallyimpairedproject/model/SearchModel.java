package com.example.visuallyimpairedproject.model;

import android.util.Log;

import com.example.visuallyimpairedproject.interfaces.SearchContract;
import com.lx.cloud.ai.SpeechUtility;
import com.lx.cloud.ai.iat.CallBack;
import com.lx.cloud.ai.iat.IatClient;
import com.lx.cloud.ai.iat.IatResult;
import com.lx.cloud.ai.iat.SessionBean;

import java.io.File;

public class SearchModel implements SearchContract.ISearchModel {

    private final String TAG = "SearchModel";
    private final String accessKey = "97f18d763f7c49848d5de7584066fdf8";
    private final String secretKey = "f0287b57c0bc434d9c4390525e07f163";
    //构造方法处初始化SDK
    public SearchModel(){
        SpeechUtility.getInstance().init(accessKey, secretKey);
    }

    @Override
    public void recordToText(String filePath,double recordTime, SearchContract.ISearchCallBack iSearchCallBack) {
        //录音时间<=1.00秒，录音失败
        if (recordTime < 1.00){
            iSearchCallBack.onRecordToTextFailure("录音时间太短！");
            return;
        }
        //语音听写所需要的参数SessionBean
        SessionBean sessionBean = new SessionBean();
        sessionBean.setAue("raw");
        sessionBean.setRst("plain");
        sessionBean.setBos(15000);
        sessionBean.setEos(15000);
        sessionBean.setLanguage("cn"); //必填！！
        File file = new File(filePath);
        IatClient.getInstance().startListening(file, sessionBean, new CallBack.IatListen() {
            @Override
            public void onIatResponse(IatResult iatResult) {
                int size = iatResult.body.frameResults.size();
                //判断返回结果的集合是否为空，不为空才取第一项
                if(size>0){
                    String result = iatResult.body.frameResults.get(0).ansStr;
                    Log.d(TAG, "onIatResponse-->"+result);
                    iSearchCallBack.onRecordToTextSuccess(result);
                    //删除录音文件
                    if (file.exists()){
                        file.delete();
                    }
                }
            }

            @Override
            public void onIatError(String s, String s1, String s2) {
                String errorMsg = s+","+s1+","+s2;
                Log.d(TAG, "onIatResponse error-->"+errorMsg);
                iSearchCallBack.onRecordToTextFailure(errorMsg);
            }
        });
    }

}
