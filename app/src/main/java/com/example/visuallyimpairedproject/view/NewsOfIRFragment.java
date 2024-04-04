package com.example.visuallyimpairedproject.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.visuallyimpairedproject.presenter.NewsOfIRPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.adapters.RecognitionResultAdapter;
import com.example.visuallyimpairedproject.bean.OcrHistory;
import com.example.visuallyimpairedproject.interfaces.NewsOfIRContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsOfIRFragment extends Fragment implements NewsOfIRContract.INewsOfIRView {

    private static final String TAG = "NewsOfIRFragment";
    private RecognitionResultAdapter mRecognitionResultAdapter;
    private NewsOfIRContract.INewsOfIRPresenter mINewsOfIRPresenter;
    private SharedPreferences sp;
    @BindView(R.id.news_of_ir_fragment_recycle) RecyclerView news_of_ir_fragment_recycle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化SharedPreferences
        sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mINewsOfIRPresenter = new NewsOfIRPresenter(this); //得到P层对象
        mINewsOfIRPresenter.getOcrResultCollection(sp.getString("token",""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_of_i_r, container, false);
        ButterKnife.bind(this,rootView); //绑定黄油刀
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    //得到收藏的OCR识别结果列表成功
    @Override
    public void getOcrResultCollectionSuccess(List<OcrHistory> ocrHistories) {
        Log.d(TAG, "NewsOfIRFragment处getOcrResultCollectionSuccess方法调用，收藏的OCR识别结果数目为-->"+ocrHistories.size());
        //创建适配器
        mRecognitionResultAdapter = new RecognitionResultAdapter(getActivity(),ocrHistories);
        //给RecyclerView对象设置适配器与布局
        news_of_ir_fragment_recycle.setAdapter(mRecognitionResultAdapter);
        news_of_ir_fragment_recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    //得到收藏的OCR识别结果列表失败
    @Override
    public void getOcrResultCollectionFailed(Throwable throwable) {
        Log.d(TAG, "NewsOfIRFragment处getOcrResultCollectionFailed方法调用-->"+throwable);
    }
}