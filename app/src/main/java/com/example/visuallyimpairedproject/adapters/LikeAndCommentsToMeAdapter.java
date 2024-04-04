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
import com.example.visuallyimpairedproject.bean.CommentsToMe;
import com.example.visuallyimpairedproject.bean.LikeToMe;

import java.util.List;

public class LikeAndCommentsToMeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "LikeAndCommentsToMeAda";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Object> mObjects;

    public LikeAndCommentsToMeAdapter(Context context, List<Object> objects){
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mObjects = objects;
    }

    //上拉到底刷新
    public void addLikeAndCommentsToMe(List<Object> objects) {
        int start = mObjects.size();
        mObjects.addAll(objects);
        int end = mObjects.size();
        Log.d(TAG, "LikeAndCommentsToMeAdapter处addLikeAndCommentsToMe方法调用");
        notifyItemRangeChanged(start,(end-start));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "LikeAndCommentsToMeAdapter处onCreateViewHolder方法执行");
        View view = mLayoutInflater.inflate(viewType, parent, false);
        if (viewType == R.layout.item_like_to_me) {
            return new LikeToMeViewHolder(view);
        } else {
            return new CommentsToMeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = mObjects.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case R.layout.item_like_to_me:
                LikeToMeViewHolder likeToMeViewHolder = (LikeToMeViewHolder) holder;
                LikeToMe likeToMe = (LikeToMe) object;
                Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+likeToMe.getPhoto()).into(likeToMeViewHolder.like_to_me_person_image); //点赞我的人的头像
                likeToMeViewHolder.like_to_me_person_name_text.setText(likeToMe.getAuthorName()); //点赞我的人的名字
                Log.d(TAG, "获得的赞头像地址-->"+RetrofitClient.BASE__IMAGE_URL+likeToMe.getPhoto());
                break;
            case R.layout.item_comments_to_me:
                CommentsToMeViewHolder commentsToMeViewHolder = (CommentsToMeViewHolder) holder;
                CommentsToMe commentsToMe = (CommentsToMe) object;
                Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+commentsToMe.getPhoto()).into(commentsToMeViewHolder.comments_to_me_person_image); //评论我的人的头像
                commentsToMeViewHolder.comments_to_me_person_name_text.setText(commentsToMe.getUserName()); //评论我的人的昵称
                commentsToMeViewHolder.comments_to_me_content_text.setText(commentsToMe.getContext()); //评论我的人的内容
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjects.get(position) instanceof LikeToMe){
            Log.d(TAG, "是LikeToMe对象");
            return R.layout.item_like_to_me;
        }else {
            Log.d(TAG, "是CommentsToMe对象");
            return R.layout.item_comments_to_me;
        }
    }

    class LikeToMeViewHolder extends RecyclerView.ViewHolder {

        ImageView like_to_me_person_image; //点赞我的人的头像
        TextView like_to_me_person_name_text; //点赞我的人的昵称

        public LikeToMeViewHolder(@NonNull View itemView) {
            super(itemView);
            like_to_me_person_image = itemView.findViewById(R.id.like_to_me_person_image); //点赞我的人的头像
            like_to_me_person_name_text = itemView.findViewById(R.id.like_to_me_person_name_text); //点赞我的人的昵称
        }
    }

    class CommentsToMeViewHolder extends RecyclerView.ViewHolder {

        ImageView comments_to_me_person_image; //评论我的人的头像
        TextView comments_to_me_content_text; //用户回复我的内容
        TextView comments_to_me_person_name_text; //评论我的人的昵称

        public CommentsToMeViewHolder(@NonNull View itemView) {
            super(itemView);
            comments_to_me_person_image = itemView.findViewById(R.id.comments_to_me_person_image); //评论我的人的头像
            comments_to_me_content_text = itemView.findViewById(R.id.comments_to_me_content_text); //用户回复我的内容
            comments_to_me_person_name_text = itemView.findViewById(R.id.comments_to_me_person_name_text); //评论我的人的昵称
        }
    }
}
