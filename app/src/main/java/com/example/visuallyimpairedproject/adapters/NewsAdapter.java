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
import com.example.visuallyimpairedproject.bean.News;
import com.example.visuallyimpairedproject.interfaces.LoadMore;

import java.util.List;

//我的收藏和浏览历史中新闻的Adapter
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "MyAttentionAndFansAdapt";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<News> mNews;
    private LoadMore mLoadMore;

    public NewsAdapter(Context context, List<News> news, LoadMore loadMore) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mNews = news;
        mLoadMore = loadMore;
    }

    //上拉到底刷新
    public void addNews(List<News> news){
        int start = mNews.size();
        mNews.addAll(news);
        int end = mNews.size();
        notifyItemRangeChanged(start,(end-start));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.news_without_image_my_collection, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        News news = mNews.get(position);
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.author_name_text.setText(news.getAuthorName()); //作者名字
        newsViewHolder.post_time_text.setText(news.getFinalTime()); //发布时间
        newsViewHolder.title_text.setText(news.getTitle()); //新闻标题
        newsViewHolder.comments_text.setText(String.valueOf(news.getCommentsCount())); //评论数量
        newsViewHolder.like_text.setText(String.valueOf(news.getLikeCount())); //转发数量
        Log.d(TAG, "onBindViewHolder得到的图片地址为-->"+ RetrofitClient.BASE__IMAGE_URL+news.getAuthorPhoto());
        Glide.with(mContext).load(RetrofitClient.BASE__IMAGE_URL+news.getAuthorPhoto()).into(newsViewHolder.author_avatar_image); //作者头像
        if (position==mNews.size()-1){
            mLoadMore.loadMoreCallBack();
        }
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView author_avatar_image; //作者头像
        TextView author_name_text; //作者名字
        TextView post_time_text; //发布时间
        TextView title_text; //新闻标题
        TextView comments_text; //评论数量
        TextView like_text; //点赞数量

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            author_avatar_image = itemView.findViewById(R.id.author_avatar_image); //作者头像
            author_name_text = itemView.findViewById(R.id.author_name_text); //作者名字
            post_time_text = itemView.findViewById(R.id.post_time_text); //发布时间
            title_text = itemView.findViewById(R.id.title_text); //新闻标题
            comments_text = itemView.findViewById(R.id.comments_text); //评论数量
            like_text = itemView.findViewById(R.id.like_text); //转发数量

        }
    }
}
