package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.presenter.SingleNewsDetailPresenter;
import com.example.visuallyimpairedproject.adapters.SingleNewsDetailAdapter;
import com.example.visuallyimpairedproject.bean.Comments;
import com.example.visuallyimpairedproject.bean.CommentsVo;
import com.example.visuallyimpairedproject.bean.SingleNewsDetail;
import com.example.visuallyimpairedproject.interfaces.SingleNewsDetailContract;
import com.example.visuallyimpairedproject.utils.MediaPlayerHelper;
import com.example.visuallyimpairedproject.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleNewsDetailActivity extends AppCompatActivity implements SingleNewsDetailContract.ISingleNewsDetailView, View.OnClickListener, TextToSpeech.OnInitListener {

    private static final String TAG = "SingleNewsDetail";
    @BindView(R.id.news_detail_recycler)
    RecyclerView news_detail_recycler; //新闻详情的RecyclerView
    @BindView(R.id.listen_news_below_relative)
    RelativeLayout listen_news_below_relative; //听新闻框框RelativeLayout
    @BindView(R.id.cancel_relative)
    RelativeLayout cancel_relative; //听新闻框框取消RelativeLayout
    @BindView(R.id.times_speed_relative)
    RelativeLayout times_speed_relative; //倍速调整RelativeLayout
    @BindView(R.id.listen_news_above_relative)
    RelativeLayout listen_news_above_relative; //听新闻框框RelativeLayout
    @BindView(R.id.comments_count_card)
    MaterialCardView comments_count_card; //评论数的CardView
    @BindView(R.id.comments_card)
    MaterialCardView comments_card; //发表看法的CardView
    @BindView(R.id.times_speed_card)
    MaterialCardView times_speed_card; //倍速调整的CardView
    @BindView(R.id.comments_count_text)
    TextView comments_count_text; //评论数的TextView
    @BindView(R.id.times_speed_text)
    TextView times_speed_text; //评论数的TextView
    @BindView(R.id.like_image)
    ImageView like_image; //点赞ImageView
    @BindView(R.id.collection_image)
    ImageView collection_image; //收藏ImageView
    @BindView(R.id.transmit_image)
    ImageView transmit_image; //转发ImageView
    @BindView(R.id.single_news_comments_image)
    ImageView single_news_comments_image; //评论ImageView
    @BindView(R.id.return_back)
    ImageView return_back; //返回ImageView
    @BindView(R.id.cancel_image)
    ImageView cancel_image; //听新闻框框取消ImageView
    @BindView(R.id.play_image)
    ImageView play_image; //播放和暂停播放ImageView
    private SingleNewsDetailContract.ISingleNewsDetailPresenter mISingleNewsDetailPresenter;
    private SingleNewsDetailAdapter mSingleNewsDetailAdapter;
    private SharedPreferences sp;
    private BottomSheetDialog mBottomSheetDialog; //底部出来的评论Diaglog
    private Long mId; //点进的单个新闻的id
    private String mCommentsContent; //评论的内容
    private double lastX, lastY;
    private MediaPlayerHelper mMediaPlayerHelper;
    private MediaPlayer mMediaPlayer;
    private TextToSpeech mTts;
    private File mFile;
    private String mWavFilePath;
    private String mReadNewsContent;
    //文件根目录
    private String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/";
    private boolean isRefreshAll = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        //初始化触摸事件
        initTouch();
        //初始化点击事件
        initOnClick();
        //初始化SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);
        mWavFilePath = basePath + new SimpleDateFormat("yyMMdd-HHmmss").format(new Date()) + ".wav"; //wav文件的路径
        mFile = new File(mWavFilePath);
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            Log.d(TAG, "createNewFile方法错误-->" + e);
        }
        //得到P层对象
        mISingleNewsDetailPresenter = new SingleNewsDetailPresenter(this);
        //得到SingleNewsDetailAdapter适配器对象
        mSingleNewsDetailAdapter = new SingleNewsDetailAdapter(this, new ArrayList<>());
        //为RecyclerView对象设置适配器和添加布局
        news_detail_recycler.setAdapter(mSingleNewsDetailAdapter);
        news_detail_recycler.setLayoutManager(new LinearLayoutManager(this));
        //得到BaseFragment传过来的单个新闻的id，判断一下，然后请求服务器
        mId = Long.parseLong(String.valueOf(getIntent().getIntExtra("id", -1)));
        if (mId != -1) {
            Log.d(TAG, "热门新闻id-->" + mId);
            isRefreshAll = false;
            mISingleNewsDetailPresenter.getSingleNewsDetail(mId, sp.getString("token", ""));

        }
    }

    //初始化触摸事件
    private void initTouch() {
        listen_news_below_relative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetX = (int) (x - lastX);
                        int offsetY = (int) (y - lastY);
                        listen_news_below_relative.offsetLeftAndRight(offsetX);
                        listen_news_below_relative.offsetTopAndBottom(offsetY);
                        //imageView.layout(imageView.getLeft() + offsetX, imageView.getTop() + offsetY, imageView.getRight() + offsetX, imageView.getBottom() + offsetY);
                        break;
                }

                return true;
            }
        });
    }

    //初始化点击事件
    private void initOnClick() {
        single_news_comments_image.setOnClickListener(this); ////评论ImageView
        comments_card.setOnClickListener(this); //发表看法的CardView
        return_back.setOnClickListener(this); //返回箭头
        //取消听新闻框框
        cancel_image.setOnClickListener(this);
        cancel_relative.setOnClickListener(this);
        //修改倍速
        times_speed_relative.setOnClickListener(this);
        times_speed_card.setOnClickListener(this);
        play_image.setOnClickListener(this); //暂停和播放按钮
        listen_news_above_relative.setOnClickListener(this); //右上角的听新闻RelativeLayout
        like_image.setOnClickListener(this); //点赞ImageView
        collection_image.setOnClickListener(this); //收藏ImageView
    }

    //获取单个新闻详情成功
    @Override
    public void getSingleNewsDetailSuccess(SingleNewsDetail singleNewsDetail) {
        Log.d(TAG, "getSingleNewsDetailSuccess-->返回的SingleNewsDetail为" + singleNewsDetail);
        //拼接要读的内容，并赋值给全局变量
        StringBuilder sb = new StringBuilder();
        sb.append(singleNewsDetail.getTitle()).append(singleNewsDetail.getPhotoContext()).append(singleNewsDetail.getContext());
        mReadNewsContent = sb.toString();
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(singleNewsDetail);
        //如果是发表评论后，就刷新整个页面，否则只用加载整个页面
        if (isRefreshAll) {
            mSingleNewsDetailAdapter.updateAll(objects);
        } else {
            mSingleNewsDetailAdapter.addSingleNewsDetail(objects);
            mTts = new TextToSpeech(this, this); //得到当新闻详情，拼接好要读的内容后再初始化TextToSpeech对象来把文字转换为语音
        }
        mISingleNewsDetailPresenter.getSingleNewsComments(mId); //成功获取单个新闻后再去请求评论，不然可能评论先得到，就会崩
    }

    //获取单个新闻详情失败
    @Override
    public void getSingleNewsDetailFailed(Throwable throwable) {
        Log.d(TAG, "getSingleNewsDetailFailed方法处获取新闻信息失败-->" + throwable);
        Toast.makeText(this, "获取新闻信息失败", Toast.LENGTH_SHORT).show();
    }

    //得到单个新闻评论成功
    @Override
    public void getSingleNewsCommentsSuccess(List<Comments> comments) {
        Log.d(TAG, "getCommentsSuccess-->返回的Comments数目为" + comments.size());
        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(new Object());
        objects.addAll(comments);
        mSingleNewsDetailAdapter.addSingleNewsDetail(objects);
    }

    //得到单个新闻评论失败
    @Override
    public void getSingleNewsCommentsFailed(Throwable throwable) {
        Log.d(TAG, "getCommentsFailed方法处获取新闻信息失败-->" + throwable);
        Toast.makeText(this, "获取新闻评论失败", Toast.LENGTH_SHORT).show();
    }

    //评论文章成功
    @Override
    public void articleCommentsSuccess(String success) {
        Log.d(TAG, "articleCommentsSuccess-->" + success);
    }

    //给文章点赞，上传点赞结果成功
    @Override
    public void postNewsLikeSuccess(String success) {
        Log.d(TAG, "SingleNewsDetailActivity处postNewsLikeSuccess调用-->" + success);
    }

    //给文章点赞，上传点赞结果失败
    @Override
    public void postNewsLikeFailed(Throwable throwable) {
        Log.d(TAG, "SingleNewsDetailActivity处postNewsLikeFailed调用-->" + throwable);
    }

    //收藏文章成功
    @Override
    public void postNewsCollectionSuccess(String success) {
        Log.d(TAG, "SingleNewsDetailActivity处postNewsCollectionSuccess调用-->" + success);
    }

    //收藏文章失败
    @Override
    public void postNewsCollectionFailed(Throwable throwable) {
        Log.d(TAG, "SingleNewsDetailActivity处postNewsCollectionFailed调用-->" + throwable);
    }

    //点击事件的逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comments_card:
                setDialog();
                break;
            case R.id.collection_image: //收藏ImageView
                if (collection_image.getDrawable().getCurrent().getConstantState() ==
                        getResources().getDrawable(R.drawable.collection_black).getConstantState()) {
                    collection_image.setImageResource(R.drawable.collection_already);
                } else {
                    collection_image.setImageResource(R.drawable.collection_black);
                }
                break;
            case R.id.like_image: //点赞ImageView
                if (like_image.getDrawable().getCurrent().getConstantState() ==
                        getResources().getDrawable(R.drawable.like_black).getConstantState()) {
                    like_image.setImageResource(R.drawable.like_blue);
                } else {
                    like_image.setImageResource(R.drawable.like_black);
                }
                break;
            case R.id.single_news_comments_image:
                news_detail_recycler.scrollToPosition(1);
                break;
            case R.id.cross_image:
                mBottomSheetDialog.dismiss();
                break;
            case R.id.send_text:
                if (mCommentsContent.length() > 0 && mCommentsContent.length() <= 150) {
                    CommentsVo commentsVo = new CommentsVo(mId, mCommentsContent, 0L, 0L);
                    mISingleNewsDetailPresenter.articleComments(commentsVo, sp.getString("token", ""));
                    isRefreshAll = true;
                    mISingleNewsDetailPresenter.getSingleNewsDetail(mId, sp.getString("token", "")); //刷新整个新闻详情页
                    mBottomSheetDialog.dismiss(); //让Dialog消失
                } else {
                    Toast.makeText(SingleNewsDetailActivity.this, "评论字数超过限制", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.return_back:
                finish();
                break;
            case R.id.cancel_image:
            case R.id.cancel_relative:
                listen_news_below_relative.setVisibility(View.GONE);
                break;
            case R.id.times_speed_relative:
            case R.id.times_speed_card:
                switch (times_speed_text.getText().toString()) {
                    case "1x":
                        setPlaySpeed(1.5f);
                        times_speed_text.setText("1.5x");
                        break;
                    case "1.5x":
                        setPlaySpeed(2.0f);
                        times_speed_text.setText("2x");
                        break;
                    case "2x":
                        setPlaySpeed(3.0f);
                        times_speed_text.setText("3x");
                        break;
                    case "3x":
                        setPlaySpeed(1.0f);
                        times_speed_text.setText("1x");
                        break;
                }
                break;
            case R.id.listen_news_above_relative:
                listen_news_below_relative.setVisibility(View.VISIBLE);
                break;
            case R.id.play_image:
                Log.d(TAG, "SingleNewsDetailActivity处的播放按钮被点击");
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayerHelper.pauseMediaPlayer();
                        play_image.setImageResource(R.drawable.news_detail_pause);
                    } else {
                        mMediaPlayerHelper.startMediaPlayer();
                        play_image.setImageResource(R.drawable.news_detail_playing);
                    }
                }
                break;
        }
    }

    //设置Dialog的方法
    private void setDialog() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        RelativeLayout bottom = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.comments_body, null);
        bottom.findViewById(R.id.cross_image).setOnClickListener(this); //关闭评论叉叉
        bottom.findViewById(R.id.send_text).setOnClickListener(this); //"发送"TextView
        EditText editText = bottom.findViewById(R.id.comments_edit); //评论的EditText
        TextView textView = bottom.findViewById(R.id.comments_count_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCommentsContent = editText.getText().toString(); //得到每次变化后EditText的内容
                int length = mCommentsContent.length(); //得到评论内容的字数
                textView.setText(length + "/150"); //设置TextView字数
                //如果字数>150,设置TextView的颜色为红色
                if (length > 150) {
                    textView.setTextColor(getResources().getColor(R.color.comments_body_limit_text));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBottomSheetDialog.setContentView(bottom);
        mBottomSheetDialog.show();
    }

    @Override
    public void onInit(int status) {
        Log.d(TAG, "onInit-->" + status);
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(SingleNewsDetailActivity.this, "语言数据丢失/不支持", Toast.LENGTH_SHORT).show();
            }
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTts.setPitch(1.0f);
            //设定语速 ，默认1.0正常语速
            mTts.setSpeechRate(1.0f);
            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    mMediaPlayer = new MediaPlayer(); //得到MediaPlayer对象
                    mMediaPlayerHelper = new MediaPlayerHelper(mMediaPlayer); //创建MediaPlayerHelper对象
                    mMediaPlayerHelper.createMediaPlayer(mWavFilePath); //初始化MediaPlayer：设置数据源等
                }

                @Override
                public void onError(String utteranceId) {
                    Log.d(TAG, "setOnUtteranceProgressListener处onError-->" + utteranceId);
                }
            });
            mTts.synthesizeToFile(mReadNewsContent, null, mFile, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID); //将识别结果转换成wav文件
            Log.d(TAG, "能够到达synthesizeToFile方法");

        }
    }

    //设置MediaPlayer播放倍速
    private boolean setPlaySpeed(float speed) {
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mMediaPlayer.getPlaybackParams();
            params.setSpeed(speed);
            mMediaPlayer.setPlaybackParams(params);
            return true;
        }
        return false;
    }

    //释放资源：MediaPlayer
    @Override
    protected void onDestroy() {
        //释放MediaPlayer资源
        if (mMediaPlayer != null) {
            mMediaPlayerHelper.stopMediaPlayer();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        //释放TextToSpeech资源
        if (mTts != null) {
            mTts.stop(); //停止（不管是否正在朗读都被打断）
            mTts.shutdown(); //关闭释放资源
        }
        //用户如果收藏了就调用收藏接口
        if (collection_image.getDrawable().getCurrent().getConstantState() ==
                getResources().getDrawable(R.drawable.collection_already).getConstantState()) {
            Log.d(TAG, "上传收藏结果");
            mISingleNewsDetailPresenter.postNewsCollection(mId, sp.getString("token", ""));
        }
        //用户如果点赞了就调用点赞接口
        if (like_image.getDrawable().getCurrent().getConstantState() ==
                getResources().getDrawable(R.drawable.like_blue).getConstantState()) {
            Log.d(TAG, "上传点赞结果");
            mISingleNewsDetailPresenter.postNewsLike(mId, sp.getString("token", ""));
        }
        super.onDestroy();
    }
}