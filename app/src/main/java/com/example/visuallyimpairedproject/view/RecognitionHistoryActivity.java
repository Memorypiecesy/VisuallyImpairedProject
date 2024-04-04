package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.presenter.RecognitionHistoryPresenter;
import com.example.visuallyimpairedproject.adapters.RecognitionResultAdapter;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.RecognitionHistoryContract;
import com.example.visuallyimpairedproject.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecognitionHistoryActivity extends AppCompatActivity implements RecognitionHistoryContract.IRecognitionHistoryView {

    private static final String TAG = "RecognitionHistoryV";
    private RecognitionHistoryContract.IRecognitionHistoryPresenter mIRecognitionHistoryPresenter;
    private SharedPreferences sp;
    @BindView(R.id.recognition_history_recycle) RecyclerView recognition_history_recycle;
    private RecognitionResultAdapter mRecognitionResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition_history);
        ButterKnife.bind(this);
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        mIRecognitionHistoryPresenter = new RecognitionHistoryPresenter(this); //得到P层对象
        mIRecognitionHistoryPresenter.getRecognitionHistory(sp.getString("token","")); //调用方法得到OCR识别历史
    }
    //得到OCR识别结果历史成功
    @Override
    public void getRecognitionHistorySuccess(List<OcrHistory> ocrHistories) {
        Log.d(TAG, "RecognitionHistoryActivity处得到OCR识别历史结果成功，个数为：-->"+ocrHistories.size());
        mRecognitionResultAdapter = new RecognitionResultAdapter(this,ocrHistories); //初始化适配器
        recognition_history_recycle.setAdapter(mRecognitionResultAdapter); //设置适配器
        recognition_history_recycle.setLayoutManager(new LinearLayoutManager(this)); //设置布局管理器
    }
    //得到OCR识别结果历史失败
    @Override
    public void getRecognitionHistoryFailed(Throwable throwable) {
        Log.d(TAG, "RecognitionHistoryActivity处得到OCR识别历史结果失败-->"+throwable);
    }
}