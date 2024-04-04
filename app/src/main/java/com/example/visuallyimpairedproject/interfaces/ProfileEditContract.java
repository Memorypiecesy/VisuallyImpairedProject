package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.User;

//NewsFragment的契约接口
public interface ProfileEditContract {
    interface IProfileEditView {

    }
    interface IProfileEditPresenter {
        void uploadProfile(User user); //上传个人资料
    }
    interface IProfileEditModel {
        void uploadProfile(User user); //上传个人资料
    }

}
