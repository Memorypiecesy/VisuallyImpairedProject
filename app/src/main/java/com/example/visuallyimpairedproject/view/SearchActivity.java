package com.example.visuallyimpairedproject.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.presenter.SearchPresenter;
import com.example.visuallyimpairedproject.interfaces.SearchContract;
import com.example.visuallyimpairedproject.utils.AudioManager;
import com.example.visuallyimpairedproject.utils.Utils;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SearchContract.ISearchView {

    private static final String TAG = "SearchActivity";
    private RelativeLayout mVoice_Recognition,mSearch_Page_Relative,mSearch_Box_Relative,mNotification_Relative;
    private TextView mCancel_Text,search_page_text;
    private EditText mSearch_Page_EditText;
    private ImageView mSearch_Page_Image_Clear, mReturn_Back, mRecord_image, mRecord_Box_Top, mRecord_Box_Bottom, mRecord_Box_Bar,mCancel_Circle,mCancel_Cross;
    private SearchContract.ISearchPresenter mISearchPresenter; //ISearchPresenter实现类对象
    private AudioRecord mAudioRecord; //AudioRecord对象
    private static int audioSource = MediaRecorder.AudioSource.MIC; //录音源
    private static int audioRate = 16000; //录音的采样频率
    private static int audioChannel = AudioFormat.CHANNEL_IN_MONO; //录音的声道，单声道
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT; //量化的深度
    private int bufferSize = AudioRecord.getMinBufferSize(audioRate, audioChannel, audioFormat); //缓存的大小
    private AudioManager mAudioManager; //AudioManager对象
    //请求权限的请求码
    private final int RECORD_REQUEST_CODE = 1;
    private final int WRITE_REQUEST_CODE = 2;
    //开始录音的时间和结束录音的时间
    private long startRecordTime;
    private long endRecordTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //请求写文件的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
        }
        initView();
        initOnClick();
    }

    //初始化点击事件
    private void initOnClick() {
        mRecord_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction(); //得到触发的事件的id
                if (action == MotionEvent.ACTION_DOWN) { //按下
                    startRecordTime = System.currentTimeMillis(); //记录录音开始的时间
                    initAudioRecord(); //初始化AudioRecord
                    searchPageViewStateChange(); //让一些View状态改变
                    if(permissionCheck(Manifest.permission.RECORD_AUDIO))
                    mAudioManager.startRecord(mAudioRecord); //开始录音
                } else if (action == MotionEvent.ACTION_UP) { //抬起
                    searchPageViewStateChangeBack(); //让一些View状态改变回来
                    if(permissionCheck(Manifest.permission.RECORD_AUDIO)){
                        String filePath = mAudioManager.stopRecord(); //结束录音
                        endRecordTime = System.currentTimeMillis(); //记录录音结束的时间
                        mISearchPresenter.recordToText(filePath,(endRecordTime-startRecordTime)*1.0/1000);
                    }
                }
                return true;
            }
        }); //录音的图片点击事件
        mSearch_Page_Image_Clear.setOnClickListener(this); //"清空"图标点击事件
        mReturn_Back.setOnClickListener(this); //返回箭头点击事件
        mSearch_Page_EditText.addTextChangedListener(new TextWatcher() { //编辑框文本变化监听事件
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //文本框中文字变化，都让叉号显示
                mSearch_Page_Image_Clear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mSearch_Page_EditText.getText().toString())) {
                    //如果文本框为空了，让叉号消失
                    mSearch_Page_Image_Clear.setVisibility(View.GONE);
                }
            }
        });
        search_page_text.setOnClickListener(this); //右上角"搜索"文字点击事件
    }

    //初始化AudioRecord对象
    private void initAudioRecord() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_REQUEST_CODE);
        }else{
            Log.d(TAG, "initAudioRecord-->AudioRecord初始化了");
            mAudioRecord = new AudioRecord(audioSource,audioRate,audioChannel,audioFormat,bufferSize);
        }
    }
    //初始化各种对象
    private void initView() {
        mNotification_Relative = findViewById(R.id.notification_relative); //"消息通知"ImageView外层的RelativeLayout
        mSearch_Box_Relative = findViewById(R.id.search_box_relative); //搜索框的父布局RelativeLayout
        mSearch_Page_Relative = findViewById(R.id.search_page_relative); //搜索页的根RelativeLayout
        search_page_text = findViewById(R.id.search_page_text); //右上角的"搜索"文字
        mCancel_Cross = findViewById(R.id.cancel_cross); //"取消"叉叉
        mCancel_Circle = findViewById(R.id.cancel_circle); //"取消"圆圈
        mCancel_Text = findViewById(R.id.cancel_text); //"取消"文字
        mVoice_Recognition = findViewById(R.id.voice_recognition); //"语音识别中"的RelativeLayout
        mRecord_Box_Top = findViewById(R.id.record_box_top); //录音时的框框的上半部分
        mRecord_Box_Bottom = findViewById(R.id.record_box_bottom); //录音时的框框的下半部分
        mRecord_Box_Bar = findViewById(R.id.record_box_bar); //录音框中间的条条
        searchPageViewDisappear(); //让一些View消失
        //让"语音识别中"的进度框消失
        mVoice_Recognition.setVisibility(View.GONE);
        mRecord_image = findViewById(R.id.record_image); //初始化录音的图片
        mISearchPresenter = new SearchPresenter(SearchActivity.this); //持有ISearchPresenter实现类对象
        mAudioManager = AudioManager.getInstance(bufferSize); //初始化AudioManager对象
        mSearch_Page_Image_Clear = findViewById(R.id.search_page_image_clear); //初始化"清空"图片
        mSearch_Page_EditText = findViewById(R.id.search_page_edit); //初始化搜索页的EditText
        mReturn_Back = findViewById(R.id.return_back); //返回箭头
        mSearch_Page_Image_Clear.setVisibility(View.GONE); //一开始让"清空"图片消失

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_page_image_clear:
                mSearch_Page_EditText.setText("");
                break;
            case R.id.return_back:
                finish();
                break;
            case R.id.search_page_text:
                //如果搜索框中有文字
                if (mSearch_Page_EditText.getText().toString().length()>0){
                    Intent SearchResultActivityIntent = new Intent(SearchActivity.this,SearchResultActivity.class);
                    SearchResultActivityIntent.putExtra("search_text",mSearch_Page_EditText.getText().toString());
                    startActivity(SearchResultActivityIntent);
                }
                break;
        }
    }

    //动态申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: //请求录音权限成功就调用initAudioRecorder方法初始化AudioRecorder，否则弹出提示
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(SearchActivity.this, "录音权限请求失败", Toast.LENGTH_SHORT).show();

                break;
            case WRITE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SearchActivity.this, "读写权限申请失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //让"语音识别中"的进度框展示
    @Override
    public void showVoiceRecognitionRelative() {
        mVoice_Recognition.setVisibility(View.VISIBLE);
    }
    //让"语音识别中"的进度框消失
    @Override
    public void dismissVoiceRecognitionRelative() {
        mVoice_Recognition.setVisibility(View.GONE);
    }

    //录音转文字成功后Toast返回的结果
    @Override
    public void recordToTextSuccess(String result) {
//        Toast.makeText(SearchActivity.this, result, Toast.LENGTH_SHORT).show();
        Intent SearchResultActivityIntent = new Intent(SearchActivity.this,SearchResultActivity.class);
        SearchResultActivityIntent.putExtra("search_text",result);
        startActivity(SearchResultActivityIntent);
    }

    //录音转文字失败后Toast原因
    @Override
    public void recordToTextFailure(String errorMsg) {
        Toast.makeText(SearchActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
    //判断权限是否已经给予
    private boolean permissionCheck(String permissionString){
        //权限未被允许返回false，否则返回true
        if (ActivityCompat.checkSelfPermission(this, permissionString) != PackageManager.PERMISSION_GRANTED) return false;
        return true;
    }
    //让一些View消失
    private void searchPageViewDisappear() {
        //让录音框不可见
        mRecord_Box_Top.setVisibility(View.GONE);
        mRecord_Box_Bottom.setVisibility(View.GONE);
        mRecord_Box_Bar.setVisibility(View.GONE);
        //让"取消"叉叉、圆圈、文字都消失
        mCancel_Cross.setVisibility(View.GONE);
        mCancel_Circle.setVisibility(View.GONE);
        mCancel_Text.setVisibility(View.GONE);
    }
    //让一些View状态改变
    private void searchPageViewStateChange(){
        //让"取消"叉叉、圆圈、文字都可见
        mCancel_Cross.setVisibility(View.VISIBLE);
        mCancel_Circle.setVisibility(View.VISIBLE);
        mCancel_Text.setVisibility(View.VISIBLE);
        //让录音框可见
        mRecord_Box_Top.setVisibility(View.VISIBLE);
        mRecord_Box_Bottom.setVisibility(View.VISIBLE);
        mRecord_Box_Bar.setVisibility(View.VISIBLE);
        //改变搜索框内组件的背景颜色
        mSearch_Page_Relative.setBackgroundColor(getResources().getColor(R.color.record_black));
        mSearch_Box_Relative.setBackgroundColor(getResources().getColor(R.color.record_black));
        mSearch_Page_EditText.setBackgroundColor(getResources().getColor(R.color.search_box_edit_background));
    }
    //让一些View状态改变回来
    private void searchPageViewStateChangeBack(){
        searchPageViewDisappear();
        //改变搜索框内组件的背景颜色
        mSearch_Page_Relative.setBackgroundColor(getResources().getColor(R.color.search_box_background));
        mSearch_Page_EditText.setBackgroundColor(getResources().getColor(R.color.search_box_background));
        mSearch_Box_Relative.setBackgroundColor(getResources().getColor(R.color.search_box_background));
    }

}