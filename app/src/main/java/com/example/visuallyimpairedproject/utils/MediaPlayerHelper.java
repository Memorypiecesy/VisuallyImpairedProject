package com.example.visuallyimpairedproject.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;


public class MediaPlayerHelper implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "MediaPlayerHelper";
    private MediaPlayer mMediaPlayer;

    public MediaPlayerHelper(MediaPlayer mediaPlayer){
        mMediaPlayer=mediaPlayer;
    }

    public void createMediaPlayer(String path) {
        Log.d(TAG, "createMediaPlayer处要播放的音频的路径-->"+path);
        //重置到未初始化状态，调用这个方法后需要重新setDataSource设置数据源
        mMediaPlayer.reset();
        try {
            Log.d(TAG, "setDataSource设置数据源前");
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); //设置资源类别
            mMediaPlayer.setDataSource(path); //设置数据源
            Log.d(TAG, "setDataSource设置数据源后");
            mMediaPlayer.setOnPreparedListener(this); //设置准备完成监听者！
            mMediaPlayer.prepareAsync();
            Log.d(TAG, "prepareAsync异步准备后");
        } catch (Exception e) {
            Log.d(TAG, "createMediaPlayer error-->"+e);
        }
    }

    public void startMediaPlayer() {
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            Log.d(TAG, "startMediaPlayer error-->"+e);
        }
    }

    public void pauseMediaPlayer() {
        try  {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            Log.d(TAG, "pauseMediaPlayer error-->"+e);
        }
    }

    public void stopMediaPlayer() {
        try {
            mMediaPlayer.stop();
        } catch (IllegalStateException e) {
            Log.d(TAG, "stopMediaPlayer error->"+e);
        }
    }
    //调用prepareAsync异步加载流媒体资源后会回调该方法，这时候再start就不会出错
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.d(TAG, "onPrepared-->MediaPlayerHelper处的MediaPlayer已准备好并播放");
    }
}
