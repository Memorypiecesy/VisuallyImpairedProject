package com.example.visuallyimpairedproject.utils;

import android.media.AudioRecord;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioManager {
    private static final String TAG = "AudioManager";
    //日历类，动态设置，因为new了之后它代表的时间就固定了。
    private Date mDate;
    //日历解析类
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
    //缓存的大小
    private int bufferSize;
    //记录播放状态
    private volatile boolean isRecording = false;
    //数字信号数组
    private byte[] noteArray;
    //文件根目录
    private String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath()+"/";
    //wav文件目录
    private String wavFilePath;
    //pcm文件目录
    private String pcmFilePath;
    //PCM文件
    private File pcmFile;
    //WAV文件
    private File wavFile;
    //AudioRecord对象
    private AudioRecord mAudioRecord;
    //文件输出流
    private OutputStream os;

    //静态方法用于外界得到AudioManager的对象
    public static AudioManager getInstance(int bufferSize) {
        return new AudioManager(bufferSize);
    }

    //给成员变量赋值
    private AudioManager(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    //开始录音
    public void startRecord(AudioRecord audioRecord) {
        createFile(); //创建录音数据填充的pcm文件对象和转换后的wav文件对象
        isRecording = true; //录制的标志为true
        mAudioRecord = audioRecord; //用传过来的AudioRecord给成员变量赋值
        Log.d(TAG, "AudioRecord开始录音前");
        mAudioRecord.startRecording(); //开始录音
        Log.d(TAG, "AudioRecord开始录音后");
        //写数据，顺便转换()
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeData();
            }
        }).start();
    }

    //停止录音，返回pcm文件的路径
    public String stopRecord() {
        isRecording = false; //录制的标志改为false
        mAudioRecord.stop(); //停止录音
        mAudioRecord = null; //让传过来的AudioRecord置空，释放资源
        //停止录音后就要写录音文件
        try {
            new PcmToWavUtil().pcmToWav(pcmFilePath,wavFilePath);
        } catch (Exception e) {
            Log.d(TAG, "Convert error-->" + e);
        }
        releaseFile(); //将文件对象置空，释放资源
        return pcmFilePath;
    }

    //写录音数据
    public void writeData() {
        noteArray = new byte[bufferSize];
        Log.d(TAG, "writeData-->noteArray is"+noteArray);
        //建立文件输出流
        try {
            os = new BufferedOutputStream(new FileOutputStream(pcmFile));
            Log.d(TAG, "writeData-->BufferedOutputStream is"+os);
        } catch (IOException e) {
            Log.d(TAG, "writeData new OutPutStream error-->" + e);
        }
        //当isRecording为true，表示正在录音，一直从AudioRecord中拿到数据并写数据
        while (isRecording) {
            Log.d(TAG, "bufferSize is -->"+bufferSize);
            int read = mAudioRecord.read(noteArray, 0, bufferSize);
            Log.d(TAG, "writeData-->"+read);
            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                try {
                    os.write(noteArray);
                } catch (IOException e) {
                    Log.d(TAG, "writeData write data error-->" + e);
                }
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                Log.d(TAG, "writeData close IO error-->" + e);
            }
        }
    }

    //创建文件夹,首先创建目录，然后创建对应的文件
    private void createFile() {
        //重置时间，得到时间代表的字符串(文件的名称)
        mDate = new Date();
        String tempString = mSimpleDateFormat.format(mDate);
        //根据当前系统时间来设置文件名称
        pcmFilePath = basePath + tempString + ".pcm";
        wavFilePath = basePath + tempString + ".wav";
        Log.d(TAG, "pcmFilePath-->"+pcmFilePath);
        //如果录音文件对象为空才创建对象
        pcmFile = new File(pcmFilePath);
        wavFile = new File(wavFilePath);
        //录音文件已经存在，删除
        if (pcmFile.exists()) {
            pcmFile.delete();
        }
        if (wavFile.exists()){
            wavFile.delete();
        }
        //创建新的录音文件
        try {
            wavFile.createNewFile();
            pcmFile.createNewFile();
        } catch (IOException e) {
            Log.d(TAG, "createFile error-->" + e);
        }
    }
    //让文件对象指向空，释放资源
    private void releaseFile(){
        pcmFile=null;
        wavFile=null;
    }
}
