package com.example.visuallyimpairedproject.presenter;

import com.example.visuallyimpairedproject.bean.User;
import com.example.visuallyimpairedproject.interfaces.ProfileEditContract;
import com.example.visuallyimpairedproject.model.ProfileEditModel;

public class ProfileEditPresenter implements ProfileEditContract.IProfileEditPresenter {

    private ProfileEditContract.IProfileEditModel mIProfileEditModel;
    private ProfileEditContract.IProfileEditView mIProfileEditView;

    public ProfileEditPresenter(ProfileEditContract.IProfileEditView iProfileEditView){
        mIProfileEditView=iProfileEditView;
        mIProfileEditModel=new ProfileEditModel();
    }

    //上传个人资料的方法
    @Override
    public void uploadProfile(User user) {
        mIProfileEditModel.uploadProfile(user);
    }
}
