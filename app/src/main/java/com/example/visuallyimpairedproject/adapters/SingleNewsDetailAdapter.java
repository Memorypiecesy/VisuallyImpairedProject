package com.example.visuallyimpairedproject.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.Comments;
import com.example.visuallyimpairedproject.bean.SingleNewsDetail;
import com.example.visuallyimpairedproject.bean.User;

import java.util.List;

public class SingleNewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SingleNewsDetailAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Object> mSingleNewsDetails;
    private int CommentsCount;
    private String BASE_URL = RetrofitClient.BASE_URL +"/";
    public SingleNewsDetailAdapter(Context context, List<Object> singleNewsDetail) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mSingleNewsDetails = singleNewsDetail;
    }

    //往单个新闻详情页面加数据，应该用来加载更多评论
    public void addSingleNewsDetail(List<Object> singleNewsDetails) {
        Log.d(TAG, "addSingleNewsDetail-->添加元素前集合中元素个数为"+mSingleNewsDetails.size());
        int start = mSingleNewsDetails.size();
        mSingleNewsDetails.addAll(singleNewsDetails);
        int end = mSingleNewsDetails.size();
        CommentsCount = singleNewsDetails.size();
        Log.d(TAG, "addSingleNewsDetail-->添加元素后集合中元素个数为"+mSingleNewsDetails.size());
        Log.d(TAG, "addSingleNewsDetail-->start为"+start+",end为"+end);
        notifyDataSetChanged();
//        notifyItemRangeChanged(start, (end - start));
    }

    //发表评论后更新全部的方法
    public void updateAll(List<Object> singleNewsDetails) {
        mSingleNewsDetails.clear();
        mSingleNewsDetails.addAll(singleNewsDetails);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder方法被执行");
        View view = mLayoutInflater.inflate(viewType, parent, false);
        if (viewType == R.layout.news_detail) {
            return new SingleNewsDetailViewHolder(view);
        } else if (viewType == R.layout.news_comment_above_content) {
            return new TextViewHolder(view);
        } else {
            return new CommentsContentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "position-->" + position);
        Object object = mSingleNewsDetails.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case R.layout.news_detail:
                SingleNewsDetail singleNewsDetail = (SingleNewsDetail) object; //把object对象转为SingleNewsDetail对象
                SingleNewsDetailViewHolder singleNewsDetailViewHolder = (SingleNewsDetailViewHolder) holder;
                Log.d(TAG, "SingleNewsDetail-->"+singleNewsDetail);
                singleNewsDetailViewHolder.title_text.setText(singleNewsDetail.getTitle()); //标题
                singleNewsDetailViewHolder.title_text.setContentDescription("新闻标题："+singleNewsDetail.getTitle()); //标题无障碍提示
                singleNewsDetailViewHolder.author_name_text.setText(singleNewsDetail.getAuthorName()); //作者名字
                singleNewsDetailViewHolder.author_name_text.setContentDescription("作者名字："+singleNewsDetail.getAuthorName()); //作者名字无障碍提示
                singleNewsDetailViewHolder.post_time_text.setText(singleNewsDetail.getFinalTime().substring(0,10)); //新闻发表时间
                singleNewsDetailViewHolder.post_time_text.setContentDescription("新闻发表时间："+singleNewsDetail.getFinalTime().substring(0,10)); //新闻发表时间无障碍提示
                singleNewsDetailViewHolder.view_count_text.setText(singleNewsDetail.getViewCount()+"次浏览"); //浏览量
                singleNewsDetailViewHolder.view_count_text.setContentDescription("浏览量："+singleNewsDetail.getViewCount()+"次浏览"); //浏览量无障碍提示
                singleNewsDetailViewHolder.image_description_text.setText(singleNewsDetail.getPhotoContext()); //图片的描述
                singleNewsDetailViewHolder.image_description_text.setContentDescription("图片的描述："+singleNewsDetail.getPhotoContext()); //图片的描述无障碍提示
                singleNewsDetailViewHolder.news_content_text.setText(singleNewsDetail.getContext()); //新闻正文
                Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+singleNewsDetail.getContextImage()).into(singleNewsDetailViewHolder.news_detail_image); //图片
                Log.d(TAG, "图片地址-->"+RetrofitClient.BASE__IMAGE_URL+singleNewsDetail.getContextImage());
                //如果已关注该作者，则将按钮变为“已关注”状态
                if (singleNewsDetail.isFans()){
                    singleNewsDetailViewHolder.follow_image.setImageResource(R.drawable.followed_button);
                }
                Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+singleNewsDetail.getAuthorPhoto()).into(singleNewsDetailViewHolder.author_avatar_image); //新闻作者头像
                Log.d(TAG, "新闻作者头像地址-->"+RetrofitClient.BASE__IMAGE_URL+singleNewsDetail.getAuthorPhoto());
                break;
            case R.layout.news_comments:
                Comments comments = (Comments) object;
                User user = comments.getUser();
                CommentsContentViewHolder commentsContentViewHolder = (CommentsContentViewHolder) holder;
                //如果position>1，代表不是第一条评论，上面的RelativeLayout要隐藏；否则将第一条评论上面的评论数修改
                if (position>1){
                    commentsContentViewHolder.relative_above.setVisibility(View.GONE);
                }else {
                    commentsContentViewHolder.comments_count_total.setText("评论数"+CommentsCount);
                }
                commentsContentViewHolder.username_text.setText(user.getUsername()); //评论的人的用户名
                commentsContentViewHolder.username_text.setContentDescription("该评论的人的名字："+user.getUsername()); //评论的人的用户名无障碍提示
                commentsContentViewHolder.comments_time_text.setText(comments.getCreateDate()); //评论的时间
                commentsContentViewHolder.comments_time_text.setContentDescription("该评论的时间："+comments.getCreateDate()); //评论的时间无障碍提示
                commentsContentViewHolder.comments_content_text.setText(comments.getContent()); //评论的内容
                commentsContentViewHolder.comments_content_text.setContentDescription("该评论的内容："+comments.getContent()); //评论的内容无障碍提示
                commentsContentViewHolder.like_amount_text_below.setText(String.valueOf(comments.getLikeCount())); //点赞数文字
                commentsContentViewHolder.like_amount_text_below.setContentDescription("该评论点赞数："+comments.getLikeCount()); //点赞数文字无障碍提示
                commentsContentViewHolder.comments_reply_amount_text.setText(comments.getChildrens().size()+"条回复"); //评论回复数
                commentsContentViewHolder.comments_reply_amount_text.setContentDescription("该评论回复数"+comments.getChildrens().size()+"条回复"); //评论回复数无障碍提示
                Glide.with(mContext).load(BASE_URL+user.getPhoto()).into(commentsContentViewHolder.comments_person_avatar); //评论者头像
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSingleNewsDetails.get(position) instanceof Comments){
            return R.layout.news_comments;
        }else {
            return R.layout.news_detail;
        }
