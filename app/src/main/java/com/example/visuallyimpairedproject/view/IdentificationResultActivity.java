package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visuallyimpairedproject.presenter.IdentificationResultPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.interfaces.IdentificationResultContract;
import com.example.visuallyimpairedproject.utils.MediaPlayerHelper;
import com.example.visuallyimpairedproject.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentificationResultActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener, IdentificationResultContract.IdentificationResultView {

    private static final String TAG = "IdentificationResult";
    private MediaPlayerHelper mMediaPlayerHelper;
    private MediaPlayer mMediaPlayer;
    private TextToSpeech mTts;
    private File mFile;
    private String mWavFilePath;
    private String mIdentificationResult;
    private Long mId;
    private IdentificationResultContract.IdentificationResultPresenter mIdentificationResultPresenter;
    private SharedPreferences sp;
    //文件根目录
    private String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/";
    @BindView(R.id.identification_result_text) TextView identification_result_text; //识别结果的TextView
    @BindView(R.id.times_speed_text) TextView times_speed_text; //倍速的TextView
    @BindView(R.id.collect_success_text) TextView collect_success_text; //收藏成功的TextView
    @BindView(R.id.return_card) MaterialCardView return_card; //"返回拍照"的CardView
    @BindView(R.id.times_speed_card) MaterialCardView times_speed_card; //倍速的CardView
    @BindView(R.id.collect_success_card) MaterialCardView collect_success_card; //收藏成功的CardView
    @BindView(R.id.collect_cancel_card) MaterialCardView collect_cancel_card; //取消收藏的CardView
    @BindView(R.id.play_image) ImageView play_image; //暂停播放和播放的ImageView
    @BindView(R.id.collection_image) ImageView collection_image; //收藏ImageView
    @BindView(R.id.collection_relative) RelativeLayout collection_relative; //收藏RelativeLayout
    //计时器类,间隔两秒后收藏CardView自动消失(第一个参数是总时间,结束后回调onFinish方法;第二个是间隔时间,每隔这么多时间回调onTick方法)
    private CountDownTimer timer = new CountDownTimer(2000, 2000){

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            collect_success_card.setVisibility(View.GONE); //收藏成功CardView消失
            collect_cancel_card.setVisibility(View.GONE); //取消收藏CardView消失
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_result);
        ButterKnife.bind(this);
        mIdentificationResultPresenter = new IdentificationResultPresenter(this); //得到P层对象
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        Intent intent = getIntent(); //得到CameraActivity传过来的intent
        mIdentificationResult = intent.getStringExtra("IdentificationResult"); //得到识别完后的内容
        mId = intent.getLongExtra("IdentificationId",-1L); //得到id
        identification_result_text.setText(mIdentificationResult);
        initOnClick();
        mWavFilePath = basePath + new SimpleDateFormat("yyMMdd-HHmmss").format(new Date()) + ".wav"; //wav文件的路径
        mFile =new File(mWavFilePath);
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            Log.d(TAG, "createNewFile方法错误-->"+e);
        }
        mTts = new TextToSpeech(this, this); //初始化TextToSpeech对象
    }
    //初始化点击事件
    private void initOnClick() {
        return_card.setOnClickListener(this); //返回箭头
        play_image.setOnClickListener(this); //播放和暂停播放按钮
        times_speed_card.setOnClickListener(this); //倍速的CardView
        collection_relative.setOnClickListener(this); //收藏RelativeLayout
    }
    //点击事件的逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_card:
                finish(); //返回上一个Activity
                break;
            case R.id.collection_relative:
                //如果当前ImageView是已收藏状态,就变为未收藏状态;否则反之
                if (collection_image.getDrawable().getCurrent().getConstantState()==
                        getResources().getDrawable(R.drawable.collection_not_yet).getConstantState()){
                    collection_image.setImageResource(R.drawable.collection_already);
                    collect_success_card.setVisibility(View.VISIBLE); //收藏成功CardView出现
                    collect_cancel_card.setVisibility(View.GONE); //取消收藏CardView消失
                    collect_success_text.setText("收藏成功\n请在\"我的收藏\"中查看"); //设置收藏成功的TextView内容
                }else {
                    collection_image.setImageResource(R.drawable.collection_not_yet);
                    collect_success_card.setVisibility(View.GONE); //收藏成功CardView消失
                    collect_cancel_card.setVisibility(View.VISIBLE); //取消收藏CardView出现
                }
                timer.start();
                break;
            case R.id.play_image:
                Log.d(TAG, "播放按钮被点击");
                if (mMediaPlayer!=null){
                    if (mMediaPlayer.isPlaying()){
                        mMediaPlayerHelper.pauseMediaPlayer();
                        play_image.setImageResource(R.drawable.play_pause);
                    }else {
                        mMediaPlayerHelper.startMediaPlayer();
                        play_image.setImageResource(R.drawable.playing);
                    }
                }
                break;
            case R.id.times_speed_card: //设置倍速
                switch (times_speed_text.getText().toString()){
                    case "1x":
                        setPlaySpeed(1.5f);
                        times_speed_text.setText("1.5x");
                        break;
                    case "1.5x":
                        setPlaySpeed(2.0f);
                        times_speed_text.setText("2x");
                        break;
                    case "2x":
                        setPlaySpeed(3.0f);
                        times_speed_text.setText("3x");
                        break;
                    case "3x":
                        setPlaySpeed(1.0f);
                        times_speed_text.setText("1x");
                        break;
                }
                break;
        }
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "onInit-->" + status);
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(IdentificationResultActivity.this, "语言数据丢失/不支持", Toast.LENGTH_SHORT).show();
            }
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTts.setPitch(1.0f);
            //设定语速 ，默认1.0正常语速
            mTts.setSpeechRate(1.0f);
            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    mMediaPlayer = new MediaPlayer(); //得到MediaPlayer对象
                    mMediaPlayerHelper = new MediaPlayerHelper(mMediaPlayer); //创建MediaPlayerHelper对象
                    mMediaPlayerHelper.createMediaPlayer(mWavFilePath); //初始化MediaPlayer：设置数据源等
                }

                @Override
                public void onError(String utteranceId) {
                    Log.d(TAG, "setOnUtteranceProgressListener处onError-->"+utteranceId);
                }
            });
            mTts.synthesizeToFile(mIdentificationResult,null,mFile,TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID); //将识别结果转换成wav文件
            Log.d(TAG, "能够到达synthesizeToFile方法");

        }
    }
    //设置MediaPlayer播放倍速
    private boolean setPlaySpeed(float speed) {
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mMediaPlayer.getPlaybackParams();
            params.setSpeed(speed);
            mMediaPlayer.setPlaybackParams(params);
            return true;
        }
        return false;
    }

    //释放资源：MediaPlayer
    @Override
    protected void onDestroy() {
        //释放MediaPlayer资源
        if (mMediaPlayer!=null){
            mMediaPlayerHelper.stopMediaPlayer();
            mMediaPlayer.release();
            mMediaPlayer=null;
        }
        //释放TextToSpeech资源
        if (mTts!=null){
            mTts.stop(); //停止（不管是否正在朗读都被打断）
            mTts.shutdown(); //关闭释放资源
        }
        //根据用户有无点击收藏结果上传收藏的内容
        if (collection_image.getDrawable().getCurrent().getConstantState()==
                getResources().getDrawable(R.drawable.collection_already).getConstantState()){
            Log.d(TAG, "上传收藏结果");
            mIdentificationResultPresenter.postCollection(mId,sp.getString("token",""));
        }
        super.onDestroy();
    }
    //上传OCR收藏结果成功
    @Override
    public void postCollectionSuccess(String postResult) {
        Log.d(TAG, "IdentificationResultActivity处上传OCR收藏结果成功-->"+postResult);
    }
    //上传OCR收藏结果失败
    @Override
    public void postCollectionFailed(Throwable throwable) {
        Log.d(TAG, "IdentificationResultActivity处上传OCR收藏结果失败-->"+throwable);
    }
}