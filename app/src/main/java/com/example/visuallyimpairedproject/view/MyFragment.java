package com.example.visuallyimpairedproject.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.visuallyimpairedproject.Client.RetrofitClient;
import com.example.visuallyimpairedproject.presenter.MyPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.User;
import com.example.visuallyimpairedproject.interfaces.MyContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFragment extends Fragment implements MyContract.IMyView {


    private static final String TAG = "MyFragment";
    private MyContract.IMyPresenter mIMyPresenter;
    private SharedPreferences sp;
    private String BASE_URL = RetrofitClient.BASE_URL +"/";
    @BindView(R.id.notification_relative) RelativeLayout notificationRelative; //"消息通知"RelativeLayout
    @BindView(R.id.collection_relative) RelativeLayout collectionRelative; //"收藏"RelativeLayout
    @BindView(R.id.recognition_history_relative) RelativeLayout recognitionHistoryRelative; //"识别历史"RelativeLayout
    @BindView(R.id.browse_history_relative) RelativeLayout browseHistoryRelative; //"浏览历史"RelativeLayout
    @BindView(R.id.feed_back_relative) RelativeLayout feedBackRelative; //"意见反馈"RelativeLayout
    @BindView(R.id.settings_relative) RelativeLayout settingsRelative; //"设置"RelativeLayout
    @BindView(R.id.my_fans_relative) RelativeLayout my_fans_relative; //"粉丝"RelativeLayout
    @BindView(R.id.my_attention_relative) RelativeLayout my_attention_relative; //"关注"RelativeLayout
    //    @BindView(R.id.modify_personal_data_text) TextView modify_personal_data_text; //"修改个人资料"TextView
    @BindView(R.id.fans_text) TextView fans_text; //粉丝数
    @BindView(R.id.follow_text) TextView follow_text; //关注数
    @BindView(R.id.user_name_text) TextView user_name_text; //用户昵称TextView
    @BindView(R.id.modify_personal_data_image) ImageView modify_personal_data_image; //"修改个人资料"ImageView
    @BindView(R.id.user_avatar_image) ImageView user_avatar_image; //用户头像ImageView


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        mIMyPresenter = new MyPresenter(this);
        sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, myFragment);
        return myFragment;
    }

    @OnClick({R.id.notification_relative, R.id.collection_relative, R.id.recognition_history_relative, R.id.browse_history_relative,
            R.id.feed_back_relative, R.id.settings_relative,R.id.modify_personal_data_image,R.id.my_fans_relative,
            R.id.my_attention_relative})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notification_relative:
                Intent MsgNotificationActivityIntent = new Intent(getActivity(),MsgNotificationActivity.class);
                startActivity(MsgNotificationActivityIntent);
                break;
            case R.id.collection_relative:
                Intent MyCollectionActivityIntent = new Intent(getActivity(),MyCollectionActivity.class);
                startActivity(MyCollectionActivityIntent);
                break;
            case R.id.recognition_history_relative:
                Intent RecognitionHistoryActivityIntent = new Intent(getActivity(),RecognitionHistoryActivity.class); //跳转到识别历史Activity
                startActivity(RecognitionHistoryActivityIntent);
                break;
            case R.id.browse_history_relative:
                Intent browsingHistoryActivityIntent = new Intent(getActivity(),BrowsingHistoryActivity.class);
                startActivity(browsingHistoryActivityIntent);
                break;
            case R.id.feed_back_relative:
                Intent feedBackActivityIntent = new Intent(getActivity(),FeedBackActivity.class);
                startActivity(feedBackActivityIntent);
                break;
//            case R.id.modify_personal_data_text:
            case R.id.modify_personal_data_image:
                Intent ProfileEditActivityIntent = new Intent(getActivity(),ProfileEditActivity.class); //跳转到个人资料修改Activity
                startActivity(ProfileEditActivityIntent);
                break;
            case R.id.settings_relative: //跳转到设置Activity
                Intent SettingsActivityIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(SettingsActivityIntent);
                break;
            case R.id.my_fans_relative: //跳转到MyAttentionAndFansActivity
                Intent MyAttentionIntent = new Intent(getActivity(), MyAttentionAndFansActivity.class);
                MyAttentionIntent.putExtra("AttentionOrFans","我的粉丝");
                startActivity(MyAttentionIntent);
                break;
            case R.id.my_attention_relative: //跳转到MyAttentionAndFansActivity
                Intent MyFansIntent = new Intent(getActivity(), MyAttentionAndFansActivity.class);
                MyFansIntent.putExtra("AttentionOrFans","我的关注");
                startActivity(MyFansIntent);
                break;
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "回到了MyFragment");
        mIMyPresenter.getUserInfo(sp.getString("token",""));
        super.onResume();
    }
    //获取用户信息成功
    @Override
    public void getUserInfoSuccess(User user) {
        Log.d(TAG, "getUserInfoSuccess-->"+user);
        fans_text.setText(String.valueOf(user.getFans())); //设置粉丝数
        fans_text.setContentDescription("粉丝数："+user.getFans()); //粉丝数无障碍提示
        follow_text.setText(String.valueOf(user.getFollower())); //设置关注数
        follow_text.setContentDescription(String.valueOf(user.getFollower())); //关注数无障碍提示
        user_name_text.setText(user.getUsername()); //设置作者名字
        Log.d(TAG, "用户头像-->"+BASE_URL+user.getPhoto());
        Glide.with(getActivity()).load(BASE_URL+user.getPhoto()).into(user_avatar_image); //用户头像
    }
    //获取用户信息失败
    @Override
    public void getUserInfoFailure(Throwable throwable) {
        Log.d(TAG, "getUserInfoFailure-->"+throwable);
    }
}