//        if (position == 0) {
//            return R.layout.news_detail;
//        } else {
//            return R.layout.news_comments;
//        }
    }

    @Override
    public int getItemCount() {
        return mSingleNewsDetails.size();
    }

    //只有文字的ViewHolder(评论数与点赞数)
    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView comments_count; //评论数
        TextView like_amount_text; //点赞数

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            comments_count = itemView.findViewById(R.id.comments_count);
            like_amount_text = itemView.findViewById(R.id.like_amount_text);
        }
    }

    //评论内容ViewHolder
    class CommentsContentViewHolder extends RecyclerView.ViewHolder {
        TextView username_text; //用户名
        TextView comments_time_text; //评论时间
        TextView comments_content_text; //评论内容
        TextView like_amount_text_below; //点赞数文字
        TextView comments_reply_amount_text; //评论回复数
        TextView comments_count_total; //第一条评论上面的评论总数
        ImageView comments_person_avatar; //评论者的头像
        ImageView like_image; //点赞的图标
        CardView comments_reply_card; //评论回复展开
        RelativeLayout relative_above; //第一条评论上面点赞数和评论数的根布局

        public CommentsContentViewHolder(@NonNull View itemView) {
            super(itemView);
            comments_person_avatar = itemView.findViewById(R.id.comments_person_avatar);
            username_text = itemView.findViewById(R.id.username_text); //用户名
            comments_time_text = itemView.findViewById(R.id.comments_time_text);
            like_image = itemView.findViewById(R.id.like_image);
            like_amount_text_below = itemView.findViewById(R.id.like_amount_text_below);
            comments_content_text = itemView.findViewById(R.id.comments_content_text);
            comments_reply_card = itemView.findViewById(R.id.comments_reply_card);
            comments_reply_amount_text = itemView.findViewById(R.id.comments_reply_amount_text);
            relative_above = itemView.findViewById(R.id.relative_above);
            comments_count_total = itemView.findViewById(R.id.comments_count_total);
        }
    }

    //新闻详情ViewHolder
    class SingleNewsDetailViewHolder extends RecyclerView.ViewHolder {
        TextView title_text; //标题
        TextView author_name_text; //作者名字
        TextView post_time_text; //新闻发表时间
        TextView view_count_text; //浏览量
        TextView image_description_text; //描述图片的文字
        TextView news_content_text; //新闻正文
        ImageView author_avatar_image; //作者头像
        ImageView follow_image; //关注按钮
        ImageView news_detail_image; //新闻开头的图片

        public SingleNewsDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            title_text = itemView.findViewById(R.id.title_text);
            author_avatar_image = itemView.findViewById(R.id.author_avatar_image);
            author_name_text = itemView.findViewById(R.id.author_name_text);
            post_time_text = itemView.findViewById(R.id.post_time_text);
            view_count_text = itemView.findViewById(R.id.view_count_text);
            follow_image = itemView.findViewById(R.id.follow_image);
            news_detail_image = itemView.findViewById(R.id.news_detail_image);
            image_description_text = itemView.findViewById(R.id.image_description_text);
            news_content_text = itemView.findViewById(R.id.news_content_text);
        }
    }
}
