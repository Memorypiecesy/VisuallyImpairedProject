package com.example.visuallyimpairedproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.OcrHistory;

import java.util.List;

public class RecognitionResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "MyAttentionAndFansAdapt";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<OcrHistory> mOcrHistories;


    public RecognitionResultAdapter(Context context, List<OcrHistory> ocrHistories) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mOcrHistories = ocrHistories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_recognition_history, parent, false);
        return new RecognitionResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OcrHistory ocrHistory = mOcrHistories.get(position);
        RecognitionResultViewHolder recognitionResultViewHolder = (RecognitionResultViewHolder) holder;
        recognitionResultViewHolder.recognition_history_content_text.setText(ocrHistory.getText()); //识别结果的名字
        recognitionResultViewHolder.recognition_history_name_text.setText("识别完成"+ocrHistory.getTime()); //识别的内容
        Log.d(TAG, "onBindViewHolder得到的图片地址为-->"+RetrofitClient.BASE__IMAGE_URL+"upload/"+ocrHistory.getPhoto());
        Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+"upload/"+ocrHistory.getPhoto()).into(recognitionResultViewHolder.recognition_history_image); //识别的图片
    }

    @Override
    public int getItemCount() {
        return mOcrHistories.size();
    }

    class RecognitionResultViewHolder extends RecyclerView.ViewHolder{

        ImageView recognition_history_image; //识别的图片
        ImageView play_image; //播放按钮
        TextView recognition_history_content_text; //识别的内容
        TextView recognition_history_name_text; //识别结果的名字

        public RecognitionResultViewHolder(@NonNull View itemView) {
            super(itemView);
            recognition_history_image = itemView.findViewById(R.id.recognition_history_image); //识别的图片
            recognition_history_content_text = itemView.findViewById(R.id.recognition_history_content_text); //识别的内容
            play_image = itemView.findViewById(R.id.play_image); //播放按钮
            recognition_history_name_text = itemView.findViewById(R.id.recognition_history_name_text); //识别结果的名字

        }
    }
}
