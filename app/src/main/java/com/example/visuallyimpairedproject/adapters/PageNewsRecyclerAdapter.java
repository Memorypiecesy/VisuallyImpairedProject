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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.PageNews;
import com.example.visuallyimpairedproject.interfaces.LoadMore;
import com.example.visuallyimpairedproject.interfaces.NewsItemClickListener;

import java.util.List;

public class PageNewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NewsRecyclerAdapter";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<PageNews> mPageNews;
    private LoadMore mLoadMore;
    private NewsItemClickListener mNewsItemClickListener;

    public PageNewsRecyclerAdapter(Context context, List<PageNews> pageNews, LoadMore loadMore, NewsItemClickListener newsItemClickListener){
        mLayoutInflater=LayoutInflater.from(context);
        mContext=context;
        mPageNews=pageNews;
        mLoadMore = loadMore;
        mNewsItemClickListener = newsItemClickListener;
    }
    //下拉刷新
    public void setFrontPageNews(List<PageNews> pageNews){
        mPageNews.clear();
//        Log.d(TAG, "setFrontPageNews方法清除数组元素后数组中剩余元素个数-->"+mPageNews.size());
        mPageNews.addAll(pageNews);
        notifyDataSetChanged();
    }
    //上拉到底刷新
    public void setBehindPageNews(List<PageNews> pageNews){
        int start = mPageNews.size();
        mPageNews.addAll(pageNews);
        int end = mPageNews.size();
        notifyItemRangeChanged(start,(end-start));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(viewType, parent, false);
        if (viewType==R.layout.news_with_three_image){
            return new MultiViewHolder(view);
        }else if (viewType==R.layout.news_without_image){
            return new TextViewHolder(view);
        }else {
            return new LoadMoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "position-->"+position);
        PageNews pageNews = mPageNews.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case R.layout.news_without_image:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.mTitle.setText(pageNews.getTitle());
                if (position!=0&&position!=1&&position!=2){
                    textViewHolder.mHot.setVisibility(View.GONE);
                    //动态的对margin属性进行修改
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)textViewHolder.mAuthorName.getLayoutParams();
                    layoutParams.leftMargin = 0;
                    textViewHolder.mAuthorName.setLayoutParams(layoutParams);
                }else {
                    textViewHolder.mHot.setVisibility(View.VISIBLE);
                    //动态的对margin属性进行修改
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)textViewHolder.mAuthorName.getLayoutParams();
                    layoutParams.leftMargin = 127;
                    textViewHolder.mAuthorName.setLayoutParams(layoutParams);
                }
                textViewHolder.mAuthorName.setText(pageNews.getAuthorName());
                textViewHolder.mCommentsCount.setText(pageNews.getCommentsCount()+"评");
                Log.d(TAG, "该新闻的评论数为-->"+pageNews.getCommentsCount());
                textViewHolder.mFinalTime.setText(pageNews.getFinalTime());
                break;
            case R.layout.news_with_three_image:
                //得到图片URL数组
                List<String> contextImage = pageNews.getContextImage();
                MultiViewHolder multiViewHolder = (MultiViewHolder) holder;
                multiViewHolder.mTitle.setText(pageNews.getTitle());
                multiViewHolder.mAuthorName.setText(pageNews.getAuthorName());
                multiViewHolder.mCommentsCount.setText(pageNews.getCommentsCount()+"评");
                multiViewHolder.mFinalTime.setText(pageNews.getFinalTime());
                //利用Glide显示图片
                Glide.with(mContext).load(contextImage.get(0)).into(multiViewHolder.mImageOne);
                Glide.with(mContext).load(contextImage.get(1)).into(multiViewHolder.mImageTwo);
                Glide.with(mContext).load(contextImage.get(2)).into(multiViewHolder.mImageThree);
                break;
            case R.layout.news_load_more:
                mLoadMore.loadMoreCallBack();
                break;
        }
        //给每个新闻item的根布局RelativeLayout设置点击事件，调用回调方法，将得到的PageNews的id传过去
        ((RelativeLayout)(holder.itemView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewsItemClickListener.newsItemOnClick(pageNews.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPageNews.size();
    }

    @Override
    public int getItemViewType(int position) {
        PageNews pageNews = mPageNews.get(position);
        int size = pageNews.getContextImage().size();
        if (position==mPageNews.size()-1){
            return R.layout.news_load_more;
        }
        if (size==0){
            return R.layout.news_without_image;
        }else {
            return R.layout.news_with_three_image;
        }
    }

    //只有文字的ViewHolder
    class TextViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mHot;
        TextView mAuthorName;
        TextView mCommentsCount;
        TextView mFinalTime;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mHot = itemView.findViewById(R.id.hot);
            mAuthorName = itemView.findViewById(R.id.author_name);
            mCommentsCount = itemView.findViewById(R.id.comments_count);
            mFinalTime = itemView.findViewById(R.id.final_time);
        }
    }
    //三张图片的ViewHolder
    class MultiViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mAuthorName;
        TextView mCommentsCount;
        TextView mFinalTime;
        ImageView mImageOne;
        ImageView mImageTwo;
        ImageView mImageThree;
        public MultiViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mAuthorName = itemView.findViewById(R.id.author_name);
            mCommentsCount = itemView.findViewById(R.id.comments_count);
            mFinalTime = itemView.findViewById(R.id.final_time);
            mImageOne = itemView.findViewById(R.id.image_1);
            mImageTwo = itemView.findViewById(R.id.image_2);
            mImageThree = itemView.findViewById(R.id.image_3);
        }
    }
    //上拉加载更多的ViewHolder
    class LoadMoreViewHolder extends RecyclerView.ViewHolder{

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
