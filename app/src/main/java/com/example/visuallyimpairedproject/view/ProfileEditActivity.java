package com.example.visuallyimpairedproject.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.visuallyimpairedproject.presenter.ProfileEditPresenter;
import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.bean.User;
import com.example.visuallyimpairedproject.interfaces.ProfileEditContract;
import com.example.visuallyimpairedproject.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileEditActivity extends AppCompatActivity implements View.OnClickListener, ProfileEditContract.IProfileEditView {

    @BindView(R.id.gender_pop_relative) RelativeLayout gender_pop_relative; //"性别"修改处可点击跳转范围RelativeLayout
    @BindView(R.id.gender_choose_text) TextView gender_choose_text; //"性别"修改内容TextView
    @BindView(R.id.return_back) ImageView return_back; //返回箭头ImageView
    @BindView(R.id.change_avatar_image) ImageView change_avatar_image; //点击更换头像的ImageView
    @BindView(R.id.save_card) MaterialCardView save_card; //保存MaterialCardView
    private BottomSheetDialog mBottomSheetDialog;
    private final int chooseAvatarRequestCode = 1; //调起相册选择头像的请求码
    private ProfileEditPresenter mProfileEditPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);
        initOnClick();
        mProfileEditPresenter = new ProfileEditPresenter(this);
    }
    //初始化点击事件
    private void initOnClick() {
        gender_pop_relative.setOnClickListener(this); //性别处右边可点击范围
    }
    //设置Dialog的方法
    private void setDialog() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        RelativeLayout bottom = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.bottom_dialog, null);
        bottom.findViewById(R.id.not_show_image).setOnClickListener(this);
        bottom.findViewById(R.id.female_image).setOnClickListener(this);
        bottom.findViewById(R.id.male_image).setOnClickListener(this);
        mBottomSheetDialog.setContentView(bottom);
        mBottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.not_show_image:
                mBottomSheetDialog.dismiss();
                gender_choose_text.setText("不展示");
                break;
            case R.id.female_image:
                mBottomSheetDialog.dismiss();
                gender_choose_text.setText("女");
                break;
            case R.id.male_image:
                mBottomSheetDialog.dismiss();
                gender_choose_text.setText("男");
                break;
            case R.id.gender_pop_relative:
                //如果mBottomSheetDialog为空才调用setDialog方法初始化，否则直接show
                if (mBottomSheetDialog==null){
                    setDialog();
                }else {
                    mBottomSheetDialog.show();
                }
                break;
            case R.id.return_back:
                finish();
                break;
            case R.id.change_avatar_image:
                Intent intent = new Intent();
                intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), chooseAvatarRequestCode);
                break;
            case R.id.save_card:
                mProfileEditPresenter.uploadProfile(new User());
                break;
        }
    }

    //选择头像后回调该方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case chooseAvatarRequestCode:
                if (resultCode == RESULT_OK&&data!=null) {
                    change_avatar_image.setImageURI(data.getData()); //通过返回来的Uri设置头像
                }
                break;
        }
    }


}