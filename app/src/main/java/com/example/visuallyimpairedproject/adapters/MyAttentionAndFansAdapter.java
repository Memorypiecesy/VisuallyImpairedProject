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
import com.example.visuallyimpairedproject.bean.AttentionAndFans;

import java.util.List;

public class MyAttentionAndFansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MyAttentionAndFansAdapt";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<AttentionAndFans> mAttentionAndFans;

    public MyAttentionAndFansAdapter(Context context, List<AttentionAndFans> attentionAndFans) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mAttentionAndFans = attentionAndFans;
    }

    //加关注或粉丝数据
    public void addAttentionOrFans(List<AttentionAndFans> attentionAndFans) {
        Log.d(TAG, "addSingleNewsDetail-->添加元素前集合中元素个数为" + mAttentionAndFans.size());
        int start = attentionAndFans.size();
        mAttentionAndFans.addAll(attentionAndFans);
        int end = mAttentionAndFans.size();
        Log.d(TAG, "addSingleNewsDetail-->添加元素后集合中元素个数为" + mAttentionAndFans.size());
        Log.d(TAG, "addSingleNewsDetail-->start为" + start + ",end为" + end);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_my_attention_and_fans, parent, false);
        return new MyAttentionAndFansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AttentionAndFans attentionAndFans = mAttentionAndFans.get(position);
        MyAttentionAndFansViewHolder myAttentionAndFansViewHolder = (MyAttentionAndFansViewHolder) holder;
        myAttentionAndFansViewHolder.author_name_text.setText(attentionAndFans.getUsername()); //作者名字
        Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+attentionAndFans.getPhoto()).into(myAttentionAndFansViewHolder.author_avatar_image); //作者头像

    }

    @Override
    public int getItemCount() {
        return mAttentionAndFans.size();
    }

    class MyAttentionAndFansViewHolder extends RecyclerView.ViewHolder {

        private TextView author_name_text; //作者名字
        private ImageView author_avatar_image; //作者头像

        public MyAttentionAndFansViewHolder(@NonNull View itemView) {
            super(itemView);
            author_name_text = itemView.findViewById(R.id.author_name_text); //作者名字
            author_avatar_image = itemView.findViewById(R.id.author_avatar_image); //作者头像
        }
    }
}
